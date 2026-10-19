package Service;

import Domain.Friendship;
import Repositories.*;
import VE.*;

/**
 * Operatii pentru prietenii indiferent de reprezentare: adăugare și ștergere.
 * Verifică regulile care depind de datele existente (useri existenți, prietenii duplicate),
 * iar forma datelor o verifică validatorul.
 */
public class FriendshipService {
    /** Repository-ul prieteniilor. */
    private final FriendshipRepository repo;
    /** Repository-ul userilor (aceeași instanță ca în UserService). */
    private final UserRepository userRepo;
    /** Strategia de validare a unei prietenii. */
    private final Validator<Friendship> validator;

    /**
     * @param repo      repository-ul prieteniilor
     * @param userRepo  repository-ul userilor, folosit pentru a verifica existența lor
     * @param validator strategia de validare a prieteniilor
     */
    public FriendshipService(FriendshipRepository repo, UserRepository userRepo,
                             Validator<Friendship> validator) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.validator = validator;
    }

    /**
     * Adaugă o prietenie între doi useri existenți.
     * Noua prietenie primește id-ul (ultimul id folosit + 1).
     * Verifica:
     * corectitudinea formei (validator), existența ambilor useri (se poate fara sa avem userRepo?)
     * apoi absența unei prietenii deja existente (în oricare sens, 1-2 sau 2-1).
     *
     * @param i1 id-ul primului user
     * @param i2 id-ul celui de-al doilea user
     * @throws ValidationException dacă id-urile sunt egale, dacă unul dintre useri
     *                             nu există sau dacă prietenia există deja
     */
    public void addFriendship(int i1, int i2) {
        Friendship f = new Friendship(repo.LastKey() + 1, i1, i2);
        validator.validate(f);
        if (userRepo.findById(i1) == null || userRepo.findById(i2) == null)
            throw new ValidationException("Unul dintre useri nu există");
        if (repo.preexistent(i1, i2) || repo.preexistent(i2, i1))
            throw new ValidationException("Această prietenie există deja");
        repo.addFriendship(f);
    }

    /**
     * Șterge prietenia dintre doi useri, indiferent de ordine.
     *
     * @param i1 id-ul primului user
     * @param i2 id-ul celui de-al doilea user
     * @throws ValidationException dacă între cei doi useri nu există prietenie
     */
    public void deleteFriendship(int i1, int i2) {
        for (Friendship f : repo.getAll()) {
            boolean match = (f.getId_user1() == i1 && f.getId_user2() == i2) || (f.getId_user1() == i2 && f.getId_user2() == i1);
            if (match) {
                repo.deleteFriendship(f.getId());
                return;
            }
        }
        throw new ValidationException("Această prietenie nu există");
    }
}
