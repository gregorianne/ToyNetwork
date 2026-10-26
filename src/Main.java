import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
import Service.FriendshipService;
import Service.NetworkService;
import Service.UserService;
import UI.UI;
import VE.FriendshipValidator;
import VE.RepositoryException;
import VE.UserValidator;
import VE.ValidationException;

/**
 * Instantierea diverselor module si rularea propriu zisa
 */
public class Main {
    public static void main(String[] args) {
        // un singur repository per tip, partajat de toate service-urile
        try {
            UserFileRepository userRepo = new UserFileRepository("src/users.csv");
            FriendshipFileRepository friendRepo = new FriendshipFileRepository("src/friendships.csv");

            UserService userService = new UserService(userRepo, friendRepo, new UserValidator());
            FriendshipService friendshipService = new FriendshipService(friendRepo, userRepo, new FriendshipValidator());
            NetworkService networkService = new NetworkService(userRepo, friendRepo);

            new UI(userService, friendshipService, networkService).run();

        } catch (RepositoryException | ValidationException | NumberFormatException e) {
            System.out.println(e);
        }
    }
}