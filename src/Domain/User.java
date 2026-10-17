package Domain;

public class User extends Entity{
    private String username;
    private String email;
    private String first_name;
    private String last_name;

    public User(int id, String username, String email,String first_name,String last_name) {
        super(id);
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
    }

    //GETTERS

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    //SETTERS

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
