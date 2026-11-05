import java.util.NoSuchElementException;
import java.util.Scanner;

class HabitXSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentDatabase database = new StudentDatabase();
        if (args.length > 0 && args[0].equalsIgnoreCase("--demo-data")) {
            loadDemoData(database);
        }

        HabitXEngine system = new HabitXEngine(scanner);
        displayWelcome();

        try {
            boolean running = true;
            while (running) {
                System.out.println("\n" + "=".repeat(60));
                System.out.println("  MAIN MENU");
                System.out.println("=".repeat(60));
                System.out.println("  1. Student Mode");
                System.out.println("  2. Mentor Mode");
                System.out.println("  3. Exit");
                System.out.println("=".repeat(60));
                System.out.print("  Who is using the system? (1-3): ");

                int choice = readValidatedInput(scanner, 1, 3);
                switch (choice) {
                    case 1:
                        if (!canStartStudent(database)) {
                            System.out.println("\n  Student limit reached. No new simulation was started.");
                            break;
                        }
                        system.resetForNextStudent();
                        system.runSimulation();
                        String newId = database.generateNextId();
                        StudentRecord record = system.buildRecord(newId);
                        database.addStudent(record);
                        System.out.println("\n  Your record has been added to the current session.");
                        System.out.println("  Student ID: " + newId);

                        if (database.getCount() >= 2) {
                            System.out.println("\n  Would you like to see an anonymous peer comparison?");
                            System.out.println("  1. Yes");
                            System.out.println("  2. No");
                            System.out.print("  Choose (1-2): ");
                            if (readValidatedInput(scanner, 1, 2) == 1) {
                                PeerComparison.showPeerComparison(record, database);
                            }
                        }
                        System.out.println("=".repeat(60));
                        System.out.printf("  Raw Average   : %.1f/100%n", record.averageScore);
                        System.out.printf("  Weighted Score: %.1f/100%n", record.weightedScore);
                        System.out.println("=".repeat(60));
                        System.out.println("\n  Thank you for completing HABITX!\n");
                        break;
                    case 2:
                        if (!database.hasData()) {
                            System.out.println("\n  No student records are available in this session.");
                        } else {
                            new MentorMenu(database, scanner).showMentorMenu();
                        }
                        break;
                    case 3:
                        running = false;
                        System.out.println("\n  Thank you for using HABITX. Goodbye!\n");
                        break;
                    default:
                        break;
                }
            }
        } catch (NoSuchElementException ex) {
            System.out.println("\n\n  Input closed. HABITX has ended safely.");
        }
    }

    static void displayWelcome() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    HABITX");
        System.out.println("          Student Habit Analysis System");
        System.out.println("=".repeat(60));
        System.out.println("  Student decisions are used to track four habit dimensions.");
        System.out.println("  Mentors can review completed records in Mentor Mode.");
        System.out.println("=".repeat(60));
    }

    static boolean canStartStudent(StudentDatabase database) {
        return database.getCount() < StudentDatabase.MAX_STUDENTS;
    }

    static int readValidatedInput(Scanner scanner, int min, int max) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException ex) {
                // Invalid input is handled by the prompt below.
            }
            System.out.print("  Enter a whole number between " + min + " and " + max + ": ");
        }
    }

    static void loadDemoData(StudentDatabase database) {
        String[] names = {"Arjun Sharma", "Priya Patel", "Rahul Verma", "Ananya Reddy", "Vikram Singh"};
        int[][] choices = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 1, 0},
                {1, 2, 1, 2, 1, 0, 2},
                {2, 3, 2, 2, 3, 2, 3},
                {3, 3, 3, 2, 3, 3, 3}
        };

        for (int i = 0; i < choices.length; i++) {
            HabitXEngine engine = new HabitXEngine(new Scanner(""));
            engine.initializeScenarios();
            engine.studentName = names[i];
            engine.totalDays = choices[i].length;
            engine.choiceHistory = new int[engine.totalDays];
            engine.dailyScores = new int[engine.totalDays][HabitXEngine.HABIT_COUNT];

            int previousStreak = 0;
            for (int day = 0; day < engine.totalDays; day++) {
                int choice = choices[i][day];
                Scenario scenario = engine.scenarios[day % engine.scenarios.length];
                engine.choiceHistory[day] = choice;
                engine.applyImpacts(scenario.getImpacts(choice));
                for (int habit = 0; habit < HabitXEngine.HABIT_COUNT; habit++) {
                    engine.dailyScores[day][habit] = engine.habits[habit].score;
                }
                boolean positive = scenario.isPositiveChoice(choice);
                engine.updateStreak(positive);
                if (!positive && previousStreak > 0) engine.streakBreaks++;
                previousStreak = engine.currentStreak;
            }

            StudentRecord record = engine.buildRecord(database.generateNextId());
            record.demoRecord = true;
            database.addStudent(record);
        }
        System.out.println("\n  Five demonstration records loaded. They are marked DEMO in Mentor Mode.");
    }
}
