package UI;

import Domain.User;
import Service.FriendshipService;
import Service.NetworkService;
import Service.UserService;
import VE.ValidationException;

import java.util.List;
import java.util.Scanner;

public class UI {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final NetworkService networkService;
    private final Scanner in = new Scanner(System.in); //pentru citire, sau buffer reader

    public UI(UserService u, FriendshipService f, NetworkService n) {
        this.userService = u;
        this.friendshipService = f;
        this.networkService = n;
    }

    private void printMenu() {
        System.out.println("\n===== NiceBook =====");
        System.out.println("1. Adaugă user");
        System.out.println("2. Șterge user");
        System.out.println("3. Adaugă prietenie");
        System.out.println("4. Șterge prietenie");
        System.out.println("5. Număr comunități");
        System.out.println("6. Cea mai mare comunitate");
        System.out.println("0. Ieșire");
        System.out.print("Alege: ");
    }

    private String readString(String str) {
        System.out.print(str + ": ");
        return in.nextLine().trim();
    }

    // aruncă NumberFormatException  daca nu e nr si prinem in timpul citirii
    private int readInt(String str) {
        return Integer.parseInt(readString(str));
    }

    public void run() {
        while (true) {
            printMenu();
            String option = in.nextLine().trim();

            try {
                switch (option) {
                    case "1" -> addUser();
                    case "2" -> userService.deleteUser(readInt("Id user"));
                    case "3" -> friendshipService.addFriendship(
                            readInt("Id primul user"), readInt("Id al doilea user"));
                    case "4" -> friendshipService.deleteFriendship(
                            readInt("Id primul user"), readInt("Id al doilea user"));
                    case "5" -> System.out.println("Număr comunități: "
                            + networkService.numberOfCommunities());
                    case "6" -> showBiggestCommunity();
                    case "0" -> {
                        System.out.println("La revedere!");
                        return;
                    }
                    default -> System.out.println("Opțiune invalidă.");
                }
                if (option.equals("1") || option.equals("2") || option.equals("3") || option.equals("4"))
                    System.out.println("Operație reușită.");
            } catch (ValidationException e) {
                System.out.println("Eroare: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Eroare: trebuie introdus un număr întreg.");
            } catch (RuntimeException e) {
                System.out.println("Eroare neașteptată: " + e.getMessage());
            }
        }
    }

    private void addUser() {
        int id = readInt("Id");
        String username = readString("Username");
        String email = readString("Email");
        String firstName = readString("Prenume");
        String lastName = readString("Nume");
        userService.addUser(id, username, email, firstName, lastName);
    }

    private void showBiggestCommunity() {
        List<User> community = networkService.mostSociableCommunity();
        System.out.println("Cea mai sociabilă comunitate:");
        for (User u : community) {
            System.out.println(" - " + u.getUsername());
        }
    }
}