package quiz;

import java.util.List;

public class QuizResult {
    private final List<QuestionAttempt> attempts;

    public QuizResult(List<QuestionAttempt> attempts) {
        this.attempts = List.copyOf(attempts);
    }

    public int getTotalQuestions() {
        return attempts.size();
    }

    public int getCorrectCount() {
        int correctCount = 0;

        for (QuestionAttempt attempt : attempts) {
            if (attempt.isCorrect()) {
                correctCount++;
            }
        }

        return correctCount;
    }

    public int getIncorrectCount() {
        return getTotalQuestions() - getCorrectCount();
    }

    public double getFinalScorePercent() {
        if (attempts.isEmpty()) {
            return 0.0;
        }

        return (getCorrectCount() * 100.0) / getTotalQuestions();
    }

    public List<QuestionAttempt> getAttempts() {
        return attempts;
    }

    public List<QuestionAttempt> getIncorrectAttempts() {
        return attempts.stream()
                .filter(attempt -> !attempt.isCorrect())
                .toList();
    }
}
