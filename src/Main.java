
import Domain.*;
import Repositories.*;

public class Main {
    public static void main() throws Exception {
        UserRepository repo = new UserRepository("src/users.csv");
        System.out.println(repo.findById(1001).getUsername());
        FriendshipRepository frepo = new FriendshipRepository("src/friendships.csv");
        System.out.println(frepo.findById(1).getId_user1());

    }
}