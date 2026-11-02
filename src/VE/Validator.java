package VE;

/**
 * Strategie de validare pentru entități de tip T(Strategy Pattern).
 * Fiecare entitate (User, Friendship etc.) are propria implementare, iar service-urile folosesc doar această interfață, fără să știe cum se face validarea.
 *
 * @param <T> tipul entității validate
 */
@FunctionalInterface
public interface Validator<T> {
    /**
     * @param entity entitatea de validat
     * @throws ValidationException dacă entitatea este invalidă
     */
    void validate(T entity);

    /**
     * Compune valiatorul instantei actuale cu un altul si le aplica pe acelasi obicet
     * @param other celalalt validator aplicat
     * @return un nou validator care aplica cei doi validatori in ordine
     * @throws NullPointerException daca other este {@code null}
     */
    default Validator<T> andThen(Validator<T> other) {
        return (entity) -> {
            this.validate(entity);
            other.validate(entity);
        };
//        Validator<T> rezultat = entity -> {
//            this.validate(entity);
//             other.validate(entity);
//        };
//        return rezultat;
    }
}