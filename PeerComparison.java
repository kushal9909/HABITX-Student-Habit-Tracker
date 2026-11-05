
class PeerComparison {

    static StudentRecord[] sortDescending(StudentDatabase db) {
        return StudentDatabase.sortByWeightedScore(db, true);
    }

    static int calculateTopPercent(int rank, int totalStudents) {
        if (totalStudents <= 0 || rank < 1 || rank > totalStudents) return 0;
        return (int) Math.round((rank * 100.0) / totalStudents);
    }

     static void showPeerComparison(StudentRecord current, StudentDatabase db) {
        if (db.getCount() < 2) {
            System.out.println("\n  Peer comparison becomes available once 2 or more students complete tracking.");
            return;
        }

        StudentRecord[] sorted = sortDescending(db);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  PEER COMPARISON - ANONYMOUS LEADERBOARD");
        System.out.println("=".repeat(60));
        System.out.println("  All student names are hidden. You are marked with  <-- You\n");

        // label counter for anonymous names
        int label = 1;

        System.out.printf("  %-6s %-12s %8s %8s %s\n", "Rank", "Student", "Avg", "Weighted", "Grade");
        System.out.println("  " + "-".repeat(56));

        for (int i = 0; i < sorted.length; i++) {
            String marker = "";
            String displayName;

            if (sorted[i].studentId.equals(current.studentId)) {
                marker = "  <-- You";
                displayName = "You";
            } else {
                displayName = "Student " + label;
                label++;
            }

            System.out.printf("  %-6s %-12s %7.1f %7.1f   %s%s\n",
                    "#" + (i + 1),
                    displayName,
                    sorted[i].averageScore,
                    sorted[i].weightedScore,
                    WeightedScoreCalculator.getGradeLabel(sorted[i].weightedScore),
                    marker);
        }

        // find current student rank
        int rank = 0;
        for (int i = 0; i < sorted.length; i++) {
            if (sorted[i].studentId.equals(current.studentId)) {
                rank = i + 1;
                break;
            }
        }

        int percentile = calculateTopPercent(rank, sorted.length);

        System.out.println("  " + "-".repeat(56));
        System.out.println("  Your Rank      : #" + rank + " out of " + sorted.length + " students");
        System.out.println("  Your Percentile: Top " + percentile + "%");
        System.out.println("=".repeat(60));
    }
}
