package Repositories;

import Domain.*;
import Service.UserService;
import VE.RepositoryException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

//Tine lista de useri, salevaza in fisier, adauga, modifica
/**
 * Memoreaza utilizatorii (după id, intr-un dictionar ordonat) și sincronizează cu un fișier CSV dupa fiecare operatie CRUD.
 */
public class UserRepository {
    //Lista de utilizatori este un dictionar cu cheia = Id-ul utilizatorului pentru acces ultra rapid
    //HashMap este neordonat dar rapid
    //Schimbat cu TreeMap, care este ordonat
    /** Utilizatorii, intr-un TreeMap dictionar ordonat*/
    private TreeMap<Integer, User> users = new TreeMap<>();

    //final inseamna ca nu se mai poate modifica pe parcurs
    /** Calea fișierului CSV. */
    private final String FILENAME;

    /**
     * Creează repository-ul și încarcă datele din fișier.
     *
     * @param FILENAME calea fișierului CSV
     * @throws RuntimeException dacă fișierul nu poate fi citit
     */
    public UserRepository(String FILENAME) {
        this.FILENAME = FILENAME;
        read_from_file();
    }

    /**
     * Adaugă un utilizator si rescrie fisierul
     * @param usr de tip User - utilizator de adăugat
     */
    public void addUser(User usr){
        users.put(usr.getId(), usr);
        update_to_file();
    }

    /**
     * Șterge utilizatorul cu id-ul dat și rescrie fișierul.
     *
     * @param id id-ul prieteniei de șters
     */
    public void deleteUser(int id){
        users.remove(id);
        update_to_file();
    }

    //note: transmoterea e prin referinta mereu, defaultul la Java
    /**
     * Caută un utilizator după id.
     *
     * @param id id-ul utilizatorului
     * @return utilizatorul găsit sau {@code null} dacă nu există
     */
    public User findById(int id){
        return users.get(id);
    }

    /**
     * Caută un utilizator după username.
     *
     * @param username numele utilizatorului
     * @return utilizatorul găsit sau {@code null} dacă nu există
     */
    public String findByUsername(String username){
        for(User u: users.values()){
            if(u.getUsername().equals(username)){
                return username;
            }
        }
        return null;
    }

    /**folosim values(); nu returnează un TreeMap, ci o colecție cu valorile lui.
     * TreeMap, valorile vin sortate după cheie (id crescător).
     * Dacă era HashMap, ordinea nu era garantată.
     * Tipul Collection nu spune nimic despre ordine, dar obiectul concret din spate o păstrează.
     * @return colecția utilizatorilor*/
    public Collection<User> getAll(){
        return users.values();
    }

    //stergere/scriere in fisier
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static  final String SEPARATOR = ";";
    private static final String HEADER = "id;username;email;first_name;last_name";

    /**
     * Rescrie tot fișierul CSV cu starea curentă din memorie.
     * @throws RepositoryException dacă scrierea eșuează
     */
    public void update_to_file(){
        List<String> linii = new ArrayList<>();
        linii.add(HEADER);
        for(User u: users.values()){
            linii.add(u.getId() + SEPARATOR + u.getUsername() + SEPARATOR
                    + u.getEmail() + SEPARATOR + u.getFirst_name() + SEPARATOR
                    + u.getLast_name());
        }
        try{
            Files.write(Path.of(FILENAME),linii);
        } catch (Exception e) {
            throw new RepositoryException("USER_REPO: Fisierul specificat nu a fost gasit la scriere!",e);
        }
    }

    /**
     * Încarcă prieteniile din fișier, sărind peste header și liniile goale.
     * @throws RepositoryException dacă fișierul nu poate fi citit
     */
    private void read_from_file() {
        try {
            List<String> lines = Files.readAllLines(Path.of(FILENAME));
            for (String line : lines.subList(1, lines.size())) {   // sare header
                if (line.isBlank()) continue;
                String[] p = line.split(SEPARATOR);
                int id = Integer.parseInt(p[0]);
                users.put(id, new User(id, p[1], p[2], p[3], p[4]));
            }
        } catch (IOException e) {
            throw new RepositoryException("USER_REPO: Fisierul specificat nu a fost gasit! la citire!",e);
        }
    }



}
