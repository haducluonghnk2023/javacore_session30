package validate.validateDepartment;

import java.util.Set;

public class ValidatorDepartment {

    // Validate if a string is not null or empty
    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Validate if a number is within a range
    public static boolean isWithinLength(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }

    public static boolean isUniqueDepartmentName(String departmentName, Set<String> existingNames) {
        return departmentName != null && (existingNames == null || !existingNames.contains(departmentName.trim()));
    }

    public static boolean isValidDepartmentName(String name, Set<String> existingNames) {
        boolean basicCheck = isNotNullOrEmpty(name) && isWithinLength(name, 10, 100);
        if (existingNames == null) {
            return basicCheck;
        }
        return basicCheck && isUniqueDepartmentName(name, existingNames);
    }

    public static boolean isValidDepartmentDescription(String description) {
        return description != null && description.length() <= 255;
    }

}
