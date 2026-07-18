package quiz;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class App {
    private static final Path QUESTIONS_FILE = Path.of("data", "questions.txt");

    private final Scanner scanner;
    private final PrintStream out;
    private List<Question> questions;

    public App(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    public static void main(String[] args) {
        App app = new App(new Scanner(System.in), System.out);
        app.run();
    }

    public void run() {
        if (!loadQuestions()) {
            return;
        }

        out.println("Welcome to the Quiz Engine");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> startQuiz();
                case "2" -> showQuestionCount();
                case "3" -> running = false;
                default -> out.println("Please enter 1, 2, or 3.");
            }
        }

        out.println("Goodbye!");
    }

    private boolean loadQuestions() {
        QuestionLoader loader = new QuestionLoader();

        try {
            questions = loader.loadQuestions(QUESTIONS_FILE);
            return true;
        } catch (QuestionFileException e) {
            out.println("The quiz could not be started.");
            out.println(e.getMessage());
            out.println("Expected file: " + QUESTIONS_FILE.toAbsolutePath());
            return false;
        }
    }

    private void printMenu() {
        out.println();
        out.println("1. Start quiz");
        out.println("2. View available questions count");
        out.println("3. Exit");
        out.print("Choose an option: ");
    }

    private void startQuiz() {
        QuizEngine engine = new QuizEngine(scanner, out);
        QuizResult result = engine.runQuiz(questions);

        out.println();
        out.println("Quiz finished!");
        out.println("Correct answers: " + result.getCorrectCount());
        out.println("Incorrect answers: " + result.getIncorrectCount());
        out.println("Final score: " + String.format("%.2f", result.getFinalScorePercent()) + "%");

        if (result.getIncorrectCount() > 0 && shouldShowCorrectAnswers()) {
            printMissedQuestions(result);
        }
    }

    private void showQuestionCount() {
        out.println("There are " + questions.size() + " questions available.");
    }

    private boolean shouldShowCorrectAnswers() {
        while (true) {
            out.print("Show missed questions and correct answers? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

            if (input.equals("y") || input.equals("yes")) {
                return true;
            }

            if (input.equals("n") || input.equals("no")) {
                return false;
            }

            out.println("Please enter y or n.");
        }
    }

    private void printMissedQuestions(QuizResult result) {
        out.println();
        out.println("Missed questions:");

        for (QuestionAttempt attempt : result.getIncorrectAttempts()) {
            Question question = attempt.getQuestion();

            out.println("- " + question.getPrompt());
            out.println("  Your answer: " + attempt.getUserAnswer() + " - " + attempt.getUserAnswerText());
            out.println("  Correct answer: " + question.getCorrectAnswer() + " - " + question.getCorrectChoiceText());

            if (question.hasExplanation()) {
                out.println("  Explanation: " + question.getExplanation());
            }
        }
    }
}
