package sportapp;
import java.util.ArrayList;

/**
 * Singleton collection that manages application users.
 * <p>
 * Provides simple CRUD-like helpers used by the UI and tests.
 */
public class UserCollection {

    private static final UserCollection instance = new UserCollection();
    private ArrayList<User> users;
    private static int userIDCounter = 0;

    private UserCollection() {
        users = new ArrayList<>();
    }

    /**
     * Returns the singleton instance.
     * @return UserCollection singleton
     */
    public static UserCollection getInstance() {
        return instance;
    }

    /**
     * Creates a new user and adds it to the collection.
     * @param username username string
     * @param password password string
     * @param securityAnswer user's security question/answer
     * @return created User object
     */
    public User addUser(String username,String password, UserSecurityAnswer securityAnswer) {
        int userID = userIDCounter++;
        User user = new User(username, userID, password, securityAnswer);
        users.add(user);
        
        return user;
    }

    /**
     * Adds an existing user object to the collection.
     * @param user user instance
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Removes a user by their username, if present.
     * @param username username of user to remove
     */
    public void removeUserByName(String username) {
        User userToRemove = findUserByName(username);
        if (userToRemove != null) {
            users.remove(userToRemove);
        }
    }

    /**
     * Finds a user by username.
     * @param username username to search
     * @return matching User or null when not found
     */
    public User findUserByName(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Checks whether the collection contains the user instance.
     * @param user user instance
     * @return true if present
     */
    public boolean checkUserExist(User user) {
        return users.contains(user);
    }

    /**
     * Returns the backing list of users. Caller should not modify directly.
     * @return list of users
     */
    public ArrayList<User> findUser() {
        return users;
    }
    
    /**
     * Clears the collection (used by tests/initialization).
     */
    public void clear() {
    	users.clear();
    }
}
