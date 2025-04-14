package business.model.employee;

public enum Sex {
    MALE,FEMALE,OTHER;
    public static boolean isValid(String value) {
        for (Sex sex : values()) {
            if (sex.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
