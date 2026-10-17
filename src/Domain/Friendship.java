package Domain;

public class Friendship extends Entity{
    int id_user1, id_user2;
    public Friendship(int id, int id_user1, int id_user2) {
        super(id);
        this.id_user1 = id_user1;
        this.id_user2 = id_user2;
    }

    //GETTERS

    public int getId_user1() {
        return id_user1;
    }

    public int getId_user2() {
        return id_user2;
    }

    //SETTERS

    public void setId_user1(int id_user1) {
        this.id_user1 = id_user1;
    }

    public void setId_user2(int id_user2) {
        this.id_user2 = id_user2;
    }
}

