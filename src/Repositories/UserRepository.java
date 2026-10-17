package Repositories;

import Domain.*;
import java.util.HashMap;
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
    }

    //deja verificat de service
    public void addUser(User usr){
        users.put(usr.getId(), usr);
    }

    //sterge dupa un id dat
    public void deleteUser(int id){
        users.remove(id);
    }

    public User findById(int id){
        return users.get(id);
    }

    //stergere/scriere in fisier
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static  final String SEPARATOR = ";";
    private static final String HEADER = "id;username;email;first_name;last_name";

    public void write(){

    }



}
