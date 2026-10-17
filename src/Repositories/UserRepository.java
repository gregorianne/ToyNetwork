package Repositories;

import Domain.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Tine lista de useri, salevaza in fisier, adauga, modifica
public class UserRepository {
    //Lista de utilizatori este un dictionar cu cheia = Id-ul utilizatorului pentru acces ultra rapid
    //HashMap este neordonat dar rapid
    private Map<Integer, User> users = new HashMap<>();

    //final inseamna ca nu se mai poate modifica pe parcurs
    private final String FILENAME;

    public UserRepository(String FILENAME) {
        this.FILENAME = FILENAME;
        read_from_file();
    }

    //deja verificat de service
    public void addUser(User usr){
        users.put(usr.getId(), usr);
        update_to_file();
    }

    //sterge dupa un id dat
    public void deleteUser(int id){
        users.remove(id);
        update_to_file();
    }

    public User findById(int id){
        return users.get(id);
    }

    //stergere/scriere in fisier
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static  final String SEPARATOR = ";";
    private static final String HEADER = "id;username;email;first_name;last_name";

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
                users.put(id, new User(id, p[1], p[2], p[3], p[4]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
