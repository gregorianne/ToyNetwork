import Domain.User;
import Factory.RepositoryFactory;
import Repositories.RepoInterface;
import VE.UserValidator;
import VE.ValidationException;
import VE.Validator;
import Domain.Friendship;
import Domain.Tuple;
import Service.FriendshipService;
import Service.NetworkService;
import Service.UserService;
import UI.UI;
import VE.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static VE.UserCriteria.*;
import static VE.UserCriteria.isPerson;
/**
 * Instantierea diverselor module si rularea propriu zisa
 */
public class Main {

    public static void main(String[] args) {

        // un singur repository per tip, partajat de toate service-urile
        try {
            RepositoryFactory factory = new RepositoryFactory();

            //! joaca de la LAB 4 aici
            //TODO mutat intr-un modul separat
            //showcase de valioator
            Validator<User> u1 = new UserValidator();
            Validator<User> u2 = u -> {
                String email = u.getEmail();
                if (email.endsWith("@")) {
                    throw new ValidationException("Adresa de email se termina cu @! Introdu o adresa valida!");
                }
            };
            Validator<User> u3 = u -> {
                String un = u.getUsername();
                if(un.contains("*") || un.contains("&") || un.contains("#")){
                    throw new ValidationException("Username nu poate contine caractere speciale!");
                }
            };

            Validator<User> compundValidator = u1.andThen(u2).andThen(u3);

            RepoInterface<Integer, User> userRepo = factory.createUserRepository(compundValidator);
            RepoInterface<Tuple<Integer, Integer>, Friendship> friendshipRepo =
                    factory.createFriendshipRepository(new FriendshipValidator());

            UserService userService = new UserService(userRepo, friendshipRepo);
            FriendshipService friendshipService = new FriendshipService(friendshipRepo, userRepo);
            NetworkService networkService = new NetworkService(userRepo, friendshipRepo);

            System.out.println("useri incarcati: " + userRepo.findAll().size());

            //! joaca de la LAB 4 aici - predicate
            //!" se combină cu and, or, negate, Predicate.not. "
            userRepo.findAll(
                    usernameStartsWith("c").and(emailProviderIs("yahoo.com"))
            ).forEach(System.out::println);

            //! Referinta la metoda
            List<User> yahooisti = userRepo.findAll(UserCriteria::emailYahoo);

            //new UI(userService, friendshipService, networkService).run();

        } catch (RepositoryException e){
            System.out.println("Repo Eroare! " + e);
        } catch (ValidationException e) {
            System.out.println("Valid Eroare! " + e);
        }
    }


public static void warm_up() {
    Predicate<String> lung = (s) -> {
        return s.length() > 5; // lungime > 5
    };
    Predicate<String> cuA  = s -> s.startsWith("A");    // incepe cu A

    lung.test("Alexandra");
    lung.test("Ana");

    cuA.test("Ana");
    cuA.test("Bogdan");

    Validator<User> u1 = new UserValidator();
    Validator<User> u2 = u -> {
        String email = u.getEmail();
        if (email.endsWith("@")) {
            throw new ValidationException("Adresa de email se termina cu @! Introdu o adresa valida!");
        }
    };
    Validator<User> u3 = u -> {
        String un = u.getUsername();
        if(un.contains("*") || un.contains("&") || un.contains("#")){
            throw new ValidationException("Username nu poate contine caractere speciale!");
        }
    };

    Validator<User> compundValidator = u1.andThen(u2).andThen(u3);
    User user1 = new User("euSuntUnUserNou**", "email@gmail.com", "user", "nou");
    user1.setId(1001);
    try {
        compundValidator.validate(user1);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    RepositoryFactory factory = new RepositoryFactory();
    RepoInterface<Integer, User> repouser = factory.createUserRepository(compundValidator);
    repouser.findAll(emailProviderIs("gmail")).forEach(System.out::println);

    Supplier<List<String>> reteta = ArrayList::new;

        List<String> a = reteta.get();   // o listă nouă
        List<String> b = reteta.get();   // altă listă nouă
}

}