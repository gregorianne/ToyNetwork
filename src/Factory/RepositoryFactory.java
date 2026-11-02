package Factory;

import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Repositories.*;
import Repositories.InDb.FriendshipDbRepository;
import Repositories.InDb.UserDbRepository;
import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
import VE.RepositoryException;
import VE.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

/**
 * Simple Factory: singura clasa care stie ce tipuri de repo exista
 * Tipul de persistenta si parametrii sunt descrisi in config.properties
 */
public class RepositoryFactory {
    /** Numele fisierului de configuratii */
    private static final String CONFIG_FILE = "config.properties";

    /** Clasa speciala pentru a citi si pastra valorile din fisierul config.properties  */
    private final Properties properties = new Properties();

    /** Persistenta citita din config */
    private final RepositoryType tip_persistenta;

    /**
     * Executa load pt config si stabileste tipul de persitenta
     *
     * @throws RepositoryException daca fisierul lipseste sau nu se poate citi
     */
    public RepositoryFactory() {
        try (InputStream in = RepositoryFactory.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null)
                throw new RepositoryException("Fisierul de configurare " + CONFIG_FILE + " nu a fost gasit (verifica path)");
            properties.load(in);
        } catch (IOException e) {
            throw new RepositoryException("Eroare la citirea fisierului " + CONFIG_FILE, e);
        }
        this.tip_persistenta = readType();
    }

    /**
     * Citeste si verifica ce tip de repo construieste
     *
     * @return tipul de persistenta
     * @throws RepositoryException daca valoarea lipseste sau nu e una din: MEMORY, FILE, DATABASE
     */
    private RepositoryType readType() {
        String value = getRequiredProperty("repository.type");
        try {
            return RepositoryType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("repository.type necunoscut: '" + value + "' (valori permise: MEMORY, FILE, DATABASE)", e);
        }
    }

    /**
     * Returneaza o valoarea a unei proprietati din config
     *
     * @param property_name numele proprietatii
     * @return valoarea nenula
     * @throws RepositoryException daca proprietatea lipseste sau e goala
     */
    private String getRequiredProperty(String property_name) {
        String value = properties.getProperty(property_name);
        if (value == null || value.isBlank())
            throw new RepositoryException("Lipseste proprietatea '" + property_name + "' din " + CONFIG_FILE);
        return value.trim(); //fara spatii goale
    }

    final String url = getRequiredProperty("db.url");
    final String user = getRequiredProperty("db.user");
    final String password = getRequiredProperty("db.password");
    final String filename = getRequiredProperty("repository.users.file");

    /**
     * Creeaza un nou repo pt utilizatori, dupa tipul specificat.
     *
     * @param validator validarea pt utilizatori
     * @return repo pt utilizatori
     * @throws RepositoryException daca nu exista tipul specificat
     */
    public RepoInterface<Integer, User> createUserRepository(Validator<User> validator) {
        Map<RepositoryType, //#CHEIE
                Function< //#VALOARE -- o reteta care primeste un validator de useri și intoarce un repository de useri
                        Validator<User>, //#INPUT
                        RepoInterface<Integer, User>>> //#OUPUT
                fabrica_map = new HashMap<>(); //#TIP de dictionar

        fabrica_map.put(RepositoryType.MEMORY, InMemoryRepository::new); //!v -> new InMemoryRepository<>(v)

        fabrica_map.put(RepositoryType.FILE, v -> new UserFileRepository //! {return new [...]} Java stie ca Function are doar apply,
                                                                                        //! deci tot ce e dupa sageata e corpul ei
                (v,
                filename)
        );

        //! Function <Validator, Repo>
        //! fabrica_map adauga o pereche
        //# (KEY: FILE
        //? VALUE (fara lambda ar fi scris ca):
        //        fabrica.put(RepositoryType.FILE, new Function<Validator<User>, RepoInterface<Integer, User>>() { --> CLASA ANONIMA
        //            @Override
        //            public RepoInterface<Integer, User> apply(Validator<User> v) { !---> SE APLICA MEREU APPLY dar cu lambda implicit
        //                return new UserFileRepository(v, filename);
        //            }
        //        });

        fabrica_map.put(RepositoryType.DATABASE, v -> new UserDbRepository
                (v,
                url,
                user,
                password)
        );

        //Validator user INPUT -> RepoInterface<Integer, User> OUTPUT
        Function<Validator<User>, RepoInterface<Integer, User>> reteta = fabrica_map.get(tip_persistenta);

        if (reteta == null)
            throw new RepositoryException("Tip de persistenta neimplementat: " + tip_persistenta);

        //acuma se aplica reteta cu validatorul transmis
        return reteta.apply(validator);

//        return switch (tip_persistenta) {
//            case MEMORY -> new InMemoryRepository<Integer, User>(validator);
//            case FILE -> new UserFileRepository(validator, getRequiredProperty("repository.users.file"));
//            case DATABASE -> new UserDbRepository(validator, getRequiredProperty("db.url"),
//                    getRequiredProperty("db.user"), getRequiredProperty("db.password"));
//            default -> throw new RepositoryException("Tip de persistenta neimplementat: " + tip_persistenta);
//        };
    }

    /**
     * Creeaza un nou repo pt prietenii, dupa tipul specificat
     *
     * @param validator validarea pt prietenii
     * @return repo pt prietenii
     * @throws RepositoryException daca nu exista tipul specificat
     */
    public RepoInterface<Tuple<Integer, Integer>, Friendship> createFriendshipRepository(Validator<Friendship> validator) {
        Map<RepositoryType,
                Function<
                        Validator<Friendship>, //#T
                        RepoInterface<Tuple<Integer,Integer>,Friendship>
                        >> fabrica_map = new HashMap<>(); //#R
        fabrica_map.put(RepositoryType.MEMORY, InMemoryRepository::new);
        fabrica_map.put(RepositoryType.FILE, v -> new FriendshipFileRepository(v, filename));
        fabrica_map.put(RepositoryType.DATABASE, v-> new FriendshipDbRepository(v, url, user, password));

        Function<Validator<Friendship>, RepoInterface<Tuple<Integer, Integer>, Friendship>> reteta = fabrica_map.get(tip_persistenta);
        if(reteta == null)throw new RepositoryException("Tip de persistenta neimplementat: " + tip_persistenta);
        return reteta.apply(validator);

        //        return switch (tip_persistenta) {
        //            case MEMORY -> new InMemoryRepository<Tuple<Integer, Integer>, Friendship>(validator);
        //            case FILE -> new FriendshipFileRepository(validator,
        //                    getRequiredProperty("repository.friendships.file"));
        //            case DATABASE -> new FriendshipDbRepository(validator, getRequiredProperty("db.url"),
        //                    getRequiredProperty("db.user"), getRequiredProperty("db.password"));
        //            default -> throw new RepositoryException("Tip de persistenta neimplementat: " + tip_persistenta);
        //        };
            }
}