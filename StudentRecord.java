
// Stores one completed student simulation.
class StudentRecord {
    String studentId;           // auto-generated: S001, S002, S003...
    String studentName;
    int totalDays;
    int[] finalScores;          // [0]=Study [1]=Focus [2]=Health [3]=Time
    int longestStreak;
    int finalStreak;
    int streakBreaks;           // how many times streak reset during simulation
    int[] choiceHistory;        // which option (0-3) was picked each day
    int[][] dailyScores;        // score snapshot for each habit after each day
    double averageScore;        // raw average of 4 habits
    double weightedScore;       // weighted average using WeightedScoreCalculator
    boolean demoRecord;

    StudentRecord(String id, String name, int days, int[] scores,
                         int longest, int finalStr, int breaks,
                         int[] choices, int[][] daily) {
        if (id == null || name == null || scores == null || scores.length != 4
                || choices == null || daily == null || days < 1 || daily.length != days
                || choices.length != days) {
            throw new IllegalArgumentException("Invalid student record data.");
        }
        this.studentId = id.trim();
        this.studentName = name.trim();
        this.totalDays = days;
        this.longestStreak = longest;
        this.finalStreak = finalStr;
        this.streakBreaks = breaks;

        // Copy the arrays so the stored record remains unchanged.
        this.finalScores = new int[4];
        for (int i = 0; i < 4; i++) {
            this.finalScores[i] = scores[i];
        }

        this.choiceHistory = new int[choices.length];
        for (int i = 0; i < choices.length; i++) {
            this.choiceHistory[i] = choices[i];
        }

        this.dailyScores = new int[daily.length][4];
        for (int i = 0; i < daily.length; i++) {
            for (int j = 0; j < 4; j++) {
                this.dailyScores[i][j] = daily[i][j];
            }
        }

        // Calculate the two summary scores.
        this.averageScore = WeightedScoreCalculator.calculateRawAverage(this.finalScores);
        this.weightedScore = WeightedScoreCalculator.calculateWeightedAverage(this.finalScores);
    }

    // Return the index of the lowest-scoring habit.
    int getWeakestIndex() {
        int minIdx = 0;
        for (int i = 1; i < 4; i++) {
            if (finalScores[i] < finalScores[minIdx]) minIdx = i;
        }
        return minIdx;
    }

    // Return the index of the highest-scoring habit.
    int getStrongestIndex() {
        int maxIdx = 0;
        for (int i = 1; i < 4; i++) {
            if (finalScores[i] > finalScores[maxIdx]) maxIdx = i;
        }
        return maxIdx;
    }
}
