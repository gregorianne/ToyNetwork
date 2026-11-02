package Repositories;

import Domain.Entity;
import Domain.User;
import VE.RepositoryException;
import VE.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.max;

/**
 * Clasa care gestioneaza operatiile crud fara sa se specifice persistenta lor (in memorie, in fisier, in DB)
 * Face operatii cu elemente de tip Entitate cu identificator unic
 * @param <ID> - tip de date pentru identificatorul unic
 * @param <Elem> - tip de date care are un identificator unic de tip ID
 */
public class InMemoryRepository<ID, Elem extends Entity<ID>>
        implements RepoInterface<ID, Elem> {

    /**
     * Validatorul specific pentru Elem
     */
    private final Validator<Elem> validator;
    /**
     * Dictionar care contine inregistrarile memorate in repo
     */
    private final Map<ID, Elem> entities;

    /**
     * Initializeaza un repo cu validatorul specific si dictionarul in care se mentin datele in memorie
     * @param validator - specific tipului de date memorate - polimorfism
     */
    public InMemoryRepository(Validator<Elem> validator) {
        this.validator = validator;
        entities = new HashMap<ID, Elem>();
    }

    /**
     * Cauta un element dupa valoarea id-ului
     * @param id -the id of the entity to be returned
     *           Id nu poate fi null
     * @throws RepositoryException daca id este null
     * @return Elem cu ID specificat sau {@code null} daca nu exista nicio valoare cu cheia specificata id
     */
    @Override
    public Elem findOne(ID id) {
        if (id == null)
            throw new RepositoryException("Id nu poate fi null");
        return entities.get(id);
    }

    /**
     * Returneaza o lista iterabila de elemente ale dictionarului
     * @return valorile entitatilor de la fiecare cheie
     */
    @Override
    public List<Elem> findAll() {
        return (List<Elem>) entities.values();
    }

    /**
     * Adauga in dictionarul intern o entitate valida
     * @param entity Entitatea nu poate fi nula
     * @return null daca s-a efectuat cu succes
     * @throws RepositoryException daca entitatea e nula
     * @throws VE.ValidationException daca entitatea nu e valida
     */
    @Override
    public Elem save(Elem entity) {
        if (entity == null)
            throw new RepositoryException("Entitatea nu poate fi nula!");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            return entity;
        }
        entities.put(entity.getId(), entity);
        return null;
    }

    /**
     * Sterge din memorie o entitate
     * @param id - cheia pentru care stergem valoarea asociata
     * @throws RepositoryException daca id e null
     * @return "the previous value associated with key, or {@code null} if there was no mapping for key."
     */
    @Override
    public Elem delete(ID id) {
        if(id==null){
            throw new RepositoryException("Id nu poat fi null!");
        }
        return entities.remove(id);
    }

    /**
     * Actualizeaza valoarea unei entitati cu a alteia primite ca parametru
     * @param entity entity must not be null
     * @throws RepositoryException daca entitatea e nula
     * @throws VE.ValidationException daca entitatea noua primita nu e valida
     * @return null daca s-a efectuat cu succes sau enitatea din parametru daca nu exista o inregistrare la ID-ul din entitiy
     */
    @Override
    public Elem update(Elem entity) {
        if (entity == null)
            throw new RepositoryException("Entitatea nu poate fi nula!");
        validator.validate(entity);

        //#daca nu contine cheia, returneaza entitatea (nu se poate actualiza o entitate inexistenta)
        if (!entities.containsKey(entity.getId())) {
            return entity;
        }

        //# se actualizeaza cu succes la adresa id
        entities.put(entity.getId(), entity);
        return null;
    }
}
