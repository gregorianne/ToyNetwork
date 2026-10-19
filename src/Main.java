import Repositories.FriendshipRepository;
import Repositories.UserRepository;
import Service.FriendshipService;
import Service.NetworkService;
import Service.UserService;
import UI.UI;
import VE.FriendshipValidator;
import VE.UserValidator;

public class Main {
    public static void main(String[] args) {
        // un singur repository per tip, partajat de toate service-urile
        UserRepository userRepo = new UserRepository("src/users.csv");
        FriendshipRepository friendRepo = new FriendshipRepository("src/friendships.csv");

        UserService userService = new UserService(userRepo, new UserValidator());
        FriendshipService friendshipService = new FriendshipService(friendRepo, userRepo, new FriendshipValidator());
        NetworkService networkService = new NetworkService(userRepo, friendRepo);

        new UI(userService, friendshipService, networkService).run();
    }
}