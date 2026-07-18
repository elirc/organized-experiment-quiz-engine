package quiz;

public class QuestionFileException extends Exception {
    public QuestionFileException(String message) {
        super(message);
    }

    public QuestionFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
