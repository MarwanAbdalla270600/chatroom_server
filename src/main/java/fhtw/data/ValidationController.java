package fhtw.data;

import fhtw.user.User;


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

        if (!DatabaseHandler.getRegisteredUsers().containsKey(user.getUsername())) {
            DatabaseHandler.getRegisteredUsers().put(user.getUsername(), user);
            System.out.println("New User " + user.getUsername() + " successfully registered");
            return true;
        }

        System.out.println("Username already exists, please choose a other name");
        return false;
    }

    public static boolean checkLogin(User user) {
        return loginUsernameIsCorrect(user) && loginPasswordIsCorrect(user);
    }

    private static boolean loginPasswordIsCorrect(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) return false;
        User registeredUser = DatabaseHandler.getRegisteredUsers().get(user.getUsername());
        if (!registeredUser.getPassword().equals(user.getPassword())) {
            System.out.println("pw wrong, try again");
            return false;
        }
        return true;
    }

    private static boolean loginUsernameIsCorrect(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) return false;
        if (!DatabaseHandler.getRegisteredUsers().containsKey(user.getUsername())) {
            System.out.println("Username not correct or not registered, try again");
            return false;
        }
        return true;
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

    private static boolean isValidPassword(String password) {
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

}
