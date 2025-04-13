package business.model.employee;

public enum Status {
    ACTIVE,INACTIVE,ONLEAVE,POLICYLEAVE;
    public static boolean isValid(String value) {
        for (Status status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
