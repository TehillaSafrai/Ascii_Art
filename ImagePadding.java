package image;

import java.awt.*;

/**
 * The ImagePadding class is responsible for padding an Image to a power of 2 dimensions.
 * It ensures that the provided Image is surrounded by a border of white pixels, making its dimensions
 * a power of 2. This is useful for certain algorithms or processing steps that require power-of-2 dimensions.
 */
public class ImagePadding {

    /**
     * Pads the given Image to a power of 2 dimensions.
     *
     * @param image The input Image to be padded.
     * @return A new Image with dimensions padded to the nearest power of 2.
     */
    public static Image paddedImage(Image image) {
        int paddedImageHeight = paddedImageDim(image.getHeight());
        int paddedImageWidth = paddedImageDim(image.getWidth());
        Color[][] pixelArray = new Color[paddedImageHeight][paddedImageWidth];

        for (int i = 0; i < paddedImageHeight; i++) {
            for (int j = 0; j < paddedImageWidth; j++) {
                if (i < (paddedImageHeight - image.getHeight()) / 2 ||
                        i >= image.getHeight() + (paddedImageHeight - image.getHeight()) / 2 ||
                        j < (paddedImageWidth - image.getWidth()) / 2 ||
                        j >= image.getWidth() + (paddedImageWidth - image.getWidth()) / 2) {
                    pixelArray[i][j] = Color.WHITE;
                } else {
                    pixelArray[i][j] =
                            image.getPixel(i - (paddedImageHeight - image.getHeight()) / 2,
                                    j - (paddedImageWidth - image.getWidth()) / 2);
                }
            }
        }

        return new Image(pixelArray, paddedImageWidth, paddedImageHeight);
    }

    /**
     * Calculates the nearest power of 2 dimension for an image dimension.
     *
     * @param imageDim The original dimension of the image.
     * @return The nearest power of 2 dimension.
     */
    private static int paddedImageDim(int imageDim) {
        int logBase2 = 1;
        while (logBase2 < imageDim) {
            logBase2 *= 2;
        }
        return logBase2;
    }
}
