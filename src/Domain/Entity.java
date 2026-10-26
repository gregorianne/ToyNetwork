package Domain;
/*
 * MARKER INTERFACE
 * A Marker Interface in Java is an interface that contains no methods or fields.
 * It is used to mark a class so that the Java runtime or compiler can identify some special behavior or capability of that class.
?Q: Cum de are ceva special daca nu are metode??
 * SERIALIZABLE
 *
 * It is used to indicate that the objects of a class can be converted into a byte stream (serialization)
 * and later reconstructed back into objects (deserialization). Since it is a marker interface,
 * it does not contain any methods or fields and is mainly used for object persistence and data transfer.
 *
! explicatie reddit
 *  When you implement the serializable interface, you're telling java that when you want to do something with the data like send it over the network,
 *  java must collect only the relevant data and send it one byte after another.
 *  Serializable will look at each pointer, figure out what it's pointing to,
 *  extract the bits you're interested in and make it available as one series of bytes.
*/

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clasă de bază pentru toate entitățile aplicației (User, Friendship etc.).
 * Gestioneaza identificatorul unic, care le este comun
 * @param <ID> - tip de identificator - poate fi integer, long, string... etc
 */

//! Serilizable ca sa fie in locatii succesive de memorie
public class Entity<ID> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7331115341259248461L;

    protected ID id;

//    /**
//     * Creează o entitate cu id-ul dat.
//     *
//     * @param id codul unic al entitatii
//     */
//    public Entity(ID id) {
//        this.id = id;
//    }

    /**
     * Constructor gol pentru a seta mai tarziu id-ul
     */
    public Entity() {}

    /**
     * Constructor care primeste un id
     * @param id identificatorul unic al instantei
     */
    public Entity(ID id) {
        this.id = id;
    }

    /**
     * Returnează id-ul entitatii.
     *
     * @return codul unic al entității
     */
    public ID getId() {
        return id;
    }

    /**
     * Setează id-ul entitatii
     *
     * @param id noul cod unic
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * two entities are equal if they have the same id
     *
     * @param o - the object to compare with
     * @return true - if o is an Entity with the same id,
     * false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false; //?
        return Objects.equals(id, entity.id);
    }

//! doua obiecte sunt egale daca au acelasi hash. reciproca nu e adevarata
    /**
     * @return the hash code computed from the id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     *
     * @return the string representation of the entity
     */
    @Override
    public String toString() {
        return "Entity{id=" + id + '}';
    }
}