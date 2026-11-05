import java.util.Scanner;

// Main HABITX System Class
class HabitXEngine {
    Habit[] habits;
    Scenario[] scenarios;
    private final Scanner scanner;
    static final int HABIT_COUNT = 4;
    int totalDays;
    int currentStreak;
    int longestStreak;
    int[] choiceHistory;        // which option was chosen each day
    int[][] dailyScores;        // score snapshot after each day [day][habit]
    int streakBreaks;

    // Store student name for mentor mode
    String studentName;

     HabitXEngine(Scanner scanner) {
        this.scanner = scanner;
        currentStreak = 0;
        longestStreak = 0;
        initializeHabits();
    }

     void initializeHabits() {
        habits = new Habit[HABIT_COUNT];
        habits[0] = new StudyHabit(50);
        habits[1] = new FocusHabit(50);
        habits[2] = new HealthHabit(50);
        habits[3] = new TimeManagementHabit(50);
    }

    int getValidatedIntInput(int min, int max, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException ex) {
                // Re-prompt for blank, non-numeric, or out-of-range input.
            }
            System.out.println("Invalid input. Please enter a whole number between " + min + " and " + max + ".");
        }
    }

     void selectDuration() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SELECT ANALYSIS DURATION");
        System.out.println("=".repeat(60));
        System.out.println("Enter the number of days you want to track your habits:");
        System.out.println("(You can choose any number from 1 to 100 days)");
        System.out.println("\nSuggested durations:");
        System.out.println("  - 7 days (1 week)");
        System.out.println("  - 14 days (2 weeks)");
        System.out.println("  - 21 days (habit formation period)");
        System.out.println("  - 30 days (1 month)");

        totalDays = getValidatedIntInput(1, 100, "\nEnter number of days (1-100): ");

        System.out.println("\nYou selected: " + totalDays + " day(s)");
        if (totalDays > 7) {
            System.out.println("Note: The system will cycle through 7 base scenarios.");
        }
        System.out.println("Your habit tracking journey begins now!");
    }

     void initializeScenarios() {
        scenarios = new Scenario[7];

        scenarios[0] = new Scenario(
                "It is 7:00 AM. Your first class starts at 9:00 AM.\nHow do you begin your day?",
                new String[]{
                        "Wake up immediately, exercise, and have a proper breakfast",
                        "Snooze alarm and wake up at 8:30 AM, skip breakfast",
                        "Wake up at 8:00 AM, take a quick shower and leave",
                        "Continue sleeping and skip the morning class"
                },
                new int[][]{
                        {5, 5, 10, 5},
                        {-5, -5, -10, -5},
                        {0, 0, -5, 0},
                        {-15, -10, -5, -15}
                }
        );

        scenarios[1] = new Scenario(
                "During lecture, your phone buzzes with multiple social media notifications.\nHow do you respond?",
                new String[]{
                        "Keep phone in bag, focus on lecture and take notes",
                        "Quickly glance at phone and return attention to lecture",
                        "Check phone periodically while partially listening",
                        "Engage with phone throughout the class session"
                },
                new int[][]{
                        {10, 10, 0, 5},
                        {0, -5, 0, 0},
                        {-5, -10, 0, -5},
                        {-10, -15, 0, -10}
                }
        );

        scenarios[2] = new Scenario(
                "You receive an assignment with a deadline of three days from now.\nWhen do you begin working on it?",
                new String[]{
                        "Start today and work systematically over three days",
                        "Begin tomorrow with planned breaks",
                        "Start the night before the deadline",
                        "Request a peer's completed work to replicate"
                },
                new int[][]{
                        {15, 5, 5, 15},
                        {5, 0, 0, 5},
                        {-5, -10, -10, -10},
                        {-20, -5, 0, -15}
                }
        );

        scenarios[3] = new Scenario(
                "Friends invite you to join a study group session for tomorrow's test.\nWhat is your response?",
                new String[]{
                        "Participate actively and contribute to group learning",
                        "Attend primarily to benefit from others' explanations",
                        "Decline respectfully and study independently at home",
                        "Decline and spend time on entertainment instead"
                },
                new int[][]{
                        {10, 5, 0, 10},
                        {5, 0, 0, 5},
                        {10, 10, 0, 5},
                        {-15, -10, 5, -15}
                }
        );

        scenarios[4] = new Scenario(
                "It is 11:00 PM. You have classes scheduled at 9:00 AM tomorrow.\nHow do you utilize this time?",
                new String[]{
                        "Sleep early after brief review of tomorrow's topics",
                        "Watch one episode of a series before sleeping",
                        "Study intensively until 2:00 AM for tomorrow's class",
                        "Remain awake throughout the night for gaming or browsing"
                },
                new int[][]{
                        {5, 5, 10, 5},
                        {0, -5, -5, 0},
                        {5, -5, -10, 0},
                        {-10, -10, -15, -10}
                }
        );

        scenarios[5] = new Scenario(
                "While studying, you experience a strong urge to check social media.\nHow do you manage this distraction?",
                new String[]{
                        "Use application blocker and maintain focus throughout session",
                        "Check once every thirty minutes as a scheduled break",
                        "Keep phone accessible and check frequently",
                        "Yield to distraction and browse for extended duration"
                },
                new int[][]{
                        {10, 15, 0, 10},
                        {5, 5, 0, 5},
                        {-5, -10, 0, -5},
                        {-15, -20, -5, -15}
                }
        );

        scenarios[6] = new Scenario(
                "It is the end of the week. Upcoming tests are scheduled in two weeks.\nHow do you prepare and plan ahead?",
                new String[]{
                        "Create detailed study schedule and begin systematic revision",
                        "Identify important topics and practice problem-solving",
                        "Plan to intensively study during the final week only",
                        "Rely on previous notes without structured preparation"
                },
                new int[][]{
                        {20, 15, 5, 20},
                        {10, 5, 0, 10},
                        {-5, -10, -5, -10},
                        {-20, -15, 0, -20}
                }
        );
    }

     void displayWelcome() {
        System.out.println("\nSTUDENT HABIT TRACKING");
        System.out.println("This system analyzes student habits through decision-making");
        System.out.println("scenarios over a customizable period.");
        System.out.println("\nFour habit dimensions are tracked:");
        System.out.println("  1. Study Discipline");
        System.out.println("  2. Focus and Attention");
        System.out.println("  3. Health and Energy");
        System.out.println("  4. Time Management");
        System.out.println("\nEach decision impacts these habit scores.");
        System.out.println("Maintain positive streaks for motivational rewards!");
    }

     int getUserChoice(int maxOptions) {
        return getValidatedIntInput(1, maxOptions, "\nEnter your choice (1-" + maxOptions + "): ") - 1;
    }

     void applyImpacts(int[] impacts) {
        for (int i = 0; i < HABIT_COUNT; i++) {
            habits[i].updateScore(impacts[i]);
        }
    }

     void updateStreak(boolean isPositive) {
        if (isPositive) {
            currentStreak++;
            if (currentStreak > longestStreak) {
                longestStreak = currentStreak;
            }
        } else {
            currentStreak = 0;
        }
    }

     void displayStreakStatus() {
        System.out.println("\n" + "-".repeat(60));

        if (currentStreak >= 3) {
            System.out.println("STREAK ACTIVE: " + currentStreak + " consecutive positive days!");
            if (currentStreak == 3) {
                System.out.println("Great start! You have built a three-day streak.");
            } else if (currentStreak < 5) {
                System.out.println("Good consistency. Keep it going!");
            } else if (currentStreak < 7) {
                System.out.println("Your consistency is paying off. Keep going!");
            } else if (currentStreak == 7) {
                System.out.println("One-week streak achieved!");
            } else {
                System.out.println("You have continued beyond a one-week streak!");
            }
            System.out.println("Longest Streak: " + longestStreak + " days");
        } else if (currentStreak > 0) {
            System.out.println("Current positive responses: " + currentStreak + " day(s)");
            System.out.println("Keep making positive choices. A three-day streak is the next milestone.");
        } else {
            System.out.println("STREAK BROKEN.");
            System.out.println("Your previous streak has ended. Start fresh with positive choices!");
            if (longestStreak > 0) {
                System.out.println("Your longest streak was: " + longestStreak + " days");
            }
        }

        System.out.println("-".repeat(60));
    }

     void displayCurrentAnalysis(int dayNumber) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("CURRENT HABIT ANALYSIS - DAY " + dayNumber);
        System.out.println("=".repeat(60));

        System.out.println("\nCurrent Habit Scores:");
        System.out.println("-".repeat(60));
        for (int i = 0; i < HABIT_COUNT; i++) {
            habits[i].displayHabit();
        }
        System.out.println("-".repeat(60));

        double avgScore = calculateAverageScore();
        String overallGrade = WeightedScoreCalculator.getGradeLabel(avgScore);

        System.out.println("\nCurrent Performance:");
        System.out.printf("Average Score : %.2f/100\n", avgScore);
        System.out.println("Current Grade : " + overallGrade);

        displayStreakStatus();

        System.out.println("=".repeat(60));
    }

     double calculateAverageScore() {
        int total = 0;
        for (int i = 0; i < HABIT_COUNT; i++) {
            total += habits[i].score;
        }
        return (double) total / HABIT_COUNT;
    }

     int findWeakestHabit() {
        int minScore = habits[0].score;
        int weakestIndex = 0;

        for (int i = 1; i < HABIT_COUNT; i++) {
            if (habits[i].score < minScore) {
                minScore = habits[i].score;
                weakestIndex = i;
            }
        }
        return weakestIndex;
    }

     void generateFinalVerdict() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          FINAL EVALUATION - DAY " + totalDays);
        System.out.println("=".repeat(60));

        System.out.println("\nFINAL HABIT SUMMARY:");
        System.out.println("-".repeat(60));
        for (int i = 0; i < HABIT_COUNT; i++) {
            habits[i].displayHabit();
        }
        System.out.println("-".repeat(60));

        double avgScore = calculateAverageScore();
        String overallGrade = WeightedScoreCalculator.getGradeLabel(avgScore);

        System.out.println("\nOVERALL HABIT GRADE:");
        System.out.println("-".repeat(60));
        System.out.printf("Average Score : %.2f/100\n", avgScore);
        System.out.println("Overall Grade : " + overallGrade);
        System.out.println("-".repeat(60));

        System.out.println("\nSTREAK STATISTICS:");
        System.out.println("-".repeat(60));
        System.out.println("Final Streak: " + currentStreak + " days");
        System.out.println("Longest Streak: " + longestStreak + " days");
        System.out.println("-".repeat(60));

        generateFinalSuggestion();
        generateCompoundSuggestion();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("End of Student Analysis");
        System.out.println("=".repeat(60));
    }

     void generateFinalSuggestion() {
        int weakestIndex = findWeakestHabit();

        System.out.println("\nFINAL SUGGESTION:");
        System.out.println("-".repeat(60));
        System.out.println("Area Requiring Attention: " + habits[weakestIndex].name);
        System.out.println();

        switch (weakestIndex) {
            case 0:
                System.out.println("Your Study Discipline requires improvement.");
                System.out.println("Establishing a consistent daily study routine and beginning");
                System.out.println("assignments early will significantly enhance academic performance.");
                break;
            case 1:
                System.out.println("Your Focus and Attention need strengthening.");
                System.out.println("Minimize distractions during study sessions and practice");
                System.out.println("concentration techniques to improve learning efficiency.");
                break;
            case 2:
                System.out.println("Your Health and Energy levels require attention.");
                System.out.println("Prioritize adequate sleep, regular exercise, and proper nutrition");
                System.out.println("to sustain optimal cognitive and physical performance.");
                break;
            case 3:
                System.out.println("Your Time Management needs improvement.");
                System.out.println("Create structured schedules, set priorities, and avoid procrastination");
                System.out.println("to maximize productivity and reduce last-minute stress.");
                break;
        }
        System.out.println("-".repeat(60));
    }

     void generateCompoundSuggestion() {
        System.out.println("\nLONG-TERM IMPACT PROJECTION:");
        System.out.println("-".repeat(60));

        double avgScore = calculateAverageScore();

        if (avgScore >= 80) {
            System.out.println("Maintaining your current positive habits will compound over time,");
            System.out.println("leading to consistent academic excellence, reduced stress, and");
            System.out.println("improved overall well-being in the long term.");
        } else if (avgScore >= 60) {
            System.out.println("Small improvements in your daily choices will compound significantly.");
            System.out.println("Over months and years, these incremental changes will transform");
            System.out.println("your academic performance and personal development trajectory.");
        } else if (avgScore >= 40) {
            System.out.println("Your habits are developing. Focus on steady improvements in the weakest area.");
            System.out.println("Consistent small changes can improve your academic routine and well-being.");
        } else {
            System.out.println("Urgent intervention is recommended. Current habits, if continued,");
            System.out.println("may lead to compounding negative effects on academic performance,");
            System.out.println("health, and future opportunities. Immediate positive changes are crucial.");
        }

        System.out.println("-".repeat(60));
    }

     boolean askToProceedOrAnalyze() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Continue to next day");
        System.out.println("2. View current analysis");

        int choice = getValidatedIntInput(1, 2, "Enter your choice (1-2): ");
        return choice == 1;
    }
    void runSimulation() {
        displayWelcome();

        studentName = readStudentName();
        selectDuration();
        initializeScenarios();

        // initialize tracking arrays now that totalDays is known
        choiceHistory = new int[totalDays];
        dailyScores = new int[totalDays][HABIT_COUNT];
        streakBreaks = 0;
        int previousStreak = 0;

        // main simulation loop
        for (int day = 1; day <= totalDays; day++) {
            int scenarioIndex = (day - 1) % 7;
            Scenario currentScenario = scenarios[scenarioIndex];

            currentScenario.displayScenario(day);
            int userChoice = getUserChoice(currentScenario.options.length);

            // store choice in history
            choiceHistory[day - 1] = userChoice;

            // Apply the selected scenario impacts.
            int[] impacts = currentScenario.getImpacts(userChoice);
            applyImpacts(impacts);

            // snapshot scores after this day
            for (int h = 0; h < HABIT_COUNT; h++) {
                dailyScores[day - 1][h] = habits[h].score;
            }

            // Update the positive-response streak.
            boolean isPositive = currentScenario.isPositiveChoice(userChoice);
            updateStreak(isPositive);

            // detect streak break: if streak just reset to 0 and was positive before
            if (!isPositive && previousStreak > 0) {
                streakBreaks++;
            }
            previousStreak = currentStreak;

            // Allow the student to view progress before continuing.
            if (day < totalDays) {
                boolean proceed = askToProceedOrAnalyze();
                if (!proceed) {
                    displayCurrentAnalysis(day);
                }
            }
        }

        // Display the final analysis.
        generateFinalVerdict();
    }

    String readStudentName() {
        while (true) {
            System.out.print("\n  Enter student name (maximum 30 characters): ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) return "Anonymous Student";
            if (name.length() <= 30) return name;
            System.out.println("  Name is too long. Please use 30 characters or fewer.");
        }
    }
    StudentRecord buildRecord(String studentId) {
        int[] scores = new int[HABIT_COUNT];
        for (int i = 0; i < HABIT_COUNT; i++) {
            scores[i] = habits[i].score;
        }

        return new StudentRecord(
                studentId,
                studentName,
                totalDays,
                scores,
                longestStreak,
                currentStreak,
                streakBreaks,
                choiceHistory,
                dailyScores
        );
    }

    // resets everything so a fresh student can run next

    void resetForNextStudent() {
        initializeHabits();         // reset all 4 habits to 50
        currentStreak = 0;
        longestStreak = 0;
        streakBreaks = 0;
        studentName = "";
        choiceHistory = null;
        dailyScores = null;
    }
}
