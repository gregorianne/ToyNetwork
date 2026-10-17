package Domain;

//ca sa nu face un repo nou pentru toate tipurile de entitati: User, Friendship, Comment etc...
public class Entity {
    private int id;

    public Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
