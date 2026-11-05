class FocusHabit extends Habit {
    FocusHabit(int initialScore) {
        super("Focus and Attention", initialScore);
    }

    @Override
    String getHabitType() {
        return "Academic Habit";
    }
}
