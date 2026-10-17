package Domain;

public class Post extends Entity{
    private String content;
    private int id_author;

    public Post(int id, String content, int id_author) {
        super(id);
        this.content = content;
        this.id_author = id_author;
    }
}
