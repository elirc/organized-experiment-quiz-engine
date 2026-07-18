package quiz;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class QuizEngine {
    private final Scanner scanner;
    private final PrintStream out;

    public QuizEngine(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    public QuizResult runQuiz(List<Question> questions) {
        List<QuestionAttempt> attempts = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            attempts.add(askQuestion(question, i + 1, questions.size()));
        }

        return new QuizResult(attempts);
    }

    private QuestionAttempt askQuestion(Question question, int questionNumber, int totalQuestions) {
        out.println();
        out.println("Question " + questionNumber + " of " + totalQuestions);
        out.println(question.getPrompt());

        for (int i = 0; i < question.getChoices().size(); i++) {
            String label = Question.indexToAnswerLabel(i);
            out.println(label + ". " + question.getChoices().get(i));
        }

        String userAnswer = readValidAnswer(question.getChoices().size());

        if (question.isCorrectAnswer(userAnswer)) {
            out.println("Correct!");
        } else {
            out.println("Incorrect.");
        }

        return new QuestionAttempt(question, userAnswer);
    }

    private String readValidAnswer(int numberOfChoices) {
        while (true) {
            out.print("Your answer: ");
            String input = scanner.nextLine().trim().toUpperCase(Locale.ROOT);

            if (isValidAnswer(input, numberOfChoices)) {
                return input;
            }

            String lastValidLabel = Question.indexToAnswerLabel(numberOfChoices - 1);
            out.println("Please enter a letter from A to " + lastValidLabel + ".");
        }
    }

    private boolean isValidAnswer(String input, int numberOfChoices) {
        if (input.length() != 1) {
            return false;
        }

        char answer = input.charAt(0);
        return answer >= 'A' && answer < ('A' + numberOfChoices);
    }
}
