package VE;

import Domain.Friendship;
/**
 * Validează forma datelor unei Prietenii
 */
public class FriendshipValidator implements Validator<Friendship> {
    /**
     *cei doi useri trebuie să fie diferiți
     * ambele id-uri trebuie să fie mai mari decât 1000.
     * @param f prietenia de validat
     * @throws ValidationException dacă una dintre reguli este încălcată
     */
    @Override
    public void validate(Friendship f) {
        if (f.getId_user1() == f.getId_user2())
            throw new ValidationException("Un utilizator nu poate fi prieten cu el însuși");
        if(f.getId_user1()<=1000 || f.getId_user2()<=1000){
            throw new ValidationException("Un id este invalid!");
        }
    }
}