package VE;

import Domain.Friendship;

import java.util.Objects;

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
        if (Objects.equals(f.getIdUser1(), f.getIdUser2()))
            throw new ValidationException("Un utilizator nu poate fi prieten cu el însuși");
        if(f.getIdUser1()<=1000 || f.getIdUser2()<=1000){
            throw new ValidationException("Un id este invalid!");
        }
    }
}