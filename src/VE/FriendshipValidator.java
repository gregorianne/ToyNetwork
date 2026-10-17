package VE;

import Domain.Friendship;

public class FriendshipValidator {

    public void validate(Friendship f) {
        if (f.getId_user1() == f.getId_user2())
            throw new ValidationException("Un utilizator nu poate fi prieten cu el însuși");
    }
}