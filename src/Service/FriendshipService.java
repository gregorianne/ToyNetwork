package Service;

import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Repositories.RepoInterface;
import VE.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Friendship> findAll() {
        return friendshipRepo.findAll();
    }

    /**
     * Verifica daca exista sau nu un user care se vrea folosut pt o operatie.
     * @param id numeric al Utilizatorului
     * @return un user
     * @throws ValidationException daca in Optionalul creat se afla o valoare nula
     */
    private User findUserOrThrow(int id) {
        return Optional.ofNullable(userRepo.findOne(id))
                .orElseThrow(() -> new ValidationException("Userul " + id + " nu exista"));
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
        var u1 = findUserOrThrow(i1);
        var u2 = findUserOrThrow(i2);
        Friendship f = new Friendship(u1.getId(),
                                        u2.getId());
        if (friendshipRepo.save(f) != null) //null = succes
            throw new ValidationException("Aceasta prietenie exista deja");
    }

    /**
     * Șterge prietenia dintre doi useri, indiferent de ordine.
     *
     * @param i1 id-ul primului user
     * @param i2 id-ul celui de-al doilea user
     * @throws ValidationException daca între cei doi useri nu exista prietenie
     */
    public void deleteFriendship(int i1, int i2) {
        var u1 = findUserOrThrow(i1);
        var u2 = findUserOrThrow(i2);
        if (friendshipRepo.delete(Friendship.normalize(u1.getId(),
                                                        u2.getId())) == null)
            throw new ValidationException("Aceasta prietenie nu exista");
    }

    /**
     * Returneaza prieteniile într-o forma usor de citit, cu username-uri si data.
     *
     * @return cate un text pe prietenie, de forma "grey07 - booker12 (din 2 Mar la ora 13:36)”
     */
    public List<String> getFriendshipsDisplay() {
        return friendshipRepo.findAll().stream()
                .map(f->{
                    var u1 = findUserOrThrow(f.getIdUser1());
                    var u2 = findUserOrThrow(f.getIdUser2());
                    return u1.getUsername() + " - " + u2.getUsername() + "(din " + f.getFormattedDate() + ")";
                })
                .collect(Collectors.toList());
    }
}
