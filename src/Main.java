import Domain.User;
import Repositories.InFile.AbstractFileRepository;
import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
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
            UserFileRepository userRepo = new UserFileRepository(new UserValidator(),"src/users.csv");
            FriendshipFileRepository friendRepo = new FriendshipFileRepository(new FriendshipValidator(),"src/friendships.csv");

            UserService userService = new UserService(userRepo, friendRepo);
            FriendshipService friendshipService = new FriendshipService(friendRepo, userRepo);
            NetworkService networkService = new NetworkService(userRepo, friendRepo);

            new UI(userService, friendshipService, networkService).run();

        } catch (RepositoryException | ValidationException | NumberFormatException e) {
            System.out.println(e);
        }
    }
}