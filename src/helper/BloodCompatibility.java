package helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BloodCompatibility {
    public static final HashMap<String, ArrayList<String>> canReceiveFrom = new HashMap<>();
    public static final HashMap<String, ArrayList<String>> canDonateTo = new HashMap<>();

    static {
        canReceiveFrom.put("A+", new ArrayList<>(List.of("A+", "A-", "O+", "O-")));
        canReceiveFrom.put("A-", new ArrayList<>(List.of("A-", "O-")));
        canReceiveFrom.put("B+", new ArrayList<>(List.of("B+", "B-", "O+", "O-")));
        canReceiveFrom.put("B-", new ArrayList<>(List.of("B-", "O-")));
        canReceiveFrom.put("AB+", new ArrayList<>(List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")));
        canReceiveFrom.put("AB-", new ArrayList<>(List.of("A-", "B-", "AB-", "O-")));
        canReceiveFrom.put("O+", new ArrayList<>(List.of("O+", "O-")));
        canReceiveFrom.put("O-", new ArrayList<>(List.of("O-")));

        canDonateTo.put("A+", new ArrayList<>(List.of("A+", "AB+")));
        canDonateTo.put("A-", new ArrayList<>(List.of("A+", "A-", "AB+", "AB-")));
        canDonateTo.put("B+", new ArrayList<>(List.of("B+", "AB+")));
        canDonateTo.put("B-", new ArrayList<>(List.of("B+", "B-", "AB+", "AB-")));
        canDonateTo.put("AB+", new ArrayList<>(List.of("AB+")));
        canDonateTo.put("AB-", new ArrayList<>(List.of("AB+", "AB-")));
        canDonateTo.put("O+", new ArrayList<>(List.of("A+", "B+", "O+", "AB+")));
        canDonateTo.put("O-", new ArrayList<>(List.of("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")));
    }
}
