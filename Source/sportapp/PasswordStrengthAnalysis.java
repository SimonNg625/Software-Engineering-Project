package sportapp;

/**
 * Utility class for password strength checking.
 */
public class PasswordStrengthAnalysis {
    /**
     * Constructs a PasswordStrengthAnalysis instance.
     * <p>
     * This constructor initializes the PasswordStrengthAnalysis object with default values.
     */
    public PasswordStrengthAnalysis() {
        // Default constructor
    }

    /**
     * Evaluates whether a password meets the project's strength policy.
     * Policy: at least 8 characters, contains upper/lower/digit/special.
     * @param password candidate password
     * @return true when password is considered strong
     */
    public static boolean isStrongPassword(String password){
        if (password.length() < 8) { return false; }
        
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for(char c: password.toCharArray()){
            if (Character.isUpperCase(c)) { hasUpper = true; }
            else if (Character.isLowerCase(c)) { hasLower = true; }
            else if (Character.isDigit(c)) { hasDigit = true; }
            else { hasSpecial = true; }
        }
        return (hasUpper && hasLower && hasDigit && hasSpecial);
    }
}
