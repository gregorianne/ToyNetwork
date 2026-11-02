package Repositories.InFile;

import Domain.*;
import VE.RepositoryException;
import VE.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Implementeaza doar specificatiile citirii si scrierii de utilizatori din fisier
 */
public class UserFileRepository extends AbstractFileRepository<Integer, User>{

    /**
     * Creaza Repo pt Utilizatori specific
     *
     * @param validator validare pt utilizatori
     * @param fileName  path pt fisier
     */
    public UserFileRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    /**
     * Implementeaza functia abstracta din AbstractFileRepo
     * Converteste un sir de caractare intr-un User
     *
     * @param line de format id;username;email;first_name;last_name
     * @return the user described by the line
     * @throws RepositoryException   if the line does not have exactly 5 fields
     * @throws NumberFormatException if the id is not a number
     */
    @Override
    protected User stringToEntity(String line) {
        String[] fields = line.split(";", -1);
        if (fields.length != 5)
            throw new RepositoryException("5 campuri asteptate, in schimb gasite: " + fields.length);
        try {
            User u = new User(fields[1], fields[2], fields[3], fields[4]);
            //id setat separat
            u.setId(Integer.parseInt(fields[0]));
            return u;
        } catch (NumberFormatException e) {
            throw new RepositoryException("id invalid '" + fields[0] + "'", e);   // exception chaining
        }
    }

    /**
     * Implementeaza functia abstracta din AbstractFileRepo
     * Converteste un User intr-un sir de caractare
     * @param u the user to be converted
     * @return the line id;username;email;first_name;last_name
     */
    @Override
    protected String entityToString(User u) {
        return u.getId() + ";" + u.getUsername() + ";" + u.getEmail() + ";"
                + u.getFirst_name() + ";" + u.getLast_name();
    }

    //#COD VECHI

    //    Lista de utilizatori este un dictionar cu cheia = Id-ul utilizatorului pentru acces ultra rapid
//    HashMap este neordonat dar rapid
//    Schimbat cu TreeMap, care este ordonat
//    /** Utilizatorii, intr-un TreeMap dictionar ordonat*/
//    private TreeMap<Integer, User> users = new TreeMap<>();
//
//    final inseamna ca nu se mai poate modifica pe parcurs
//    /** Calea fișierului CSV. */
//    private final String FILENAME;
//
//    /**
//     * Creează repository-ul și încarcă datele din fișier.
//     *
//     * @param FILENAME calea fișierului CSV
//     * @throws RuntimeException dacă fișierul nu poate fi citit
//     */
//    public UserFileRepository(String FILENAME) {
//        this.FILENAME = FILENAME;
//        read_from_file();
//    }
//
//    /**
//     * Adaugă un utilizator si rescrie fisierul
//     * @param usr de tip User - utilizator de adăugat
//     */
//    public void addUser(User usr){
//        users.put(usr.getId(), usr);
//        update_to_file();
//    }
//
//    /**
//     * Șterge utilizatorul cu id-ul dat și rescrie fișierul.
//     *
//     * @param id id-ul prieteniei de șters
//     */
//    public void deleteUser(int id){
//        users.remove(id);
//        update_to_file();
//    }
//
//    //note: transmoterea e prin referinta mereu, defaultul la Java
//    /**
//     * Caută un utilizator după id.
//     *
//     * @param id id-ul utilizatorului
//     * @return utilizatorul găsit sau {@code null} dacă nu există
//     */
//    public User findById(int id){
//        return users.get(id);
//    }
//
//    /**
//     * Caută un utilizator după username.
//     *
//     * @param username numele utilizatorului
//     * @return utilizatorul găsit sau {@code null} dacă nu există
//     */
//    public String findByUsername(String username){
//        for(User u: users.values()){
//            if(u.getUsername().equals(username)){
//                return username;
//            }
//        }
//        return null;
//    }
//
//    /**folosim values(); nu returnează un TreeMap, ci o colecție cu valorile lui.
//     * TreeMap, valorile vin sortate după cheie (id crescător).
//     * Dacă era HashMap, ordinea nu era garantată.
//     * Tipul Collection nu spune nimic despre ordine, dar obiectul concret din spate o păstrează.
//     * @return colecția utilizatorilor*/
//    public Collection<User> findAll(){
//        return users.values();
//    }
//
//    //stergere/scriere in fisier
//    private static final String NEW_LINE_SEPARATOR = "\n";
//    private static  final String SEPARATOR = ";";
//    private static final String HEADER = "id;username;email;first_name;last_name";
//
//    /**
//     * Rescrie tot fișierul CSV cu starea curentă din memorie.
//     * @throws RepositoryException dacă scrierea eșuează
//     */
//    public void update_to_file(){
//        List<String> linii = new ArrayList<>();
//        linii.add(HEADER);
//        for(User u: users.values()){
//            linii.add(u.getId() + SEPARATOR + u.getUsername() + SEPARATOR
//                    + u.getEmail() + SEPARATOR + u.getFirst_name() + SEPARATOR
//                    + u.getLast_name());
//        }
//        try{
//            Files.write(Path.of(FILENAME),linii);
//        } catch (Exception e) {
//            throw new RepositoryException("USER_REPO: Fisierul specificat nu a fost gasit la scriere!",e);
//        }
//    }
//
//    /**
//     * Încarcă prieteniile din fișier, sărind peste header și liniile goale.
//     * @throws RepositoryException dacă fișierul nu poate fi citit
//     */
//    private void read_from_file() {
//        try {
//            List<String> lines = Files.readAllLines(Path.of(FILENAME));
//            for (String line : lines.subList(1, lines.size())) {   // sare header
//                if (line.isBlank()) continue;
//                String[] p = line.split(SEPARATOR);
//                int id = Integer.parseInt(p[0]);
//                users.put(id, new User(id, p[1], p[2], p[3], p[4]));
//            }
//        } catch (IOException e) {
//            throw new RepositoryException("USER_REPO: Fisierul specificat nu a fost gasit! la citire!",e);
//        }
//    }



}
