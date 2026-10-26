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
import java.util.Properties;

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

    /**
     * Creeaza un nou repo pt utilizatori, dupa tipul specificat
     *
     * @param validator validarea pt utilizatori
     * @return repo pt utilizatori
     * @throws RepositoryException daca nu exista tipul specificat
     */
    public RepoInterface<Integer, User> createUserRepository(Validator<User> validator) {
        switch (tip_persistenta) {
            case MEMORY:
                return new InMemoryRepository<Integer, User>(validator);
            case FILE:
                return new UserFileRepository(validator, getRequiredProperty("repository.users.file"));
            case DATABASE:
                return new UserDbRepository(validator, getRequiredProperty("db.url"),
                        getRequiredProperty("db.user"), getRequiredProperty("db.password"));
            default:
                throw new RepositoryException("Tip de persistenta neimplementat: " + tip_persistenta);
        }
    }

    /**
     * Creeaza un nou repo pt prietenii, dupa tipul specificat
     *
     * @param validator validarea pt prietenii
     * @return repo pt prietenii
     * @throws RepositoryException daca nu exista tipul specificat
     */
    public RepoInterface<Tuple<Integer, Integer>, Friendship> createFriendshipRepository(
            Validator<Friendship> validator) {
        switch (tip_persistenta) {
            case MEMORY:
                return new InMemoryRepository<Tuple<Integer, Integer>, Friendship>(validator);
            case FILE:
                return new FriendshipFileRepository(validator,
                        getRequiredProperty("repository.friendships.file"));
            case DATABASE:
                return new FriendshipDbRepository(validator, getRequiredProperty("db.url"),
                        getRequiredProperty("db.user"), getRequiredProperty("db.password"));
            default:
                throw new RepositoryException("Tip de persistenta neimplementat: " + tip_persistenta);
        }
    }
}