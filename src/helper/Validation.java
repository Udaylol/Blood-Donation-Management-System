package helper;

public interface Validation {

     static boolean isValidName(String name) {
        if (name == null || name.length() < 2 || name.length() > 50) return false;
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') return false;
        }
        return true;
    }

     static boolean isValidAge(int age) {
        return (age >= 18 && age <= 65);
    }

     static boolean isValidBloodGroup(String bloodGroup) {
        if (bloodGroup == null) return false;
        String[] validGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String group : validGroups) {
            if (group.equalsIgnoreCase(bloodGroup)) return true;
        }
        return false;
    }

     static boolean isValidCity(String city) {
        if (city == null || city.length() < 2 || city.length() > 50) return false;
        for (char c : city.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') return false;
        }
        return true;
    }

     static boolean isValidContact(String contact) {
        if (contact == null || contact.length() != 10) return false;
        for (char c : contact.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
