package fhtw.data;

import fhtw.user.User;

import java.io.Serializable;

public class ValidationController {

    public static boolean registerNewUser(User user) {
        if (!isValidUsername(user.getUsername())) {
            System.out.println("Username invalid. Usernames must be between 3 and 25 characters long, and contain only " +
                    "digits and english letters");
            return false;
        }

        if (!isValidPassword(user.getPassword())) {
            System.out.println("Password invalid. Passwords must be between 6 and 25 characters long and must contain " +
                    "numbers AND digits!");
            return false;
        }
        //denk für gender wirds keine Validierung brauchen, weil man im GUI graphisch nur zwischen weibl./männl. wählt?!

        if (!DatabaseHandler.registeredUsers.containsKey(user.getUsername())) {
            DatabaseHandler.registeredUsers.put(user.getUsername(), user);
            System.out.println("New User " + user.getUsername() + " successfully registered");
            return true;
        }

        System.out.println("Username already exists, please choose a other name");
        return false;
    }


    private static boolean isValidUsername(String username) {
        if (username == null) return false;

        int minLength = 3;
        int maxLength = 25;
        if (username.length() < minLength || username.length() > maxLength) {
            return false;
        }

        char[] lettersUsername = username.toCharArray();
        for (char character : lettersUsername) {
            if (!Character.isLetterOrDigit(character)) return false;
        }
        return true;
    }

    private static boolean isValidPassword(String password) { //checks that pw's have digits AND letters and 6-25 char length
        if (password == null) return false;

        int minLength = 6;
        int maxLength = 25;
        if (password.length() < minLength || password.length() > maxLength) {
            return false;
        }

        boolean containsDigit = false;
        boolean containsLetter = false;
        char[] lettersPassword = password.toCharArray();

        for (char character : lettersPassword) {
            if (Character.isDigit(character)) {
                containsDigit = true;
            } else if (Character.isLetter(character)) {
                containsLetter = true;
            }
        }
        return containsDigit && containsLetter;
    }

}//end
