class TimeManagementHabit extends Habit {
     TimeManagementHabit(int initialScore) {
        super("Time Management", initialScore);
    }

    @Override
     String getHabitType() {
        return "Academic Habit";
    }
}
