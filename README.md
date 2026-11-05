# HABITX — Student Habit Analysis System

HABITX is a Java console application that uses student-life scenarios to demonstrate how everyday decisions affect four habit scores: Study Discipline, Focus and Attention, Health and Energy, and Time Management.

## Features

### Student Mode
- Complete a tracking simulation from 1 to 100 days.
- Respond to seven student-life scenarios that repeat in sequence.
- View habit scores, streaks, grades and improvement suggestions.
- Compare results using anonymous peer labels. Student names are hidden in Student Mode.

### Mentor Mode
- View student records and individual performance details.
- Review daily choices and score history.
- Search by partial name or student ID.
- Review students by performance and intervention priority.
- See actual student names. Demonstration records are marked `DEMO`.

## Built with

- Core Java; no external libraries
- Classes, objects, constructors and methods
- Inheritance and method overriding
- Arrays, loops, conditional statements and sorting/searching

The application stores up to 100 student records in an array. It does not use a database or permanent file storage.

## Requirements and run instructions

Requires JDK 11 or later.

Compile and run:

```bash
javac *.java
java HabitXSystem
```

To load sample records for Mentor Mode:

```bash
java HabitXSystem --demo-data
```

Demo records are generated using the same scenario rules as Student Mode and are marked `DEMO` in Mentor Mode. Without the argument, the application starts with an empty database.

Records are stored in memory and are lost when the application exits.

## Scoring rules

- Each habit starts at 50 and remains between 0 and 100.
- Raw average is the mean of the four habit scores.
- Weighted score: Study 30%, Focus 25%, Health 15%, Time Management 30%.
- Grade bands: Excellent 80–100, Good 60–79.9, Average 40–59.9, Poor 20–39.9, Critical below 20.
- Peer comparison ranks students by weighted score and uses anonymous labels in Student Mode.

## Project structure

```text
HABITX/
├── BaseHabit.java
├── Habit.java
├── StudyHabit.java
├── FocusHabit.java
├── HealthHabit.java
├── TimeManagementHabit.java
├── Scenario.java
├── StudentRecord.java
├── StudentDatabase.java
├── WeightedScoreCalculator.java
├── PeerComparison.java
├── MentorMenu.java
├── HabitXEngine.java
├── HabitXSystem.java
├── README.md
├── .gitignore
└── LICENSE
```

## Limitations

- Student records are not saved after the application closes.
- The in-memory database supports up to 100 records per run.
- Seven scenarios repeat for tracking periods longer than seven days.
- Scores are illustrative outputs based on predefined rules, not validated measures of real-world behaviour.
