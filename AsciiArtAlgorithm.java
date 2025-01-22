package ascii_art;

import image.Image;
import image_char_matching.SubImagesControl;
import image_char_matching.SubImgCharMatcher;

/**
 * The AsciiArtAlgorithm class is responsible for converting an Image object
 * into ASCII art using a given resolution and a SubImgCharMatcher for character matching.
 * It utilizes the SubImagesControl class to transform the image into a 2D array of brightness values.
 * The ASCII art result is stored in a 2D char array.
 */
public class AsciiArtAlgorithm {
    /**
     * Resolution of the ASCII art grid
     */
    private final int resolution;
    /**
     * 2D array to store brightness values of sub-images
     */
    private final double[][] subImagesBrightnessList;

    /**
     * SubImgCharMatcher for character matching
     */
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * Constructs an AsciiArtAlgorithm object with the given Image, resolution, and SubImgCharMatcher.
     *
     * @param image               The Image object to be converted to ASCII art.
     * @param resolution          The resolution of the ASCII art grid.
     * @param subImgCharMatcher   The SubImgCharMatcher used for character matching.
     */
    public AsciiArtAlgorithm(Image image, int resolution, SubImgCharMatcher subImgCharMatcher) {
        this.resolution = resolution;
        // Transform the image into a 2D array of brightness values
        subImagesBrightnessList = SubImagesControl.imageToBrightnessArray(image, resolution);
        this.subImgCharMatcher = subImgCharMatcher;
    }

    /**
     * Runs the ASCII art conversion algorithm and returns the result as a 2D char array.
     *
     * @return A 2D char array representing the ASCII art.
     */
    public char[][] run() {
        char[][] asciiArt = new char[resolution][resolution];
        // Iterate through each pixel in the ASCII art grid
        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                // Get the character based on the brightness value using the SubImgCharMatcher
                asciiArt[i][j] = subImgCharMatcher.getCharByImageBrightness(subImagesBrightnessList[i][j]);
            }
        }
        return asciiArt;
    }
}