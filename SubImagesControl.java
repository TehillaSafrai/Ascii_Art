package image_char_matching;

import image.Image;
import image.ImagePadding;

import java.awt.*;
/**
 * The SubImagesControl class is responsible for processing an image into a matrix of brightness values,
 * divided into sub-images. It provides methods to convert an Image object into a 2D array of brightness
 * values
 * based on a specified resolution.
 */
public class SubImagesControl {
    /**
     * The maximum value for each color component in an RGB pixel.
     */
    private static final double MAX_RGB = 255;

    /**
     * Private default constructor to prevent instantiation of the class.
     */
    private SubImagesControl(){}
    /**
     * Converts an Image object into a matrix of brightness values, divided into sub-images based on
     * the specified resolution.
     *
     * @param image      The input Image object.
     * @param resolution The resolution value, indicating the number of divisions for both width and height.
     * @return A 2D array of brightness values for the sub-images.
     */
    public static double[][] imageToBrightnessArray(image.Image image, int resolution){
        image = ImagePadding.paddedImage(image);
        image.Image[][] dividedImage = divideImage(image, resolution);
        return brightnessOfSubImages(dividedImage);
    }

    /**
     * Divides an image into sub-images based on the specified resolution.
     *
     * @param image      The input Image object.
     * @param resolution The resolution value, indicating the number of divisions for both width and height.
     * @return A 2D array of Image objects representing the sub-images.
     */
    private static image.Image[][] divideImage(image.Image image, int resolution) {
        int subImageWidth = image.getWidth() / resolution;
        int subImageHeight = image.getHeight() / resolution;
        image.Image[][] dividedImage = new image.Image[resolution][resolution];
        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                Color[][] subImagePixels = new Color[subImageHeight][subImageWidth];
                for (int rowSubImage = 0; rowSubImage < subImageHeight; rowSubImage++) {
                    for (int colSubImage = 0; colSubImage < subImageWidth; colSubImage++) {
                        subImagePixels[rowSubImage][colSubImage] = image.getPixel(i * subImageHeight +
                                rowSubImage, j * subImageWidth + colSubImage);
                    }
                }
                dividedImage[i][j] = new image.Image(subImagePixels, subImageWidth, subImageHeight);
            }
        }
        return dividedImage;
    }


    /**
     * Calculates the average brightness of each sub-image in the matrix.
     *
     * @param dividedImage A 2D array of Image objects representing sub-images.
     * @return A 2D array of average brightness values for each sub-image.
     */
    private static double[][] brightnessOfSubImages(image.Image[][] dividedImage) {
        double[][] brightnessOfSubImages = new double[dividedImage.length][dividedImage[0].length];
        for (int i = 0; i < dividedImage.length; i++) {
            for (int j = 0; j < dividedImage[0].length; j++) {
                brightnessOfSubImages[i][j] =
                        averageOfImageBrightness(dividedImage[i][j]);
            }
        }
        return brightnessOfSubImages;
    }

    /**
     * Calculates the average brightness of an individual image.
     *
     * @param image The input Image object.
     * @return The average brightness value of the image.
     */
    private static double averageOfImageBrightness(Image image){
        double sumOfGreyShades = 0;
        for (int pixelRow = 0; pixelRow < image.getHeight();
             pixelRow++) {
            for (int pixelCol = 0; pixelCol < image.getWidth();
                 pixelCol++) {
                Color colorOfPixel = image.getPixel(pixelRow, pixelCol);
                sumOfGreyShades +=
                        colorOfPixel.getRed() * 0.2126 + colorOfPixel.getGreen() * 0.7152 +
                                colorOfPixel.getBlue() * 0.0722;
            }
        }
        return (sumOfGreyShades / (image.getHeight() * image.getWidth() * MAX_RGB));
    }

}
