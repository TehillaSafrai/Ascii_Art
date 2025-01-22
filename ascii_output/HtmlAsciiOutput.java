package ascii_output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Output a 2D array of chars to an HTML file viewable in a web browser.
 * @author Dan Nirel
 * The HtmlAsciiOutput class implements the AsciiOutput interface and is responsible for
 * outputting a 2D array of characters
 * to an HTML file, making it viewable in a web browser. It includes functionalities for
 * creating an HTML document with
 * specific styling and formatting to represent the given character array.
 */
public class HtmlAsciiOutput implements AsciiOutput {
    /**
     * The base line spacing factor used in HTML styling.
     */
    private static final double BASE_LINE_SPACING = 0.8;
    /**
     * The base font size for characters in the HTML document.
     */
    private static final double BASE_FONT_SIZE = 150.0;

    /**
     * font name
     */
    private final String fontName;
    /**
     * the file name
     */
    private final String filename;

    /**
     * Constructor for HtmlAsciiOutput.
     *
     * @param filename The name of the HTML file to be generated.
     * @param fontName The font name to be used for styling the characters in the HTML file.
     */
    public HtmlAsciiOutput(String filename, String fontName) {
        this.fontName = fontName;
        this.filename = filename;
    }

    /**
     * Outputs a 2D array of characters to an HTML file with specific styling for proper rendering
     * in a web browser.
     *
     * @param chars The 2D array of characters to be output to the HTML file.
     */
    @Override
    public void out(char[][] chars) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Writing the HTML structure with inline styling
            writer.write(String.format(
                    "<!DOCTYPE html>\n"+
                            "<html>\n"+
                            "<body style=\""+
                            "\tCOLOR:#000000;"+
                            "\tTEXT-ALIGN:center;"+
                            "\tFONT-SIZE:1px;\">\n"+
                            "<p style=\""+
                            "\twhite-space:pre;"+
                            "\tFONT-FAMILY:%s;"+
                            "\tFONT-SIZE:%frem;"+
                            "\tLETTER-SPACING:0.15em;"+
                            "\tLINE-HEIGHT:%fem;\">\n",
                    fontName, BASE_FONT_SIZE/chars[0].length, BASE_LINE_SPACING));

            // Writing the character array content to the HTML file
            for(int y = 0 ; y < chars.length ; y++) {
                for (int x = 0; x < chars[y].length ; x++) {
                    String htmlRep;
                    // Handling special HTML characters
                    switch(chars[y][x]) {
                        case '<': htmlRep = "&lt;";  break;
                        case '>': htmlRep = "&gt;";  break;
                        case '&': htmlRep = "&amp;"; break;
                        default:  htmlRep = String.valueOf(chars[y][x]);
                    }
                    writer.write(htmlRep);
                }
                writer.newLine();
            }

            // Closing the HTML document structure
            writer.write(
                    "</p>\n"+
                            "</body>\n"+
                            "</html>\n");
        } catch(IOException e) {
            Logger.getGlobal().severe(String.format("Failed to write to \"%s\"", filename));
        }
    }
}

