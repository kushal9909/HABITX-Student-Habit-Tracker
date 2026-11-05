// Base class for all habit categories.
class BaseHabit {
    String name;
    int score;
    static final int MIN_SCORE = 0;
    static final int MAX_SCORE = 100;

    BaseHabit(String name, int initialScore) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Habit name cannot be empty.");
        }
        this.name = name;
        this.score = Math.max(MIN_SCORE, Math.min(MAX_SCORE, initialScore));
    }

    void updateScore(int change) {
        score += change;
        if (score > MAX_SCORE) score = MAX_SCORE;
        if (score < MIN_SCORE) score = MIN_SCORE;
    }

    String getStatus() {
        if (score >= 80) return "Excellent";
        else if (score >= 60) return "Good";
        else if (score >= 40) return "Average";
        else if (score >= 20) return "Poor";
        else return "Critical";
    }

    String getHabitType() { return "General Habit"; }

    // Overloaded display methods: default output and custom heading.
    void displayHabit() {
        displayHabit("");
    }

    void displayHabit(String prefix) {
        System.out.printf("%s%-25s : %3d/100 (%s) [%s]%n",
                prefix, name, score, getStatus(), getHabitType());
    }
}
