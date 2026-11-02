package Service;
import Domain.Entity;
import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Repositories.RepoInterface;
import VE.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * operatii pentru useri: adăugare și ștergere.
 * Garantează unicitatea id-ului și a username-ului.
 */
public class UserService {
    /** Repository-ul userilor. */
    private final RepoInterface<Integer, User> userRepo;
    /** Repository-ul prieteniilor pentru a sterge si prietenii. */
    private final RepoInterface<Tuple<Integer, Integer>, Friendship> friendshipRepo;

    /**
     * @param userRepo repository-ul userilor
     * @param friendshipRepo repository-ul prieteniilor
     */
    public UserService(RepoInterface<Integer, User> userRepo,
                           RepoInterface<Tuple<Integer, Integer>, Friendship> friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    /**
     * Generează id-ul următorului user: cel mai mare id existent + 1,
     * sau 1001 dacă nu există niciun user (validatorul cere id > 1000).
     *
     * @return noul id
     */
    private int nextId() {
        int max = userRepo
                .findAll()
                .stream()
                .mapToInt(Entity::getId) //!transformă fiecare user în id-ul lui. Stream-ul era de User, devine de Integer: 1001, 1002, 1005. Entity::getId e referința de tipul 3 (metodă de instanță a unui obiect oarecare), echivalentul lui u -> u.getId().
                .filter(u -> u >= 1000)
                .max()
                .orElse(1000);
        return max + 1;
    }

    /**
     * Creează și adaugă un user nou, cu id generat automat.
     * Username-ul trebuie să fie unic în rețea, iar datele
     * trebuie să treacă de validator (aplicat de repository la save).
     *
     * @param username   numele de utilizator (unic)
     * @param email      adresa de email (trebuie să conțină '@')
     * @param first_name prenumele (nevid)
     * @param last_name  numele de familie (nevid)
     * @throws ValidationException dacă username-ul există deja sau dacă datele sunt invalide
     */
    public void addUser(String username, String email, String first_name, String last_name) {
        userRepo.findAll()
                .stream()
                .filter(existing -> existing.getUsername().equals(username))
                .forEach(existing -> {
                    throw new ValidationException("Username-ul este deja folosit!");
                });

        User u = new User(username, email, first_name, last_name);
        //# validarea se face direct in in-memory repository
        u.setId(nextId());
        userRepo.save(u);
    }

    /**
     * Propaga findAll si pentru straturile mai jos de repo
     * @return Lista de utilizatori din repo
     */
    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * Returneaza utilizatorii ordonati dupa nume
     *
     * @return o lista noua cu utilizatori ordonati
     */
    public List<User> getAllSortedByName() {
        return userRepo.findAll().stream()
                .sorted(Comparator.comparing(User::getLast_name)
                        .thenComparing(User::getFirst_name))
                .collect(Collectors.toList());
    }

    /**
     * Șterge userul cu id-ul dat, împreună cu toate prieteniile lui.
     *
     * @param id id-ul userului de șters
     * @throws ValidationException dacă nu există niciun user cu acest id
     */
    public void deleteUser(int id) {
        if (userRepo.findOne(id) == null) {
            throw new ValidationException("Userul căutat nu există!");
        }
        //nici nu mai e nevoie de o lista separata in memorie

        friendshipRepo.findAll()
                .stream()
                .filter(f -> f.getIdUser1() == id || f.getIdUser2() == id)
                .map(Entity::getId)
                .forEach(friendshipRepo::delete);

        userRepo.delete(id);
    }
}