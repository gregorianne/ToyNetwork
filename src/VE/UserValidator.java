package VE;
import Domain.User;

public class UserValidator implements Validator<User> {
    public void validate(User u) {
        StringBuilder errors = new StringBuilder();

        if (u.getId() <= 1000)
            errors.append("Id-ul trebuie să fie mai mare decât 1000\n");
        if (u.getUsername() == null || u.getUsername().isBlank())
            errors.append("Username gol\n");
        if (u.getEmail() == null)
            errors.append("Introdu un email!\n");
        if (!u.getEmail().contains("@"))
            errors.append("Email invalid. Foloseste o adresa valida --> @\n");
        if (u.getFirst_name() == null || u.getFirst_name().isBlank())
            errors.append("Prenume gol\n");
        if (u.getLast_name() == null || u.getLast_name().isBlank())
            errors.append("Nume gol\n");

        if (errors.length() > 0)
            throw new ValidationException(errors.toString());
    }
}