package Service;
import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
import Repositories.RepoInterface;
import VE.*;

import java.util.ArrayList;
import java.util.List;

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
        int max = 1000;
        for (User u : userRepo.findAll()) {
            if (u.getId() > max) max = u.getId();
        }
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
        for (User existing : userRepo.findAll()) {
            if (existing.getUsername().equals(username))
                throw new ValidationException("Username-ul este deja folosit!");
        }
        User u = new User(username, email, first_name, last_name);
        //# validarea se face direct in in-memory repository
        u.setId(nextId());
        userRepo.save(u);
    }

    public Iterable<User> getAll() {
        return userRepo.findAll();
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
        // întâi le strângem, apoi le ștergem (nu ștergem în timpul iterării)
        List<Tuple<Integer, Integer>> prietenii_de_sters = new ArrayList<>();
        for (Friendship f : friendshipRepo.findAll()) {
            if (f.getIdUser1() == id || f.getIdUser2() == id)
                prietenii_de_sters.add(f.getId()); // PERECHE AICI, nu doar un id
        }
        for (Tuple<Integer, Integer> cheie : prietenii_de_sters) {
            friendshipRepo.delete(cheie);
        }
        userRepo.delete(id);
    }
}