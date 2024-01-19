package fhtw.data;

import fhtw.user.User;

/**
 * This class provides methods for validating user registration and login credentials.
 * It includes checks for valid usernames, valid passwords, and the correctness of login details.
 * It interacts with the DatabaseHandler to check against existing users and to register new users.
 */
public class ValidationController {

    /**
     * Validates and registers a new user.
     * Checks if the username and password are valid according to the defined criteria.
     * If valid, the user is added to the registered users in the database.
     *
     * @param user the User object containing the registration details
     * @return true if the user is successfully registered, false otherwise
     */
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

    /**
     * Validates the login credentials of a user.
     * Checks if the username exists and if the password matches the registered users password.
     *
     * @param user the User object containing the login details
     * @return true if the login credentials are correct, false otherwise
     */
    public static boolean checkLogin(User user) {
        return loginUsernameIsCorrect(user) && loginPasswordIsCorrect(user);
    }

    /**
     * Checks if the provided password matches the registered users password.
     *
     * @param user the User object containing the login details
     * @return true if the password is correct, false otherwise
     */
    private static boolean loginPasswordIsCorrect(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) return false;
        User registeredUser = DatabaseHandler.getRegisteredUsers().get(user.getUsername());
        if (!registeredUser.getPassword().equals(user.getPassword())) {
            System.out.println("pw wrong, try again");
            return false;
        }
        return true;
    }

    /**
     * Checks if the username is correct and registered.
     *
     * @param user the User object containing the login details
     * @return true if the username is correct and registered, false otherwise
     */
    private static boolean loginUsernameIsCorrect(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) return false;
        if (!DatabaseHandler.getRegisteredUsers().containsKey(user.getUsername())) {
            System.out.println("Username not correct or not registered, try again");
            return false;
        }
        return true;
    }

    /**
     * Validates if the username meets the criteria: 3-25 characters long, and contains only digits and english letters.
     *
     * @param username the username to validate
     * @return true if the username is valid, false otherwise
     */
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

    /**
     * Validates if the password meets the criteria: 6-25 characters long, and must contain both numbers AND digits.
     *
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
     */
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
