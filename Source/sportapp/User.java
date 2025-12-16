package sportapp;

import sportapp.membership.*;

/**
 * Represents a user of the Sport Management application.
 * <p>
 * A User has identity fields, authentication data and a membership tier.
 */
public class User {
    private String username;
    private int userID;
    private String password;
    private MemberShip membership;
    private UserSecurityAnswer securityAnswer;

    /**
     * Constructs a new User with the given attributes. New users default to Basic membership.
     * @param username username string
     * @param userID unique user identifier
     * @param password plaintext password (the project may substitute hashing in future)
     * @param securityAnswer security question/answer object for password recovery
     */
    public User(String username,int userID, String password, UserSecurityAnswer securityAnswer) {
        this.username = username;
        this.userID = userID;
        this.password = password;
        this.membership = new BasicMemberShip();
        this.securityAnswer = securityAnswer;
    }
    
    /**
     * Returns the username.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the numeric user id.
     * @return user id
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Returns the user's membership tier implementation.
     * @return membership object
     */
    public MemberShip getMembership() {
        return membership;
    }

    /**
     * Returns the user's password (stored in plain text in this prototype).
     * @return password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the user's password.
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Returns the security question associated with this user.
     * @return security question text
     */
    public String getQuestion() {
        return this.securityAnswer.getQuestion();
    }
    
    /**
     * Verifies whether the provided answer matches the stored security answer.
     * @param answer answer to check
     * @return true if the answer matches, false otherwise
     */
    public boolean verifySercurityAnswer(String answer) {
        return this.securityAnswer.getAnswer().equals(answer);
    }
    
    /**
     * Verifies whether the provided password matches the user's password.
     * @param password password to verify
     * @return true if passwords match
     */
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }
    
    /**
     * Upgrades the user's membership to Gold.
     */
    public void upgradeToGold() {
        this.membership = new GoldMemberShip();
    }
    
    /**
     * Upgrades the user's membership to Platinum.
     */
    public void upgradeToPlatinum() {
        this.membership = new PlatinumMemberShip();
    }
}
