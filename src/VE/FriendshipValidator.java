package VE;

import Domain.Friendship;

public class FriendshipValidator implements Validator<Friendship>{

    public void validate(Friendship f) {
        if (f.getId_user1() == f.getId_user2())
            throw new ValidationException("Un utilizator nu poate fi prieten cu el însuși");
        if(f.getId_user1()<=1000 || f.getId_user2()<=1000){
            throw new ValidationException("Un id este invalid!");
        }
    }
}