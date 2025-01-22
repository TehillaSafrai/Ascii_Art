package image_char_matching;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Inspired by, and partly copied from
 * https://github.com/korhner/asciimg/blob/95c7764a6abe0e893fae56b3b6b580e09e1de209/src/main/java/io/
 * korhner/asciimg/image/AsciiImgCache.java
 * described in the blog:
 * https://dzone.com/articles/ascii-art-generator-java
 * Adaptations made by Dan Nirel and again by Rachel Behar.
 * The class converts characters to a binary "image" (2D array of booleans).
 * The CharConverter class is responsible for converting individual characters to a binary "image,"
 * represented as a 2D array of booleans. It utilizes a specified font and pixel resolution to render
 * characters into
 * black-and-white images. This class is inspired by and adapted from the AsciiImgCache class found in the
 * asciimg project on GitHub, with modifications made by Dan Nirel and Rachel Behar.
 */
public class CharConverter {
    /**
     * Factor for adjusting the offset of the rendered character within the image
     */
    private static final double X_OFFSET_FACTOR = 0.2;
    /**
     * Factor for adjusting the offset of the rendered character within the image
     */
    private static final double Y_OFFSET_FACTOR = 0.75;

    /**
     * Default font name
     */
    private static final String FONT_NAME = "Courier New";
    /**
     * Default pixel resolution
     */
    public static final int DEFAULT_PIXEL_RESOLUTION = 16;

    /**
     * Converts the specified character to a binary 2D array of booleans, representing a black-and-white
     * image.
     *
     * @param c The character to be converted.
     * @return A boolean 2D array representing the binary image of the character.
     */
    public static boolean[][] convertToBoolArray(char c) {
        // Render the character to a buffered image
        BufferedImage img = getBufferedImage(c, FONT_NAME, DEFAULT_PIXEL_RESOLUTION);

        // Convert the buffered image to a boolean matrix
        boolean[][] matrix = new boolean[DEFAULT_PIXEL_RESOLUTION][DEFAULT_PIXEL_RESOLUTION];
        for (int y = 0; y < DEFAULT_PIXEL_RESOLUTION; y++) {
            for (int x = 0; x < DEFAULT_PIXEL_RESOLUTION; x++) {
                // Check if the color at the specified pixel is black
                matrix[y][x] = img.getRGB(x, y) == 0;
            }
        }
        return matrix;
    }

    /**
     * Creates a buffered image of the specified character using the given font and pixel resolution.
     *
     * @param c               The character to be rendered.
     * @param fontName        The name of the font to be used.
     * @param pixelsPerRow    The pixel resolution per row for the resulting image.
     * @return A buffered image of the rendered character.
     */
    private static BufferedImage getBufferedImage(char c, String fontName, int pixelsPerRow) {
        String charStr = Character.toString(c);
        Font font = new Font(fontName, Font.PLAIN, pixelsPerRow);

        // Create a buffered image and draw the character onto it
        BufferedImage img = new BufferedImage(pixelsPerRow, pixelsPerRow, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setFont(font);

        // Calculate the offset based on factors
        int xOffset = (int) Math.round(pixelsPerRow * X_OFFSET_FACTOR);
        int yOffset = (int) Math.round(pixelsPerRow * Y_OFFSET_FACTOR);

        // Draw the character on the buffered image
        g.drawString(charStr, xOffset, yOffset);
        return img;
    }
}
