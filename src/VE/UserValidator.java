package VE;
import Domain.User;
/**
 * Valideaza forma unui User
 * Toate erorile găsite sunt adunate și raportate împreună, astfel încât utilizatorul vede tot ce are de corectat dintr-o singură încercare.
 */
public class UserValidator implements Validator<User> {
    /**
     * id-ul trebuie să fie mai mare decât 1000
     * username-ul nu poate fi null sau gol
     * email-ul nu poate fi null și trebuie să conțină '@'
     * prenumele și numele nu pot fi null sau goale
     * @param u userul de validat
     * @throws ValidationException cu toate mesajele de eroare, separate prin linie nouă, dacă cel puțin o regulă este încălcată
     */
    @Override
    public void validate(User u) {
        StringBuilder errors = new StringBuilder();

        if (u.getId() <= 1000)
            errors.append("Id-ul trebuie să fie mai mare decât 1000\n");
        if (u.getUsername() == null || u.getUsername().isBlank())
            errors.append("Username gol\n");
        if (u.getEmail() == null)
            errors.append("Introdu un email!\n");
        else if (!u.getEmail().contains("@"))
            errors.append("Email invalid. Foloseste o adresa valida --> @\n");
        if (u.getFirst_name() == null || u.getFirst_name().isBlank())
            errors.append("Prenume gol\n");
        if (u.getLast_name() == null || u.getLast_name().isBlank())
            errors.append("Nume gol\n");

        if (errors.length() > 0)
            throw new ValidationException(errors.toString());
    }
}