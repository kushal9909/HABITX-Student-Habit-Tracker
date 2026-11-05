// Stores student records in a fixed-size array (Semester 1 array practice).
class StudentDatabase {
    StudentRecord[] records;
    int count;
    static final int MAX_STUDENTS = 100;

    StudentDatabase() {
        records = new StudentRecord[MAX_STUDENTS];
        count = 0;
    }

    // Returns false when the fixed array has reached its capacity.
    boolean addStudent(StudentRecord record) {
        if (record == null || count >= MAX_STUDENTS) {
            return false;
        }
        records[count] = record;
        count++;
        return true;
    }

    // Returns a sorted copy; the database keeps its original insertion order.
    static StudentRecord[] sortByWeightedScore(StudentDatabase database, boolean descending) {
        StudentRecord[] sorted = new StudentRecord[database.getCount()];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i] = database.getStudent(i);
        }
        for (int i = 0; i < sorted.length - 1; i++) {
            int selected = i;
            for (int j = i + 1; j < sorted.length; j++) {
                boolean comesFirst = descending
                        ? sorted[j].weightedScore > sorted[selected].weightedScore
                        : sorted[j].weightedScore < sorted[selected].weightedScore;
                if (comesFirst) selected = j;
            }
            StudentRecord temp = sorted[i];
            sorted[i] = sorted[selected];
            sorted[selected] = temp;
        }
        return sorted;
    }

    int getCount() { return count; }
    boolean hasData() { return count > 0; }

    StudentRecord getStudent(int index) {
        if (index >= 0 && index < count) return records[index];
        return null;
    }

    String generateNextId() {
        int nextNum = count + 1;
        if (nextNum < 10) return "S00" + nextNum;
        if (nextNum < 100) return "S0" + nextNum;
        return "S" + nextNum;
    }

    // Linear search: partial, case-insensitive name match.
    StudentRecord[] searchByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new StudentRecord[0];
        }
        String lowerQuery = query.trim().toLowerCase();
        int matchCount = 0;
        for (int i = 0; i < count; i++) {
            if (records[i].studentName.toLowerCase().contains(lowerQuery)) matchCount++;
        }
        StudentRecord[] results = new StudentRecord[matchCount];
        int index = 0;
        for (int i = 0; i < count; i++) {
            if (records[i].studentName.toLowerCase().contains(lowerQuery)) {
                results[index] = records[i];
                index++;
            }
        }
        return results;
    }

    // Binary search: IDs are generated sequentially and records are not reordered.
    StudentRecord searchById(String targetId) {
        if (targetId == null || targetId.trim().isEmpty()) return null;
        int left = 0;
        int right = count - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2;
            int comparison = records[middle].studentId.compareToIgnoreCase(targetId.trim());
            if (comparison == 0) return records[middle];
            if (comparison < 0) left = middle + 1;
            else right = middle - 1;
        }
        return null;
    }
}
