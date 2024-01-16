package fhtw.data;

import fhtw.user.User;

import java.util.logging.Logger;


public class ValidationController {
    private static final Logger logger = Logger.getLogger(ValidationController.class.getName());

    public static boolean registerNewUser(User user) {
        if (!isValidUsername(user.getUsername())) {
            logger.warning("Username invalid. Usernames must be between 3 and 25 characters long, and contain only " +
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
            logger.info("New User " + user.getUsername() + " successfully registered");
            return true;
        }

        logger.warning("Username already exists, please choose a other name");
        return false;
    }

    public static User findUser(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        if (DatabaseHandler.getRegisteredUsers().containsKey(username)) {
            User searchedUser = DatabaseHandler.getRegisteredUsers().get(username);
            return searchedUser;
        }
        return null;
    }

    public static boolean checkLogin(User user) {
        return loginUsernameIsCorrect(user) && loginPasswordIsCorrect(user);
    }

    private static boolean loginPasswordIsCorrect(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.warning("Password is null or empty");
            return false;
        }
        User registeredUser = DatabaseHandler.getRegisteredUsers().get(user.getUsername());
        if (registeredUser == null) {
            logger.warning("No registered user found for username: " + user.getUsername());
            return false;
        }
        if (!registeredUser.getPassword().equals(user.getPassword())) {
            logger.warning("Password incorrect for user: " + user.getUsername());
            return false;
        }
        return true;
    }

    private static boolean loginUsernameIsCorrect(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            logger.warning("Username is null or empty");
            return false;
        }
        if (!DatabaseHandler.getRegisteredUsers().containsKey(user.getUsername())) {
            logger.warning("Username not correct or not registered: " + user.getUsername());
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

}//end
