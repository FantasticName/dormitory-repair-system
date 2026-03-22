package io.github.fantasticname.dormitory.repair.util;

import java.util.Scanner;

public class ConsoleUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字！");
            }
        }
    }

    public static int readRole(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                Integer role = Integer.parseInt(scanner.nextLine().trim());
                if (role != 0 && role != 1){
                    System.out.println("请按要求输入数字选择角色! 角色 1-学生，2-维修人员");
                }else{
                    return role;
                }

            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字！");
            }
        }
    }

    // 密码输入（不加密，以后再考虑加密）
    public static String readPassword(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}