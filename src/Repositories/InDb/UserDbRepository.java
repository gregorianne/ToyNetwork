package Repositories.InDb;

import Domain.User;
import Repositories.RepoInterface;
import VE.ValidationException;
import VE.Validator;

/**
 * Schelet pentru repo din baza de date
 */
public class UserDbRepository implements RepoInterface<Integer, User> {
    private final Validator<User> validator;
    private final String url;
    private final String user;
    private final String password;

    /**
     * @param validator validator pr utilizatori
     * @param url       the database url (db.url)
     * @param user      the database user (db.user)
     * @param password  the database password (db.password)
     */
    public UserDbRepository(Validator<User> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public User findOne(Integer id) {
        throw new UnsupportedOperationException("UserDbRepository.findOne - laboratorul 6");
    }

    @Override
    public Iterable<User> findAll() {
        throw new UnsupportedOperationException("UserDbRepository.findAll - laboratorul 6");
    }

    @Override
    public User save(User entity) {
        throw new UnsupportedOperationException("UserDbRepository.save - laboratorul 6");
    }

    @Override
    public User delete(Integer id) {
        throw new UnsupportedOperationException("UserDbRepository.delete - laboratorul 6");
    }

    @Override
    public User update(User entity) {
        throw new UnsupportedOperationException("UserDbRepository.update - laboratorul 6");
    }
}