package VE;
import Domain.User;

import java.util.function.Predicate;

/**
 * Criterii de căutare pentru RepoInterface.findAll(Predicate). Un criteriu nou este o metodă aici,
 *  nu o metodă nouă în Repository: interfața și implementările ei nu se modifică.
 */
public final class UserCriteria {
    public UserCriteria(){}
    public static Predicate<User> usernameStartsWith(String prefix){
        return user -> user.getUsername().startsWith(prefix);
    }

    public static Predicate<User> nameStartsWith(String prefix){
        return user -> user.getFirst_name().startsWith(prefix);
    }

    public static Predicate<User> emailProviderIs(String provider){
        return user -> user.getEmail().endsWith("@" + provider);
    }

    public static Predicate<User> isPerson(String firstName, String lastName){
        return user -> user.getFirst_name().contains(firstName) && user.getLast_name().contains(lastName);
    }
}
