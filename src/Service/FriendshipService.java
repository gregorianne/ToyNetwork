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
 * Operatii pentru prietenii indiferent de reprezentare: adaugare si stergere.
 * Verifica regulile care depind de datele existente (useri existenti, prietenii duplicate),
 * iar forma datelor o verifica validatorul.
 */
public class FriendshipService {
    /** Repository-ul prieteniilor. */
    private final RepoInterface<Tuple<Integer, Integer>, Friendship> friendshipRepo;
    /** Repository-ul userilor (aceeasi instanta ca în UserService). */
    private final RepoInterface<Integer, User> userRepo;

    /**
     * @param friendshipRepo      repository-ul prieteniilor
     * @param userRepo  repository-ul userilor, folosit pentru a verifica existenta lor
     */
    public FriendshipService(RepoInterface<Tuple<Integer, Integer>, Friendship> friendshipRepo, RepoInterface<Integer, User> userRepo) {
        this.friendshipRepo = friendshipRepo;
        this.userRepo = userRepo;
    }

    /**
     * @return o lista iterabila cu prieteniile
     */
    public Iterable<Friendship> getAll() {
        return friendshipRepo.findAll();
    }

    /**
     * Adauga o prietenie între doi useri existenti.
     * Noua prietenie primeste id-ul (ultimul id folosit + 1).
     * Verifica:
     * corectitudinea formei (validator), existenta ambilor useri (se poate fara sa avem userRepo?)
     * apoi absenta unei prietenii deja existente (în oricare sens, 1-2 sau 2-1).
     *
     * @param i1 id-ul primului user
     * @param i2 id-ul celui de-al doilea user
     * @throws ValidationException daca id-urile sunt egale, daca unul dintre useri
     *                             nu exista sau daca prietenia exista deja
     */
    public void addFriendship(int i1, int i2) {
        if (userRepo.findOne(i1) == null || userRepo.findOne(i2) == null)
            throw new ValidationException("Unul dintre useri nu exista");
        Friendship f = new Friendship(i1, i2);
        if (friendshipRepo.save(f) != null)
            throw new ValidationException("Aceasta prietenie exista deja");
    }

    /**
     * sterge prietenia dintre doi useri, indiferent de ordine.
     *
     * @param i1 id-ul primului user
     * @param i2 id-ul celui de-al doilea user
     * @throws ValidationException daca între cei doi useri nu exista prietenie
     */
    public void deleteFriendship(int i1, int i2) {
        if (friendshipRepo.delete(Friendship.normalize(i1, i2)) == null)
            throw new ValidationException("Aceasta prietenie nu exista");
    }

    /**
     * Returneaza prieteniile într-o forma usor de citit, cu username-uri si data.
     *
     * @return cate un text pe prietenie, de forma "grey07 - booker12 (din 2 Mar la ora 13:36)"
     */
    public List<String> getFriendshipsDisplay() {
        List<String> result = new ArrayList<>();
        for (Friendship f : friendshipRepo.findAll()) {
            User u1 = userRepo.findOne(f.getIdUser1());
            User u2 = userRepo.findOne(f.getIdUser2());
            String name1 = (u1 != null) ? u1.getUsername() : "?" + f.getIdUser1();
            String name2 = (u2 != null) ? u2.getUsername() : "?" + f.getIdUser2();
            result.add(name1 + " - " + name2 + " (din " + f.getFormattedDate() + ")");
        }
        return result;
    }
}
