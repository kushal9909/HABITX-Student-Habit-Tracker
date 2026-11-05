import java.util.Scanner;

class MentorMenu {
    Scanner scanner;
    StudentDatabase db;

    static final String[] HABIT_NAMES = {
            "Study Discipline", "Focus and Attention", "Health and Energy", "Time Management"
    };

     MentorMenu(StudentDatabase database, Scanner scanner) {
        this.db = database;
        this.scanner = scanner;
    }

     void showMentorMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("          MENTOR MODE");
            System.out.println("=".repeat(60));
            System.out.println("  Students in database: " + db.getCount());
            System.out.println();
            System.out.println("  1. View all students");
            System.out.println("  2. View by intervention priority (high to low)");
            System.out.println("  3. Search for a student");
            System.out.println("  4. View top performing students");
            System.out.println("  5. View most critical students");
            System.out.println("  6. Back to main menu");
            System.out.println("=".repeat(60));
            System.out.print("  Enter your choice (1-6): ");

            int choice = readIntInput(1, 6);

            switch (choice) {
                case 1:
                    viewAllStudents();
                    break;
                case 2:
                    viewByInterventionPriority();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    viewTopPerformers();
                    break;
                case 5:
                    viewMostCritical();
                    break;
                case 6:
                    running = false;
                    break;
            }
        }
    }

    void viewAllStudents() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ALL STUDENTS");
        System.out.println("=".repeat(60));

        printStudentTableHeader();

        for (int i = 0; i < db.getCount(); i++) {
            printStudentTableRow(db.getStudent(i));
        }

        System.out.println("  " + "-".repeat(56));
        System.out.println("  Total students: " + db.getCount());
        System.out.println("=".repeat(60));
    }

     void viewByInterventionPriority() {
        StudentRecord[] sorted = StudentDatabase.sortByWeightedScore(db, false);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  INTERVENTION PRIORITY  (highest need first)");
        System.out.println("=".repeat(60));

        System.out.printf("  %-8s %-6s %-18s %8s %9s%n",
                "Priority", "ID", "Name", "Raw", "Weighted");
        System.out.println("  " + "-".repeat(56));

        for (int i = 0; i < sorted.length; i++) {
            System.out.printf("  %-8s %-6s %-18s %7.1f %8.1f%n",
                    "#" + (i + 1),
                    sorted[i].studentId,
                    sorted[i].studentName,
                    sorted[i].averageScore,
                    sorted[i].weightedScore);

        }

        System.out.println("  " + "-".repeat(56));
        System.out.println("=".repeat(60));
    }

     void searchStudent() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  SEARCH STUDENT");
        System.out.println("=".repeat(60));
        System.out.println("  1. Search by Name");
        System.out.println("  2. Search by ID (e.g. S001)");
        System.out.print("  Choose (1-2): ");

        int type = readIntInput(1, 2);

        if (type == 1) {
            System.out.print("  Enter name (partial match works): ");
            String query = scanner.nextLine().trim();
            if (query.isEmpty()) {
                System.out.println("  Search name cannot be empty.");
                return;
            }
            StudentRecord[] results = db.searchByName(query);

            if (results.length == 0) {
                System.out.println("\n  No students found matching \"" + query + "\"");
            } else {
                System.out.println("\n  Found " + results.length + " result(s):\n");
                for (int i = 0; i < results.length; i++) {
                    printDetailedStudentCard(results[i]);
                }
            }
        } else {
            System.out.print("  Enter Student ID (e.g. S001): ");
            String id = scanner.nextLine().trim().toUpperCase();
            StudentRecord result = db.searchById(id);

            if (result == null) {
                System.out.println("\n  No student found with ID \"" + id + "\"");
            } else {
                printDetailedStudentCard(result);
            }
        }
    }

     void viewTopPerformers() {
        StudentRecord[] sorted = StudentDatabase.sortByWeightedScore(db, true);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  TOP PERFORMING STUDENTS  (best first)");
        System.out.println("=".repeat(60));
        System.out.println("  These students deserve positive feedback and recognition.\n");

        printStudentTableHeader();
        for (int i = 0; i < sorted.length; i++) {
            printStudentTableRow(sorted[i]);
        }
        System.out.println("  " + "-".repeat(56));
        System.out.println("=".repeat(60));
    }

     void viewMostCritical() {
        // pass 1: count how many are critical
        int criticalCount = 0;
        for (int i = 0; i < db.getCount(); i++) {
            StudentRecord s = db.getStudent(i);
            if (s.weightedScore < 40) {
                criticalCount++;
            }
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  MOST CRITICAL STUDENTS  (needs immediate attention)");
        System.out.println("=".repeat(60));

        if (criticalCount == 0) {
            System.out.println("\n  No critical students at this time.");
            System.out.println("  All students are performing at acceptable levels.\n");
        } else {
            System.out.println("  Found " + criticalCount + " critical student(s):\n");

            // pass 2: display each critical student with full detail
            for (int i = 0; i < db.getCount(); i++) {
                StudentRecord s = db.getStudent(i);
                if (s.weightedScore < 40) {
                    printDetailedStudentCard(s);
                }
            }
        }
        System.out.println("=".repeat(60));
    }

     void printDetailedStudentCard(StudentRecord s) {
        System.out.println("\n  " + "-".repeat(56));
        System.out.println("  Student: " + s.studentName + "  |  ID: " + s.studentId +
                "  |  Days: " + s.totalDays + (s.demoRecord ? "  |  DEMO" : ""));
        System.out.println("  " + "-".repeat(56));

        // habit scores
        System.out.println("  Habit Scores:");
        for (int h = 0; h < 4; h++) {
            String marker = "";
            if (h == s.getWeakestIndex()) marker = "  <-- Weakest";
            if (h == s.getStrongestIndex()) marker = "  <-- Strongest";
            System.out.printf("    %-25s: %3d/100  %s\n", HABIT_NAMES[h], s.finalScores[h], marker);
        }

        // scores summary
        System.out.println();
        System.out.printf("    Raw Average   : %.1f/100  (%s)\n",
                s.averageScore, WeightedScoreCalculator.getGradeLabel(s.averageScore));
        System.out.printf("    Weighted Score: %.1f/100  (%s)\n",
                s.weightedScore, WeightedScoreCalculator.getGradeLabel(s.weightedScore));

        // streak info
        System.out.println();
        System.out.println("    Longest Streak : " + s.longestStreak + " days");
        System.out.println("    Streak Breaks  : " + s.streakBreaks);

        System.out.println("\n    DAILY PROGRESS:");
        System.out.printf("    %-4s %-8s %6s %6s %6s %6s%n", "Day", "Choice", "Study", "Focus", "Health", "Time");
        for (int day = 0; day < s.totalDays; day++) {
            System.out.printf("    %-4d %-8d %6d %6d %6d %6d%n",
                    day + 1, s.choiceHistory[day] + 1,
                    s.dailyScores[day][0], s.dailyScores[day][1],
                    s.dailyScores[day][2], s.dailyScores[day][3]);
        }

        System.out.println();
        System.out.println("  " + "-".repeat(56));
    }

     void printStudentTableHeader() {
        System.out.printf("  %-6s %-18s %7s %7s %-10s%n",
                "ID", "Name", "Raw", "Wt.", "Grade");
        System.out.println("  " + "-".repeat(56));
    }

     void printStudentTableRow(StudentRecord s) {
        System.out.printf("  %-6s %-18s %6.1f %6.1f   %-10s%n",
                s.studentId,
                s.studentName + (s.demoRecord ? " [DEMO]" : ""),
                s.averageScore,
                s.weightedScore,
                WeightedScoreCalculator.getGradeLabel(s.weightedScore));

    }

     StudentRecord[] copyAllRecords() {
        StudentRecord[] copy = new StudentRecord[db.getCount()];
        for (int i = 0; i < db.getCount(); i++) {
            copy[i] = db.getStudent(i);
        }
        return copy;
    }

    int readIntInput(int min, int max) {
        while (true) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                System.out.print("  Invalid input. Enter a number between " + min + " and " + max + ": ");
                continue;
            }

            // Check if all characters are digits (only positive numbers)
            boolean isAllDigits = true;
            for (int i = 0; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i))) {
                    isAllDigits = false;
                    break;
                }
            }

            if (!isAllDigits) {
                System.out.print("  Invalid input. Enter a number between " + min + " and " + max + ": ");
                continue;
            }

            int val;
            try {
                val = Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.print("  Invalid input. Enter a number between " + min + " and " + max + ": ");
                continue;
            }

            if (val >= min && val <= max) {
                return val;
            }

            System.out.print("  Please enter a number between " + min + " and " + max + ": ");
        }
    }

}
