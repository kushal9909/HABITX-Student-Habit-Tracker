
class WeightedScoreCalculator {
    // weights match habits[] order: [Study, Focus, Health, Time]
    static final double[] WEIGHTS = {0.30, 0.25, 0.15, 0.30};

    //returns weighted average
     static double calculateWeightedAverage(int[] scores) {
        validateScores(scores);
        double weighted = 0;
        for (int i = 0; i < 4; i++) {
            weighted += scores[i] * WEIGHTS[i];
        }
        return weighted;
    }

    // raw average (simple mean)
     static double calculateRawAverage(int[] scores) {
        validateScores(scores);
        int total = 0;
        for (int i = 0; i < 4; i++) {
            total += scores[i];
        }
        return (double) total / 4;
    }

    private static void validateScores(int[] scores) {
        if (scores == null || scores.length != 4) {
            throw new IllegalArgumentException("Exactly four habit scores are required.");
        }
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] < 0 || scores[i] > 100) {
                throw new IllegalArgumentException("Habit scores must be between 0 and 100.");
            }
        }
    }

    // grade label from a score value
     static String getGradeLabel(double score) {
        if (score >= 80) return "Excellent";
        else if (score >= 60) return "Good";
        else if (score >= 40) return "Average";
        else if (score >= 20) return "Poor";
        else return "Critical";
    }
}
