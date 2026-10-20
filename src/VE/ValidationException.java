package VE;

/**
 * Excepție aruncată când forma unei entitati nu este cea corecta
 */
public class ValidationException extends RuntimeException {
    /**
     * @param message descrierea erorii afisate
     */
    public ValidationException(String message){
        super(message);
    }
}


