package VE;

/**
 * Excepție aruncată când un repository nu poate citi sau scrie fișierul de date.
 */
public class RepositoryException extends RuntimeException {
    /**
     * @param message descrierea problemei
     */
    public RepositoryException(String message) {
        super(message);
    }

    /**
     * @param message descrierea problemei
     * @param cause   excepția originală (pentru a nu pierde detaliile)
     */
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}