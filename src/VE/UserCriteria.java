package VE;
import Domain.User;

import java.util.function.Predicate;

/**
 * Criterii de căutare pentru RepoInterface.findAll(Predicate). Un criteriu nou este o metodă aici,
 *  nu o metodă nouă în Repository: interfața și implementările ei nu se modifică.
 */
public final class UserCriteria {
    public UserCriteria(){}

    /**
     * Filtrare dupa inceputul numelui de handle
     * @param prefix inceputul numelui
     * @return predicat care filtreaza dupa prefix
     */
    public static Predicate<User> usernameStartsWith(String prefix){
        return user -> user.getUsername().startsWith(prefix);
    }

    /**
     * Filtrare dupa inceputul prenumelui
     * @param prefix inceputul prenumelui
     * @return predicat care filtreaza dupa prefix
     */
    public static Predicate<User> nameStartsWith(String prefix){
        return user -> user.getFirst_name().startsWith(prefix);
    }

    /**
     * Filtrare dupa tipul de email + .com (sa nu fie doar ceva@)
     * @param provider inceputul numelui
     * @return predicat care filtreaza dupa provider
     */
    public static Predicate<User> emailProviderIs(String provider){
        return user -> user.getEmail().endsWith("@" + provider + ".com");
    }

    /**
     * Cauta o persoana dupa litere din nume si prenume
     * @param firstName prenume
     * @param lastName nume de familie (ne plac englejii cum le invart)
     * @return predicat care filtreaza numele complet dupa litere continute in input
     */
    public static Predicate<User> isPerson(String firstName, String lastName){
        return user -> user.getFirst_name().contains(firstName) && user.getLast_name().contains(lastName);
    }

    /**
     * filtreaza pe un user care (inca) foloseste yahoo
     * @param user la care se verifica mail
     * @return adv/fals daca are sau nu yahoo la mail
     */
    public static boolean emailYahoo(User user){
        return user.getEmail().endsWith("@yahoo.com");
    }

}

