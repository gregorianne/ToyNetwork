package Service;

import Domain.Friendship;
import Repositories.*;
import VE.*;

public class FriendshipService {
    private final FriendshipRepository repo;
    private final UserRepository userRepo;
    private final Validator<Friendship> validator;

    public FriendshipService(FriendshipRepository repo, UserRepository userRepo,
                             Validator<Friendship> validator) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.validator = validator;
    }

    public void addFriendship(int i1, int i2) {
        Friendship f = new Friendship(repo.LastKey() + 1, i1, i2);
        validator.validate(f);
        if (userRepo.findById(i1) == null || userRepo.findById(i2) == null)
            throw new ValidationException("Unul dintre useri nu există");
        if (repo.preexistent(i1, i2) || repo.preexistent(i2, i1))
            throw new ValidationException("Această prietenie există deja");
        repo.addFriendship(f);
    }

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
