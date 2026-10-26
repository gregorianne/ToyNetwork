import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Factory.RepositoryFactory;
import Repositories.InFile.AbstractFileRepository;
import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
import Repositories.RepoInterface;
import Service.FriendshipService;
import Service.NetworkService;
import Service.UserService;
import UI.UI;
import VE.*;

/**
 * Instantierea diverselor module si rularea propriu zisa
 */
public class Main {
    public static void main(String[] args) {
        // un singur repository per tip, partajat de toate service-urile
        try {
            RepositoryFactory factory = new RepositoryFactory();

            RepoInterface<Integer, User> userRepo = factory.createUserRepository(new UserValidator());
            RepoInterface<Tuple<Integer, Integer>, Friendship> friendshipRepo =
                    factory.createFriendshipRepository(new FriendshipValidator());

            UserService userService = new UserService(userRepo, friendshipRepo);
            FriendshipService friendshipService = new FriendshipService(friendshipRepo, userRepo);
            NetworkService networkService = new NetworkService(userRepo, friendshipRepo);

            new UI(userService, friendshipService, networkService).run();

        } catch (RepositoryException e){
            System.out.println("Repo Eroare! " + e);
        } catch (ValidationException e) {
            System.out.println("Valid Eroare! " + e);
        }
    }
}