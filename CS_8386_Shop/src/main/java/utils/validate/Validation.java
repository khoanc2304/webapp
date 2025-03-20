package utils.validate;

import java.util.regex.Pattern;

public class Validation {
    //
    // Chỉ chứa 3-10 ký tự không dấu
    public static boolean validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        String regex = "^[a-zA-Z0-9]{3,10}$";
        return Pattern.matches(regex, username);
    }

    // Không chứa số
    public static boolean validateFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return false;
        }
        String regex = "^[^0-9]+$";
        return Pattern.matches(regex, fullName);
    }

    // Đảm bảo cuối có đủ @gmail.com
    public static boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        //String regex = "^[\\w._%+-]+@gmail\\.com$";
        String regex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }
}