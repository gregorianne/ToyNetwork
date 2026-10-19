package Service;
import Domain.User;
import Repositories.UserRepository;
import VE.*;

public class UserService {
    private final UserRepository repo;
    private final Validator<User> validator;

    public UserService(UserRepository repo, Validator<User> validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public void addUser(int id, String username, String email, String first_name, String last_name){
        if(repo.findById(id)!=null){
            throw new ValidationException("Userul deja exista!\n");
        }
        if (repo.existsUsername(username))
            throw new ValidationException("Username-ul este deja folosit!");
        User u = new User(id, username, email, first_name, last_name);
        validator.validate(u);
        repo.addUser(u);
    }

    public void deleteUser(int id){
        if(repo.findById(id)==null){
            throw new ValidationException("Userul cautat nu exista!\n");
        }
        repo.deleteUser(id);
    }
}
