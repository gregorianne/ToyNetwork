package Domain;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Reprezintă o relație de prietenie între doi utilizatori ai rețelei.
 * Prietenia este simetrică: (A, B) este aceeași relație cu (B, A).
 *
 * @author Andra
 * @version 1.0
 */
public class Friendship extends Entity<Tuple<Integer, Integer>> {
    //# Inainte de Tuple si Entity cu identificator generic

//*    /** Id-ul primului utilizator din prietenie. */
//    private int id_user1;
//
//    /** Id-ul celui de-al doilea utilizator din prietenie. */
//    private int id_user2;
//
//    /**
//     * Creează o prietenie între doi utilizatori.
//     *
//     * @param id       id-ul unic al prieteniei
//     * @param id_user1 id-ul primului utilizator
//     * @param id_user2 id-ul celui de-al doilea utilizator
//     */
//    public Friendship(int id, int id_user1, int id_user2) {
//        super(id);
//        this.id_user1 = id_user1;
//        this.id_user2 = id_user2;
//    }

    /** Formatul ISO folosit în fișiere, ex: 2026-10-19T10:30 */
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /** Data și ora la care s-a format prietenia. */
    private LocalDateTime dateOfFriendship;

    /**
     * Creează o prietenie între doi utilizatori.
     * Id-ul prieteniei este perechea de id-uri ale userilor, normalizată
     * (id-ul mai mic primul).
     *
     * @param u1               id-ul primului utilizator
     * @param u2               id-ul celui de-al doilea utilizator
     * @param dateOfFriendship data și ora prieteniei
     */
    public Friendship(Integer u1, Integer u2, LocalDateTime dateOfFriendship) {
        setId(normalize(u1, u2));
        this.dateOfFriendship = dateOfFriendship;
    }

    /** Returnează perechea ordonată după id, ca (A,B) și (B,A) să fie aceeași cheie.
     * @param u1 id-ul primului utilizator
     * @param u2 id-ul celui de-al doilea utilizator
     * @return Tuple de u1,u2 ordonati dupa id
     * */
    public static Tuple<Integer, Integer> normalize(Integer u1, Integer u2) {
        return u1 <= u2
                ? new Tuple<>(u1, u2)
                : new Tuple<>(u2, u1);
    }

    /**
     * Creează o prietenie cu data curentă.
     *
     * @param u1 id-ul primului utilizator
     * @param u2 id-ul celui de-al doilea utilizator
     */
    public Friendship(Integer u1, Integer u2) {
        this(u1, u2, LocalDateTime.now());
    }

    /**
     * Returneaza id-ul primului prieten (cel mai mic id din pereche)
     * @return Integer id-ul primului prieten
     */
    public Integer getIdUser1() {
        return getId().getFirst();
    }

    /**
     * Returneaza id-ul celui de-al doilea prieten (cel mai mare id din pereche)
     * @return Integer id-ul celui de-al doilea prieten
     */
    public Integer getIdUser2() {
        return getId().getSecond();
    }

    /**
     * @return data la care s-a realizat prietenia
     */
    public LocalDateTime getDateOfFriendship() {
        return dateOfFriendship;
    }

    /**
     * Schimba sau seteaza data la care s-a efectuat prietenia
     * @param dateOfFriendship noua data
     */
    public void setDateOfFriendship(LocalDateTime dateOfFriendship) {
        this.dateOfFriendship = dateOfFriendship;
    }

    /**
     * @return text de forma user1 ; user2 ; data_prieteniei
     * am eliminat necesitatea unui id separat la citirea din fisier
     */
    @Override
    public String toString() {
        return getIdUser1() + ";" + getIdUser2() + ";"
                + dateOfFriendship.format(FORMAT);
    }

    /** Format pentru afișare în UI, ex: 2 Mar la ora 13:36 */
    public static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("d MMM 'la ora' HH:mm", Locale.ENGLISH);

    /**
     * Returnează data prieteniei într-un format ușor de citit.
     *
     * @return text de forma "2 Mar la ora 13:36"
     */
    public String getFormattedDate() {
        return dateOfFriendship.format(DISPLAY_FORMAT);
    }
}