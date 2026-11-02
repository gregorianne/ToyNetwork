package Repositories;

import Domain.Entity;
import VE.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Repo interfata pentru operatii CRUD
 *
 * @param <ID> - Elementul de tip Elem trebuie sa aiba un atribut de tip ID
 * @param <Elem>  - Tipul de elemente salvate in Repo-ul implementat de aceasta interfata
 */
public interface RepoInterface<ID, Elem extends Entity<ID>> {
//    default List<Elem> findAll(Predicate<Elem> filter){
//        List<Elem> rez = new ArrayList<Elem>();
//        for(Elem e: findAll()){
//            if(filter.test(e)){
//                rez.add(e);
//            }
//        }
//        return rez;
//    }

    /**
     *
     */
    default List<Elem> findAll(Predicate<Elem> myFilter) {
        return findAll().stream()
                .filter(myFilter)
                .collect(Collectors.toList());
    }

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    Elem findOne(ID id);

    /**
     @return all entities
     */
    List<Elem> findAll();

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    Elem save(Elem entity);

    /**
     * removes the entity with the specified id
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    Elem delete(ID id);

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated,
     * otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    Elem update(Elem entity);
}
