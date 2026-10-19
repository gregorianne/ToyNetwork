package VE;

/**
 * Strategie de validare pentru entități de tip T(Strategy Pattern).
 * Fiecare entitate (User, Friendship etc.) are propria implementare, iar service-urile folosesc doar această interfață, fără să știe cum se face validarea.
 *
 * @param <T> tipul entității validate
 */
public interface Validator<T> {
    /**
     * @param entity entitatea de validat
     * @throws ValidationException dacă entitatea este invalidă
     */
    void validate(T entity);
}