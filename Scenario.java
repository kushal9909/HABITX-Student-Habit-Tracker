class Scenario {
    String description;
    String[] options;
    int[][] impacts;

     Scenario(String description, String[] options, int[][] impacts) {
        if (description == null || options == null || impacts == null || options.length == 0 || options.length != impacts.length) {
            throw new IllegalArgumentException("Scenario options and impacts must be non-empty and have matching lengths.");
        }
        for (int i = 0; i < impacts.length; i++) {
            if (impacts[i] == null || impacts[i].length != 4) {
                throw new IllegalArgumentException("Each scenario option must contain exactly four habit impacts.");
            }
        }
        this.description = description;
        this.options = options;
        this.impacts = impacts;
    }

     void displayScenario(int dayNumber) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DAY " + dayNumber);
        System.out.println("=".repeat(60));
        System.out.println(description);
        System.out.println("\nYour Options:");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
    }

     int[] getImpacts(int choice) {
        if (choice >= 0 && choice < impacts.length) {
            int[] copy = new int[impacts[choice].length];
            for (int i = 0; i < copy.length; i++) copy[i] = impacts[choice][i];
            return copy;
        }
        return new int[4];
    }

     boolean isPositiveChoice(int choice) {
        if (choice >= 0 && choice < impacts.length) {
            int[] impact = impacts[choice];
            int totalImpact = 0;
            for (int i = 0; i < impact.length; i++) totalImpact += impact[i];
            return totalImpact > 0;
        }
        return false;
    }
}
