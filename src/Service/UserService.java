package Service;
import Domain.User;
import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
import VE.*;

import java.util.Collection;

/**
 * operatii pentru useri: adăugare și ștergere.
 * Garantează unicitatea id-ului și a username-ului.
 */
public class UserService {
    /** Repository-ul userilor. */
    private final UserFileRepository repo;
    /** Strategia de validare a unui user. */
    private final Validator<User> validator;
    /** Repository-ul prieteniilor pentru a sterge si prietenii. */
    private final FriendshipFileRepository friendshipRepo;

    /**
     * @param repo      repository-ul userilor
     * @param friendshipRepo repository-ul prieteniilor
     * @param validator strategia de validare a userilor
     */
    public UserService(UserFileRepository repo, FriendshipFileRepository friendshipRepo,
                       Validator<User> validator) {
        this.repo = repo;
        this.friendshipRepo = friendshipRepo;
        this.validator = validator;
    }

    /**
     * Creează și adaugă un user nou.
     * Id-ul și username-ul trebuie să fie unice în rețea, iar datele
     * trebuie să treacă de validator.
     *
     * @param id         id-ul unic al userului
     * @param username   numele de utilizator (unic)
     * @param email      adresa de email (trebuie să conțină '@')
     * @param first_name prenumele (nevid)
     * @param last_name  numele de familie (nevid)
     * @throws ValidationException dacă id-ul sau username-ul există deja sau dacă datele sunt invalide
     */
    public void addUser(int id, String username, String email, String first_name, String last_name){
        User u = new User(id, username, email, first_name, last_name);
        validator.validate(u);
        if(repo.findById(id)!=null){
            throw new ValidationException("Userul deja exista!");
        }
        if (repo.findByUsername(username)!=null)
            throw new ValidationException("Username-ul este deja folosit!");
        repo.addUser(u);
    }

    public Collection<User> getAll(){
        return repo.getAll();
    }

    /**
     * Șterge userul cu id-ul dat, împreună cu toate prieteniile lui.
     *
     * @param id id-ul userului de șters
     * @throws ValidationException dacă nu există niciun user cu acest id
     */
    public void deleteUser(int id) {
        if (repo.findById(id) == null) {
            throw new ValidationException("Userul căutat nu există!");
        }
        friendshipRepo.deleteAllOfUser(id);
        repo.deleteUser(id);
    }
}
