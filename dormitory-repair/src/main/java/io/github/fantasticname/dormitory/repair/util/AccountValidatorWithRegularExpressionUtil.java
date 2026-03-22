package io.github.fantasticname.dormitory.repair.util;

import java.util.regex.Pattern;

public class AccountValidatorWithRegularExpressionUtil {
    private static final Pattern STUDENT_PATTERN = Pattern.compile("^(3125|3225)\\d{6}$");
    private static final Pattern ADMIN_PATTERN = Pattern.compile("^0025\\d{6}$");

    public static boolean isValidStudentAccount(String account) {
        return STUDENT_PATTERN.matcher(account).matches();
    }

    public static boolean isValidAdminAccount(String account) {
        return ADMIN_PATTERN.matcher(account).matches();
    }
}