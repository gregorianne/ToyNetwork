package Repositories.InFile;

import java.io.IOException;

import Domain.*;
import Repositories.InMemoryRepository;
import VE.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Repository care memoreaza datele in memorie si in fisier
 * Template Method: citirea si scrierea in fisier sunt scrise aici, o singură data, iar subclasele doar specifica felul in care se converteste informatia din fisier
 *
 * @param <ID> - type E must have an attribute of type ID
 * @param <Elem>  - type of entities saved in repository
 */
public abstract class AbstractFileRepository<ID, Elem extends Entity<ID>>
        extends InMemoryRepository<ID, Elem> {

    private final String FILENAME;

    /**
     * Initializeaza repo cu validatorul specific, citeste datele din fisier
     *
     * @param validator - validare specifica pentru entitatile intializate in antet cu Elem
     * @param fileName  - path pentru fisier
     */
    public AbstractFileRepository(Validator<Elem> validator, String fileName) {
        super(validator); //! se duce la validator sa vada daca este vreunul de tip Elem
        this.FILENAME = fileName;
        loadData();
    }

    //# FUNCTII DE CONVERSIE IN SI DIN STRING

    /**
     * Creaza o entitate de tip elem dintr un sir de caractere, metoda care poate fi implementata explicit in repository specific
     *
     * @param line - sir de caractere din fisier
     * @return Entitate cu valorile din line
     */
    protected abstract Elem String_to_Entity(String line);

    /**
     * Converteste o entitate intr un sir de caractere care poate fi scris in fisier
     *
     * @param entity - the entity to be converted
     * @return the line of text that describes the entity
     */
    protected abstract String Entity_to_String(Elem entity);


    //# CITIRE / SCRIERE IN FISIER
    /**
     * Citeste toate liniile din fisier si salveaza entitatile valide in memorie
     * Daca fisierul nu exista, repo-ul va incepe aplicatia gol
     *
     * @throws RepositoryException daca fisierul nu poate fi citit / exista linii invalide
     * @throws ValidationException daca intr-una din linii sunt date invalide + specifica linia si continua executia
     * @throws FileNotFoundException daca nu exista fisierul si incepe executia fara date in memorie
     */
    private void loadData() {
        int linie_curenta = 0;
        try (BufferedReader cititor = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = cititor.readLine()) != null) {
                linie_curenta++;
                if (line.isBlank())
                    continue;
                try {
                    super.save(String_to_Entity(line));
                } catch (RepositoryException | ValidationException e) {
                    System.out.println(FILENAME + ": Linia " + linie_curenta + " invalida si ignorata: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fisierul " + FILENAME + " nu exista sau nu avem acces la el; repository gol.");
        } catch (IOException e) {
            //! se respecta impachetarea exceptiilor in Repo Exception
            throw new RepositoryException("Eroare la citirea fisierului " + FILENAME, e);
        }
    }

    /**
     * Rescrie intregul fisier cu ce se afla in memorie
     *
     * @throws RepositoryException daca nu se poate scrie
     */
    private void writeToFile() {
        try (BufferedWriter scriitor = new BufferedWriter(new FileWriter(FILENAME))) {
            for (Elem entity : findAll()) { //! se parcurge fiecare entitate
                scriitor.write(
                        Entity_to_String(entity) //! conversie inapoi in string dupa specificatiile fiecarei entitati
                );
                scriitor.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Eroare la scrierea fisierului " + FILENAME, e);
        }
    }

    //# ACTUALIZAREA DATELOR IN MEMORIE - CRUD
    //# implementeaza ceea ce este deja in InMemoryRepository si salveaza modificarile si in fisier
    /**
     * Adauga in dictionarul intern o entitate valida si o salveaza in fisier
     * @param entity Entitatea nu poate fi nula
     * @return null daca s-a efectuat cu succes
     * @throws RepositoryException daca entitatea e nula
     * @throws VE.ValidationException daca entitatea nu e valida
     */
    @Override
    public Elem save(Elem entity) {
        Elem result = super.save(entity); //# super se duce la parinte - InMemoryRepository
        if (result == null)
            writeToFile();
        return result;
    }

    /**
     * Sterge din memorie o entitate si rescrie fisierul cu noile date din memorie
     * @param id - cheia pentru care stergem valoarea asociata
     * @throws RepositoryException daca id e null
     * @return "the previous value associated with key, or {@code null} if there was no mapping for key."
     */
    @Override
    public Elem delete(ID id) {
        Elem result = super.delete(id);
        if (result != null)
            writeToFile();
        return result;
    }

    /**
     * Actualizeaza valoarea unei entitati cu a alteia primite ca parametru
     * Daca se efectueaza cu succes, rescrie fisierul
     * @param entity entity must not be null
     * @throws RepositoryException daca entitatea e nula
     * @throws VE.ValidationException daca entitatea noua primita nu e valida
     * @return null daca s-a efectuat cu succes sau enitatea din parametru daca nu exista o inregistrare la ID-ul din entitiy
     */
    @Override
    public Elem update(Elem entity) {
        Elem result = super.update(entity);
        if (result == null)
            writeToFile();
        return result;
    }
}
