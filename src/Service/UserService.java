package Service;
import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Repositories.InFile.AbstractFileRepository;
import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
import VE.*;

import java.util.ArrayList;
import java.util.List;

/**
 * operatii pentru useri: adăugare și ștergere.
 * Garantează unicitatea id-ului și a username-ului.
 */
public class UserService {
    /** Repository-ul userilor. */
    private final UserFileRepository user_repo;
    /** Repository-ul prieteniilor pentru a sterge si prietenii. */
    private final FriendshipFileRepository friendshipRepo;

    /**
     * @param user_repo repository-ul userilor
     * @param friendshipRepo repository-ul prieteniilor
     */
    public UserService(UserFileRepository user_repo,
                       FriendshipFileRepository friendshipRepo) {
        this.user_repo = user_repo;
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
        for (User u : user_repo.get_All()) {
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
        for (User existing : user_repo.get_All()) {
            if (existing.getUsername().equals(username))
                throw new ValidationException("Username-ul este deja folosit!");
        }
        User u = new User(username, email, first_name, last_name);
        //# validarea se face direct in in-memory repository
        u.setId(nextId());
        user_repo.save(u);
    }

    public Iterable<User> getAll() {
        return user_repo.get_All();
    }

    /**
     * Șterge userul cu id-ul dat, împreună cu toate prieteniile lui.
     *
     * @param id id-ul userului de șters
     * @throws ValidationException dacă nu există niciun user cu acest id
     */
    public void deleteUser(int id) {
        if (user_repo.findOne(id) == null) {
            throw new ValidationException("Userul căutat nu există!");
        }
        // întâi le strângem, apoi le ștergem (nu ștergem în timpul iterării)
        List<Tuple<Integer, Integer>> prietenii_de_sters = new ArrayList<>();
        for (Friendship f : friendshipRepo.get_All()) {
            if (f.getIdUser1() == id || f.getIdUser2() == id)
                prietenii_de_sters.add(f.getId()); // PERECHE AICI, nu doar un id
        }
        for (Tuple<Integer, Integer> cheie : prietenii_de_sters) {
            friendshipRepo.delete(cheie);
        }
        user_repo.delete(id);
    }
}