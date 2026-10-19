package Domain;
/**
 * Reprezintă un utilizator al rețelei de socializare.
 */
public class User extends Entity{
    /** Numele de utilizator (unic în rețea). */
    private String username;
    /** Adresa de email. */
    private String email;
    /** Prenumele utilizatorului. */
    private String first_name;
    /** Numele de familie al utilizatorului. */
    private String last_name;

    /**
     * Initializeaza un utilizator la fiecare noua instanta
     *
     * @param id         codul unic al utilizatorului
     * @param username   numele de utilizator
     * @param email      adresa de email
     * @param first_name prenumele
     * @param last_name  numele de familie
     */
    public User(int id, String username, String email,String first_name,String last_name) {
        super(id);
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
    }

    /**
     * Returnează o reprezentare text a utilizatorului.
     *
     * @return text de forma "username at: email Name: prenume nume"
     */
    @Override
    public String toString(){
        return username + " at: " + email + " Name: " + first_name + " " + last_name;
    }

    /** @return numele de utilizator */
    public String getUsername() { return username; }

    /** @return adresa de email */
    public String getEmail() { return email; }

    /** @return prenumele */
    public String getFirst_name() { return first_name; }

    /** @return numele de familie */
    public String getLast_name() { return last_name; }

    /** @param email noua adresă de email */
    public void setEmail(String email) { this.email = email; }

    /** @param username noul nume de utilizator */
    public void setUsername(String username) { this.username = username; }

    /** @param first_name noul prenume */
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    /** @param last_name noul nume de familie */
    public void setLast_name(String last_name) { this.last_name = last_name; }
}
