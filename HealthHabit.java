class HealthHabit extends Habit {
     HealthHabit(int initialScore) {
        super("Health and Energy", initialScore);
    }

    @Override
     String getHabitType() {
        return "Wellness Habit";
    }
}
