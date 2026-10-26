package Service;

import Domain.Friendship;
import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
import VE.*;

/**
 * Operatii pentru prietenii indiferent de reprezentare: adăugare și ștergere.
 * Verifică regulile care depind de datele existente (useri existenți, prietenii duplicate),
 * iar forma datelor o verifică validatorul.
 */
public class FriendshipService {
    /** Repository-ul prieteniilor. */
    private final FriendshipFileRepository friendshipRepo;
    /** Repository-ul userilor (aceeași instanță ca în UserService). */
    private final UserFileRepository userRepo;

    /**
     * @param friendshipRepo      repository-ul prieteniilor
     * @param userRepo  repository-ul userilor, folosit pentru a verifica existența lor
     */
    public FriendshipService(FriendshipFileRepository friendshipRepo, UserFileRepository userRepo) {
        this.friendshipRepo = friendshipRepo;
        this.userRepo = userRepo;
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
        if (userRepo.findOne(i1) == null || userRepo.findOne(i2) == null)
            throw new ValidationException("Unul dintre useri nu există");
        Friendship f = new Friendship(i1, i2);
        if (friendshipRepo.save(f) != null)
            throw new ValidationException("Această prietenie există deja");
    }

    /**
     * Șterge prietenia dintre doi useri, indiferent de ordine.
     *
     * @param i1 id-ul primului user
     * @param i2 id-ul celui de-al doilea user
     * @throws ValidationException dacă între cei doi useri nu există prietenie
     */
    public void deleteFriendship(int i1, int i2) {
        if (friendshipRepo.delete(Friendship.normalize(i1, i2)) == null)
            throw new ValidationException("Această prietenie nu există");
    }
}
