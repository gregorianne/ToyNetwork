package Repositories;

import Domain.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import VE.RepositoryException;
import Domain.*;

/**
 * Memoreaza prieteniile (după id, intr-un dictionar ordonat) și le sincronizează cu un fișier CSV dupa fiecare operatie CRUD.
 */
public class FriendshipRepository {

    //Lista de utilizatori este un dictionar cu cheia = Id-ul utilizatorului pentru acces ultra rapid
    //HashMap este neordonat dar rapid
    /** Prieteniile, ordonate după id. */
    private TreeMap<Integer, Friendship> friendships = new TreeMap<>();

    //final inseamna ca nu se mai poate modifica pe parcurs
    /** Calea fișierului CSV. */
    private final String FILENAME;

    /**
     * Creează repository-ul și încarcă datele din fișier.
     *
     * @param FILENAME calea fișierului CSV
     * @throws RuntimeException dacă fișierul nu poate fi citit
     */
    public FriendshipRepository(String FILENAME) {
        this.FILENAME = FILENAME;
        read_from_file();
    }

    //deja verificat de service
    /**
     * Adaugă o prietenie și rescrie fișierul
     * @param fr prietenia de adăugat
     */
    public void addFriendship(Friendship fr){
        friendships.put(fr.getId(), fr);
        update_to_file();
    }

    /**
     * Șterge prietenia cu id-ul dat și rescrie fișierul.
     *
     * @param id id-ul prieteniei de șters
     */
    public void deleteFriendship(int id){
        friendships.remove(id);
        update_to_file();
    }

    /**
     * Caută o prietenie după id.
     *
     * @param id id-ul prieteniei
     * @return prietenia găsită sau {@code null} dacă nu există
     */
    public Friendship findById(int id){
        return friendships.get(id);
    }

    /**
     * Verifică dacă există deja o prietenie între doi utilizatori.
     *
     * @param i1 id-ul primului utilizator
     * @param i2 id-ul celui de-al doilea utilizator
     * @return {@code true} dacă prietenia există sau {@code false} altfel
     */
    public boolean preexistent(int i1, int i2){
        for(Friendship f: friendships.values()){
            if(f.getId_user1()==i1 && f.getId_user2()==i2){
                return true;
            }
        }
        return false;
    }

    /**
     * Returnează cel mai mare id de prietenie folosit, pentru incrementare automata la ID
     *
     * @return ultimul id, sau -1 dacă nu există nicio prietenie
     */
    public int LastKey(){
        if(!friendships.isEmpty()){
            return friendships.lastKey();
        }
        return -1;
    }

    /**@return colecția prieteniilor     */
    public Collection<Friendship> getAll(){
        return friendships.values();
    }

    /**
     * Șterge toate prieteniile în care apare userul dat și rescrie fișierul o singură dată.
     *
     * @param userId id-ul userului ale cărui prietenii se șterg
     */
    public void deleteAllOfUser(int userId) {
        friendships.values().removeIf(f -> f.getId_user1() == userId || f.getId_user2() == userId);
        update_to_file();
    }

    //stergere/scriere in fisier
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static  final String SEPARATOR = ";";
    private static final String HEADER = "id;id_ser1;id_user2";

    /**
     * Rescrie tot fișierul CSV cu starea curentă din memorie.
     * @throws RepositoryException dacă scrierea eșuează
     */
    public void update_to_file(){
        List<String> linii = new ArrayList<>();
        linii.add(HEADER);
        for(Friendship f: friendships.values()){
            linii.add(f.getId() + SEPARATOR + f.getId_user1() + SEPARATOR
                    + f.getId_user2());
        }
        try{
            Files.write(Path.of(FILENAME),linii);
        } catch (Exception e) {
            throw new RepositoryException("FRIENDSHIP_REPO: Fisierul specificat nu a fost gasit la scriere!",e);
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
                int idu1 = Integer.parseInt(p[1]);
                int idu2 = Integer.parseInt(p[2]);

                friendships.put(id, new Friendship(id, idu1, idu2));
            }
        } catch (IOException e) {
            throw new RepositoryException("FRIENDSHIP_REPO: Fisierul specificat nu a fost gasit la citire!",e);
        }
    }



}
