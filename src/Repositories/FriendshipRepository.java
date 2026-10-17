package Repositories;

import Domain.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Domain.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class FriendshipRepository {

    //Lista de utilizatori este un dictionar cu cheia = Id-ul utilizatorului pentru acces ultra rapid
    //HashMap este neordonat dar rapid
    private Map<Integer, Friendship> friendships = new HashMap<>();

    //final inseamna ca nu se mai poate modifica pe parcurs
    private final String FILENAME;

    public FriendshipRepository(String FILENAME) {
        this.FILENAME = FILENAME;
        read_from_file();
    }

    //deja verificat de service
    public void addFriendship(Friendship fr){
        friendships.put(fr.getId(), fr);
        update_to_file();
    }

    //sterge dupa un id dat
    public void deleteFriendship(int id){
        friendships.remove(id);
        update_to_file();
    }

    public Friendship findById(int id){
        return friendships.get(id);
    }

    //stergere/scriere in fisier
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static  final String SEPARATOR = ";";
    private static final String HEADER = "id;username;email;first_name;last_name";

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
            throw new RuntimeException(e);
        }
    }

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
            throw new RuntimeException(e);
        }
    }



}
