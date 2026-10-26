package Repositories.InFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import VE.RepositoryException;
import Domain.*;
import VE.Validator;

/**
 * Memoreaza prieteniile (după id, intr-un dictionar ordonat) și le sincronizează cu un fișier CSV dupa fiecare operatie CRUD.
 */
public class FriendshipFileRepository extends AbstractFileRepository<Tuple<Integer,Integer>, Friendship>{

    /**
     * Creează repository-ul și încarcă datele din fișier.
     *
     * @param fileName calea fișierului CSV
     * @throws RuntimeException dacă fișierul nu poate fi citit
     */
    public FriendshipFileRepository(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
    }
    // dictionarul e doar in inmemoryrepository!!
    
    //#Avem de implementat doar cele 2 functii abstracte din abstract repo

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
    protected Friendship String_to_Entity(String line) {
        String[] fields = line.split(";", -1);
        if (fields.length != 3)
            throw new RepositoryException("3 campuri asteptate, in schimb gasite: " + fields.length);
        try {
            int id1 = Integer.parseInt(fields[0]);
            int id2 = Integer.parseInt(fields[1]);
            LocalDateTime date = LocalDateTime.parse(fields[2]);
            return new Friendship(id1, id2, date);
        } catch (NumberFormatException e) {
            throw new RepositoryException("id invalid in linia '" + line + "'", e);
        } catch (DateTimeParseException e) {
            throw new RepositoryException("data invalida '" + fields[2] + "'", e);
        }
    }


    /**
     * Implementeaza functia abstracta din AbstractFileRepo
     * Converteste un Friendship intr-un sir de caractare
     * @param f prietenia de convertit la sir text
     * @return linia idUser1;idUser2;date, with the date in ISO format
     */
    @Override
    protected String Entity_to_String(Friendship f) {
        return f.getIdUser1() + ";" + f.getIdUser2() + ";" + f.getDateOfFriendship();
    }




    //#COD VECHI

//
//    private TreeMap<Integer, Friendship> friendships = new TreeMap<>();
//    /**
//     * Adaugă o prietenie și rescrie fișierul
//     * @param fr prietenia de adăugat
//     */
//    public void addFriendship(Friendship fr){
//        friendships.put(fr.getId(), fr);
//        update_to_file();
//    }
//    /**
//     * Șterge prietenia cu id-ul dat și rescrie fișierul.
//     *
//     * @param id id-ul prieteniei de șters
//     */
//    public void deleteFriendship(int id){
//        friendships.remove(id);
//        update_to_file();
//    }
//    /**
//     * Caută o prietenie după id.
//     *
//     * @param id id-ul prieteniei
//     * @return prietenia găsită sau {@code null} dacă nu există
//     */
//    public Friendship findById(int id){
//        return friendships.get(id);
//    }
//    /**
//     * Verifică dacă există deja o prietenie între doi utilizatori.
//     *
//     * @param i1 id-ul primului utilizator
//     * @param i2 id-ul celui de-al doilea utilizator
//     * @return {@code true} dacă prietenia există sau {@code false} altfel
//     */
//    public boolean preexistent(int i1, int i2){
//        for(Friendship f: friendships.values()){
//            if(f.getId_user1()==i1 && f.getId_user2()==i2){
//                return true;
//            }
//        }
//        return false;
//    }
//    /**
//     * Returnează cel mai mare id de prietenie folosit, pentru incrementare automata la ID
//     *
//     * @return ultimul id, sau -1 dacă nu există nicio prietenie
//     */
//    public int LastKey(){
//        if(!friendships.isEmpty()){
//            return friendships.lastKey();
//        }
//        return -1;
//    }
//    /**@return colecția prieteniilor     */
//    public Collection<Friendship> getAll(){
//        return friendships.values();
//    }
//    /**
//     * Șterge toate prieteniile în care apare userul dat și rescrie fișierul o singură dată.
//     *
//     * @param userId id-ul userului ale cărui prietenii se șterg
//     */
//    public void deleteAllOfUser(int userId) {
//        friendships.values().removeIf(f -> f.getId_user1() == userId || f.getId_user2() == userId);
//        update_to_file();
//    }
//    /**
//     * Rescrie tot fișierul CSV cu starea curentă din memorie.
//     * @throws RepositoryException dacă scrierea eșuează
//     */
//    public void update_to_file(){
//        List<String> linii = new ArrayList<>();
//        linii.add(HEADER);
//        for(Friendship f: friendships.values()){
//            linii.add(f.getId() + SEPARATOR + f.getId_user1() + SEPARATOR
//                    + f.getId_user2());
//        }
//        try{
//            Files.write(Path.of(FILENAME),linii);
//        } catch (Exception e) {
//            throw new RepositoryException("FRIENDSHIP_REPO: Fisierul specificat nu a fost gasit la scriere!",e);
//        }
//    }
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
//                int idu1 = Integer.parseInt(p[1]);
//                int idu2 = Integer.parseInt(p[2]);
//
//                friendships.put(id, new Friendship(id, idu1, idu2));
//            }
//        } catch (IOException e) {
//            throw new RepositoryException("FRIENDSHIP_REPO: Fisierul specificat nu a fost gasit la citire!",e);
//        }
//    }

}
