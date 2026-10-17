package Domain;

public class Comment extends Entity{
    private String content;
    private int id_author;
    private int id_post;

    public Comment(int id, String content, int id_author, int id_post) {
        super(id);
        this.content = content;
        this.id_author = id_author;
        this.id_post = id_post;
    }

}
