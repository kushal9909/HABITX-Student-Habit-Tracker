// Specialized habit types (Inheritance demonstration)
class StudyHabit extends Habit {
     StudyHabit(int initialScore) {
        super("Study Discipline", initialScore);
    }

    @Override
    String getHabitType() {
        return "Academic Habit";
    }
}
