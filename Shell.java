package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImagePadding;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.*;

/**
 * The Shell class represents a command-line interface for managing ASCII art generation and customization.
 * It allows users to interactively input commands for adjusting parameters such as character set, image,
 * output method, and resolution. The Shell utilizes various classes such as AsciiArtAlgorithm, Image,
 * SubImgCharMatcher, and different output methods for creating and displaying ASCII art.
 */
public class Shell {
    /**
     * error message: illegal resolution boundaries
     */
    private static final String EXCEEDING_BOUNDARIES_ERROR = "Did not change resolution due to" +
            " exceeding boundaries.";
    /**
     * error message: illegal resolution.
     */
    private static final String FORMAT_RESOLUTION_ERROR = "Did not change resolution due to incorrect " +
            "format.";
    /**
     * error message: illegal Charset. can't run.
     */
    private static final String EMPTY_CHARSET_ERROR = "Did not execute. Charset is empty.";
    /**
     * massage: resolution print.
     */
    private static final String RESOLUTION_MESSAGE = "Resolution set to ";
    /**
     * error message: illegal image path.
     */
    private static final String IMAGE_FILE_ERROR = "Did not execute due to problem with image file.";
    /**
     * error message: illegal output command.
     */
    private static final String OUTPUT_METHOD_ERROR = "Did not change output method due to incorrect format.";
    /**
     * error message: illegal command.
     */
    private static final String INCORRECT_COMMAND_ERROR = "Did not execute due to incorrect command.";
    /**
     * error message: illegal add command.
     */
    private static final String ADD_ERROR = "Did not add due to incorrect format.";
    /**
     * error message: illegal remove command.
     */
    private static final String REMOVE_ERROR = "Did not remove due to incorrect format.";
    /**
     * print massage
     */
    private static final String ENTER_INPUT = ">>> ";
    /**
     * exit command.
     */
    private static final String EXIT_COMMAND = "exit";
    /**
     * show all charset command
     */
    private static final String CHARS_COMMAND = "chars";
    /**
     * add a char command
     */
    private static final String ADD_COMMAND = "add";
    /**
     * remove a char command
     */
    private static final String REMOVE_COMMAND = "remove";
    /**
     * change resolution command
     */
    private static final String RESIZE_COMMAND = "res";
    /**
     * empty string to use in replacement
     */
    private static final String EMPTY_STRING = "";
    /**
     * change image path command
     */
    private static final String IMAGE_COMMAND = "image";
    /**
     * create an asciiArt command
     */
    private static final String CREATE_ASCII_ART_COMMAND = "asciiArt";
    /**
     * change output method command
     */
    private static final String OUTPUT_METHOD_COMMAND = "output";
    /**
     * space string to add
     */
    private static final String SPACE_STRING = " ";
    /**
     * use console output command
     */
    private static final String CONSOLE_COMMAND = "console";
    /**
     * use html output command
     */
    private static final String HTML_COMMAND = "html";
    /**
     * default html output address
     */
    private static final String DEFAULT_HTML_FILE_ADDRESS = "out.html";
    /**
     * default html font
     */
    private static final String DEFAULT_HTML_FONT = "Courier New";
    /**
     * up the resolution command
     */
    private static final String RESOLUTION_UP_COMMAND = "up";
    /**
     * bring the resolution down command
     */
    private static final String RESOLUTION_DOWN_COMMAND = "down";
    /**
     * a dot
     */
    private static final String DOT = ".";
    /**
     * add or remove all chars to charset
     */
    private static final String ADD_OR_REMOVE_ALL_LETTERS_COMMAND = "all";
    /**
     * add or remove a space command
     */
    private static final String ADD_OR_REMOVE_SPACE_COMMAND = "space";
    /**
     * range sign to add or remove a range of chars
     */
    private static final char RANGE_SIGN = '-';
    /**
     * space as a char
     */
    private static final Character SPACE_CHAR = ' ';
    /**
     * default image path
     */
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";

    /**
     * resolution and the default resolution
     */
    private int resolution = 128;
    /**
     * the updating charset
     */
    private static final Set<Character> CHARSET = new HashSet<>();
    /**
     * console as a string
     */
    private String console = "console";
    /**
     * the current image
     */
    private Image image;
    /**
     * the current AsciiArtAlgorithm
     */
    private AsciiArtAlgorithm asciiArtAlgorithm;
    /**
     * the current SubImgCharMatcher
     */
    private SubImgCharMatcher subImgCharMatcher;

    /**
     * The main method to initiate and run the ASCII art Shell.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run();
    }

    /**
     * Runs the ASCII art Shell, allowing users to interactively input commands and manage ASCII
     * art parameters.
     */
    public void run() {
        if (!prepareAlgorithm()) {
            return;
        }
        System.out.print(ENTER_INPUT);
        String userInput = KeyboardInput.readLine();
        while (!userInput.equals(EXIT_COMMAND)) {
            try {
                readUserInput(userInput);
            } catch (IncorrectInputException incorrectInputException) {
                System.out.println(incorrectInputException.getMessage());
            }
            System.out.print(ENTER_INPUT);
            userInput = KeyboardInput.readLine();
        }
    }

    /**
     * Prepares the ASCII art algorithm by initializing the character set, setting the default image,
     * and creating the necessary objects for ASCII art generation.
     *
     * @return true if the preparation was successful, false otherwise.
     */
    private boolean prepareAlgorithm() {
        initializeCharset();
        try {
            changeImage(DEFAULT_IMAGE_PATH);
        } catch (IncorrectInputException incorrectInputException) {
            System.out.println(incorrectInputException.getMessage());
            return false;
        }
        image = ImagePadding.paddedImage(image);
        subImgCharMatcher = new SubImgCharMatcher(charsetToArray());
        createAsciiArtAlgorithm();
        return true;
    }

    /**
     * Converts the character set to a char array for use in the ASCII art algorithm.
     *
     * @return A char array representing the character set.
     */
    private char[] charsetToArray() {
        char[] charArray = new char[CHARSET.size()];
        int i = 0;
        for (char c : CHARSET) {
            charArray[i] = c;
            i++;
        }
        return charArray;
    }

    /**
     * Creates the ASCII art algorithm with the current image, resolution, and character matcher.
     */
    private void createAsciiArtAlgorithm() {
        asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution, subImgCharMatcher);
    }

    /**
     * Initializes the default character set with ASCII digits (0-9).
     */
    private void initializeCharset() {
        for (char i = 48; i < 58; i++) {
            CHARSET.add(i);
        }
    }

    /**
     * Reads and interprets the user input, executing the corresponding action.
     *
     * @param userInput The user input string to be processed.
     * @throws IncorrectInputException If the input is incorrect or not recognized.
     */
    private void readUserInput(String userInput) throws IncorrectInputException {
        String[] tokens = userInput.split(" ", 2);
        if (tokens[0].equals(CHARS_COMMAND)) {
            printCharArray();
        } else if (tokens[0].equals(ADD_COMMAND)) {
            addChars(userInput.replace(ADD_COMMAND + SPACE_STRING, EMPTY_STRING));
        } else if (tokens[0].equals(REMOVE_COMMAND)) {
            removeChars(userInput.replace(REMOVE_COMMAND + SPACE_STRING, EMPTY_STRING));
        } else if (tokens[0].equals(RESIZE_COMMAND)) {
            resChange(userInput.replace(RESIZE_COMMAND + SPACE_STRING, EMPTY_STRING));
        } else if (tokens[0].equals(IMAGE_COMMAND)) {
            changeImage(userInput.replace(IMAGE_COMMAND + SPACE_STRING, EMPTY_STRING));
        } else if (tokens[0].equals(OUTPUT_METHOD_COMMAND)) {
            changeOutput(userInput.replace(OUTPUT_METHOD_COMMAND + SPACE_STRING, EMPTY_STRING));
        } else if (tokens[0].equals(CREATE_ASCII_ART_COMMAND)) {
            runAsciiArt();
        } else {
            throw new IncorrectInputException(INCORRECT_COMMAND_ERROR);
        }
    }

    /**
     * Changes the output method for displaying ASCII art (console or HTML).
     *
     * @param userInput The user input specifying the desired output method.
     * @throws IncorrectInputException If the input format is incorrect.
     */
    private void changeOutput(String userInput) throws IncorrectInputException {
        if (userInput.equals(HTML_COMMAND)) {
            console = HTML_COMMAND;
        } else if (userInput.equals(CONSOLE_COMMAND)) {
            console = CONSOLE_COMMAND;
        } else {
            throw new IncorrectInputException(OUTPUT_METHOD_ERROR);
        }
    }

    /**
     * Changes the current image to a new image specified by the user.
     *
     * @param imagePath The file path of the new image.
     * @throws IncorrectInputException If there is an issue with the image file.
     */
    private void changeImage(String imagePath) throws IncorrectInputException {
        try {
            image = new Image(imagePath);
        } catch (IOException ioException) {
            throw new IncorrectInputException(IMAGE_FILE_ERROR);
        }
        createAsciiArtAlgorithm();
    }

    /**
     * Executes the process of generating and displaying ASCII art based on the current settings.
     *
     * @throws IncorrectInputException If the character set is empty.
     */
    private void runAsciiArt() throws IncorrectInputException {
        if (CHARSET.isEmpty()) {
            throw new IncorrectInputException(EMPTY_CHARSET_ERROR);
        }
        if (console.equals(CONSOLE_COMMAND)) {
            new ConsoleAsciiOutput().out(asciiArtAlgorithm.run());
        }
        if (console.equals(HTML_COMMAND)) {
            new HtmlAsciiOutput(DEFAULT_HTML_FILE_ADDRESS, DEFAULT_HTML_FONT).out(asciiArtAlgorithm.run());
        }
    }

    /**
     * Changes the resolution of the ASCII art by increasing or decreasing it.
     *
     * @param userInput The user input specifying the desired resolution change.
     * @throws IncorrectInputException If the input format is incorrect or the resolution change
     * exceeds boundaries.
     */
    private void resChange(String userInput) throws IncorrectInputException {
        int maxResolution = image.getWidth();
        int minResolution = Math.max(1, image.getWidth() / image.getHeight());
        if (userInput.equals(RESOLUTION_UP_COMMAND)) {
            if (resolution * 2 > maxResolution) {
                throw new IncorrectInputException(EXCEEDING_BOUNDARIES_ERROR);
            }
            resolution *= 2;
            System.out.println(RESOLUTION_MESSAGE + resolution + DOT);
        } else if (userInput.equals(RESOLUTION_DOWN_COMMAND)) {
            if (resolution / 2 < minResolution) {
                throw new IncorrectInputException(EXCEEDING_BOUNDARIES_ERROR);
            }
            resolution /= 2;
            System.out.println(RESOLUTION_MESSAGE + resolution + DOT);
        } else {
            throw new IncorrectInputException(FORMAT_RESOLUTION_ERROR);
        }
        createAsciiArtAlgorithm();
    }

    /**
     * Removes characters from the character set based on user input.
     *
     * @param userInput The user input specifying characters to be removed.
     * @throws IncorrectInputException If the input format is incorrect.
     */
    private void removeChars(String userInput) throws IncorrectInputException {
        if (userInput.length() == 1) {
            CHARSET.remove(userInput.charAt(0));
            subImgCharMatcher.removeChar(userInput.charAt(0));
        } else if (userInput.equals(ADD_OR_REMOVE_ALL_LETTERS_COMMAND)) {
            CHARSET.clear();
            subImgCharMatcher = new SubImgCharMatcher(new char[]{});
            createAsciiArtAlgorithm();
        } else if (userInput.equals(ADD_OR_REMOVE_SPACE_COMMAND)) {
            CHARSET.remove(SPACE_CHAR);
            subImgCharMatcher.removeChar(SPACE_CHAR);
        } else if (userInput.length() == 3 && userInput.charAt(1) == RANGE_SIGN) {
            int minimalChar = Math.min(userInput.charAt(0), userInput.charAt(2));
            int maximalChar = Math.max(userInput.charAt(0), userInput.charAt(2));
            for (char i = (char) minimalChar; i < maximalChar + 1; i++) {
                CHARSET.remove(i);
                subImgCharMatcher.removeChar(i);
            }
        } else {
            throw new IncorrectInputException(REMOVE_ERROR);
        }
    }

    /**
     * Prints the current character set in sorted order.
     */
    private void printCharArray() {
        ArrayList<Character> charArray = new ArrayList<>(CHARSET);
        charArray.sort(Comparator.naturalOrder());

        for (char c : charArray) {
            System.out.print(c + SPACE_STRING);
        }
        System.out.println();
    }

    /**
     * Adds characters to the character set based on user input.
     *
     * @param userInput The user input specifying characters to be added.
     * @throws IncorrectInputException If the input format is incorrect.
     */
    private void addChars(String userInput) throws IncorrectInputException {
        if (userInput.length() == 1) {
            CHARSET.add(userInput.charAt(0));
            subImgCharMatcher.addChar(userInput.charAt(0));
        } else if (userInput.equals(ADD_OR_REMOVE_ALL_LETTERS_COMMAND)) {
            for (char i = 32; i < 127; i++) {
                CHARSET.add(i);
                subImgCharMatcher.addChar(i);
            }
        } else if (userInput.equals(ADD_OR_REMOVE_SPACE_COMMAND)) {
            CHARSET.add(SPACE_CHAR);
            subImgCharMatcher.addChar(SPACE_CHAR);
        } else if (userInput.length() == 3 && userInput.charAt(1) == RANGE_SIGN) {
            int minimalChar = Math.min(userInput.charAt(0), userInput.charAt(2));
            int maximalChar = Math.max(userInput.charAt(0), userInput.charAt(2));
            for (char i = (char) minimalChar; i < maximalChar + 1; i++) {
                CHARSET.add(i);
                subImgCharMatcher.addChar(i);
            }
        } else {
            throw new IncorrectInputException(ADD_ERROR);
        }
    }
}
