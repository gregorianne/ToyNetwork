package UI;

import Domain.User;
import Service.FriendshipService;
import Service.NetworkService;
import Service.UserService;
import VE.ValidationException;

import java.util.List;
import java.util.Scanner;

/**
 * Interfața în consolă a aplicației. Afișează un meniu, citește datele
 * de la tastatură și apelează service-urile. Nu conține logică de business:
 * doar prinde excepțiile și afișează mesajele de eroare.
 */
public class UI {
    /** Service-ul pentru useri. */
    private final UserService userService;
    /** Service-ul pentru prietenii. */
    private final FriendshipService friendshipService;
    /** Service-ul pentru analiza rețelei. */
    private final NetworkService networkService;
    /** Citirea de la tastatură. */
    private final Scanner in = new Scanner(System.in);

    /**
     * @param u service-ul userilor
     * @param f service-ul prieteniilor
     * @param n service-ul rețelei
     */
    public UI(UserService u, FriendshipService f, NetworkService n) {
        this.userService = u;
        this.friendshipService = f;
        this.networkService = n;
    }

    /**
     * Meniu
      */
    private void printMenu() {
        System.out.println("\n===== NiceBook =====");
        System.out.println("a. Afiseaza useri");
        System.out.println("1. Adaugă user");
        System.out.println("2. Șterge user");
        System.out.println("3. Adaugă prietenie");
        System.out.println("4. Șterge prietenie");
        System.out.println("5. Număr comunități");
        System.out.println("6. Cea mai mare comunitate");
        System.out.println("0. Ieșire");
        System.out.print("Alege: ");
    }

    /**
     * Afișează un mesaj și citește o linie de la tastatură.
     *
     * @param str eticheta afișată înaintea câmpului
     * @return textul introdus, fără spații la capete
     */
    private String readString(String str) {
        System.out.print(str + ": ");
        return in.nextLine().trim();
    }

    /**
     * Afișează un mesaj și citește un număr întreg.
     *
     * @param str eticheta afișată înaintea câmpului
     * @return numărul introdus
     * @throws NumberFormatException dacă textul introdus nu este un număr întreg
     */
    private int readInt(String str) {
        return Integer.parseInt(readString(str));
    }

    public void showUsers(){
        for(User u: userService.getAll()){
            System.out.println(u.toString());
        }
    }

    /**
     * Rulează meniul în bucla
     * Erorile sunt prinse aici, astfel încât programul nu se oprește
     * După operațiile 1-4 reușite se afișează o confirmare
     */
    public void run() {
        while (true) {
            printMenu();
            String option = in.nextLine().trim();

            try {
                switch (option) {
                    case "a" -> showUsers();
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

    /**
     * Citește câmpurile unui user (id, username, email, prenume, nume)
     * și îl adaugă prin service.
     *
     * @throws ValidationException dacă datele sunt invalide sau userul există deja
     * @throws NumberFormatException dacă id-ul nu este un număr
     */
    private void addUser() {
        int id = readInt("Id");
        String username = readString("Username");
        String email = readString("Email");
        String firstName = readString("Prenume");
        String lastName = readString("Nume");
        userService.addUser(id, username, email, firstName, lastName);
    }

    /** Afișează username-urile membrilor celei mai sociabile comunități. */
    private void showBiggestCommunity() {
        List<User> community = networkService.mostSociableCommunity();
        System.out.println("Cea mai sociabilă comunitate:");
        for (User u : community) {
            System.out.println(" - " + u.getUsername());
        }
    }
}