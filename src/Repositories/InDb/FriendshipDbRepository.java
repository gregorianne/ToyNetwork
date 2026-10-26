package Repositories.InDb;

import Domain.Friendship;
import Domain.Tuple;
import Repositories.RepoInterface;
import VE.Validator;

/**
 * Schelet pentru repo din baza de date
 */
public class FriendshipDbRepository implements RepoInterface<Tuple<Integer, Integer>, Friendship> {
    private final Validator<Friendship> validator;
    private final String url;
    private final String user;
    private final String password;

    /**
     * @param validator validator pt prietenii
     * @param url       the database url (db.url)
     * @param user      the database user (db.user)
     * @param password  the database password (db.password)
     */
    public FriendshipDbRepository(Validator<Friendship> validator, String url,
                                  String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Friendship findOne(Tuple<Integer, Integer> id) {
        throw new UnsupportedOperationException("FriendshipDbRepository.findOne - laboratorul 4");
    }

    @Override
    public Iterable<Friendship> findAll() {
        throw new UnsupportedOperationException("FriendshipDbRepository.findAll - laboratorul 4");
    }

    @Override
    public Friendship save(Friendship entity) {
        throw new UnsupportedOperationException("FriendshipDbRepository.save - laboratorul 4");
    }

    @Override
    public Friendship delete(Tuple<Integer, Integer> id) {
        throw new UnsupportedOperationException("FriendshipDbRepository.delete - laboratorul 4");
    }

    @Override
    public Friendship update(Friendship entity) {
        throw new UnsupportedOperationException("FriendshipDbRepository.update - laboratorul 4");
    }
}