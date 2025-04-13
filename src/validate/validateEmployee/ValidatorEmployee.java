package validate.validateEmployee;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

public class ValidatorEmployee {

    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidEmployeeId(String id, Set<String> existingIds) {
        return id != null
                && !id.trim().isEmpty()
                && id.startsWith("E")
                && id.length() == 5
                && !existingIds.contains(id.trim());
    }

    public static boolean isValidEmployeeName(String name) {
        return name != null
                && !name.trim().isEmpty()
                && name.length() >= 15
                && name.length() <= 150;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])[0-9]{7}$");
    }

    public static boolean isPositiveInteger(int value) {
        return value > 0;
    }

    public static boolean isPositiveDouble(double value) {
        return value > 0;
    }

    public static boolean isValidDate(String date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static LocalDate parseDate(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, formatter);
    }
}
