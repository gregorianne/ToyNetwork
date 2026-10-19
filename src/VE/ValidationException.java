package VE;

public class ValidationException extends RuntimeException {
    /**
     * @param message descrierea erorii afisate
     */
    public ValidationException(String message){
        super(message);
    }
}


