package sportapp;

/**
 * Simple container for a user's security question and answer.
 */
public class UserSecurityAnswer {
    private String question;
    private String answer;

    /**
     * Constructs a new pair of security question and answer.
     * @param question question text
     * @param answer answer text
     */
    public UserSecurityAnswer(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    /**
     * Returns the stored answer.
     * @return answer string
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Returns the stored question.
     * @return question string
     */
    public String getQuestion() {
        return question;
    }
}
