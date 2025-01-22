package ascii_output;

/**
 * Output a 2D array of chars to the console.
 * @author Dan Nirel
 * The ConsoleAsciiOutput class implements the AsciiOutput interface to provide functionality
 * for outputting a 2D array of characters to the console. It is used in the ASCII art generation
 * process to display the generated ASCII art in the console.
 */
public class ConsoleAsciiOutput implements AsciiOutput {

    /**
     * Outputs a 2D array of characters to the console, with each character separated by a space.
     *
     * @param chars The 2D array of characters to be displayed in the console.
     */
    @Override
    public void out(char[][] chars) {
        for (int y = 0; y < chars.length; y++) {
            for (int x = 0; x < chars[y].length; x++) {
                System.out.print(chars[y][x] + " ");
            }
            System.out.println();
        }
    }
}

