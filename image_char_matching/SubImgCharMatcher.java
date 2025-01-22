package image_char_matching;

import java.util.*;

/**
 * The SubImgCharMatcher class is responsible for managing the association between characters and their
 * corresponding brightness values. It provides methods to add, remove, and retrieve characters based on
 * their brightness levels.
 * The class also normalizes the brightness values and ensures a one-to-one mapping between brightness values
 * and characters, facilitating the matching process in ASCII art generation.
 */
public class SubImgCharMatcher {
    /**
     * An empty string constant.
     */
    private static final String EMPTY_CHAR = "";
    /**
     * A TreeMap storing the mapping between brightness values and characters.
     * The brightness values are unnormalized.
     */
    private final TreeMap<Double, String> charToBrightness;
    /**
     * A TreeMap storing the mapping between normalized brightness values and characters.
     */
    private final TreeMap<Double, String> charToBrightnessNormal;
    /**
     * Constructs a SubImgCharMatcher object with an initial character set.
     *
     * @param charset An array of characters representing the initial character set.
     */
    public SubImgCharMatcher (char[] charset){
        charToBrightness = new TreeMap<>();
        charToBrightnessNormal = new TreeMap<>();
        findBrightnessOfCharset(charset);
        normalizeBrightnessOfChars();
    }


    /**
     * Normalizes the brightness values of characters.
     */
    private void normalizeBrightnessOfChars(){
        charToBrightnessNormal.clear();
        if(charToBrightness.isEmpty()){
            return;
        }
        double minBrightness = charToBrightness.firstKey();
        double maxBrightness = charToBrightness.lastKey();
        for (Map.Entry<Double, String> unNormelizedEntry : charToBrightness.entrySet()){
            double newCharBrightness =
                    (unNormelizedEntry.getKey() - minBrightness)/(maxBrightness-minBrightness);
            charToBrightnessNormal.put(newCharBrightness, unNormelizedEntry.getValue());
        }
    }

    /**
     * Finds the brightness values of characters in the provided character set.
     *
     * @param charset An ArrayList of characters representing the character set.
     */
    private void findBrightnessOfCharset(char[] charset){
        for (char c: charset) {
            addCharCommonCode(c);
        }
    }
    /**
     * Finds the brightness value of a single character.
     *
     * @param c The input character.
     * @return The brightness value of the character.
     */
    private double findCharBrightness(char c) {
        boolean[][] charBooleanImage = CharConverter.convertToBoolArray(c);
        int counterWhites = 0;
        for (boolean[] booleans : charBooleanImage) {
            for (int j = 0; j < charBooleanImage[0].length; j++) {
                if (booleans[j]) {
                    counterWhites++;
                }
            }
        }
        return (double) counterWhites / (double)(charBooleanImage.length*charBooleanImage[0].length);
    }

    /**
     * Retrieves the character associated with a given brightness value.
     *
     * @param brightness The brightness value for which to retrieve the corresponding character.
     * @return The character associated with the specified brightness value.
     */
    public char getCharByImageBrightness(double brightness) {
        double floorKeyMinusBrightness = Math.abs(brightness - charToBrightnessNormal.floorKey(brightness));
        double ceilingKeyMinusBrightness =
                Math.abs(brightness - charToBrightnessNormal.ceilingKey(brightness));
        String chars;
        if(floorKeyMinusBrightness < ceilingKeyMinusBrightness){
            chars = charToBrightnessNormal.get(charToBrightnessNormal.floorKey(brightness));
        }else {
            chars = charToBrightnessNormal.get(charToBrightnessNormal.ceilingKey(brightness));
        }
        if(chars.length() == 1){
            return chars.charAt(0);
        }
        String[] split = chars.split(EMPTY_CHAR);
        char minimalAscii = split[0].charAt(0);
        for (String letter: split) {
            if(letter.charAt(0) < minimalAscii ){
                minimalAscii = letter.charAt(0);
            }
        }
        return minimalAscii;
    }

    /**
     * Adds a character to the character set and updates the brightness values.
     *
     * @param c The character to be added.
     */
    public void addChar (char c){
        addCharCommonCode(c);
        normalizeBrightnessOfChars();
    }

    /**
     * Common code for adding a character to the character set and updating brightness values.
     *
     * @param c The character to be added.
     */
    private void addCharCommonCode(char c){
        double charBrightness = findCharBrightness(c);
        if(charToBrightness.containsKey(charBrightness)){
            charToBrightness.put(charBrightness, charToBrightness.get(charBrightness) + c);
        }
        else {
            charToBrightness.put(charBrightness, String.valueOf(c));
        }
    }

    /**
     * Removes a character from the character set and updates the brightness values.
     *
     * @param c The character to be removed.
     */
    public void removeChar (char c){
        double charBrightness = findCharBrightness(c);
        String charsWithSameBrightness = charToBrightness.get(charBrightness);
        if(charsWithSameBrightness == null){
            return;
        }
        if(charToBrightness.get(charBrightness).length() == 1){
            charToBrightness.remove(charBrightness);
            charToBrightnessNormal.remove(charBrightness);
        }
        else {
            charsWithSameBrightness = charsWithSameBrightness.replace(String.valueOf(c),
                        EMPTY_CHAR);
            charToBrightness.put(charBrightness, charsWithSameBrightness);
            charToBrightnessNormal.put(charBrightness, charsWithSameBrightness);
        }
        normalizeBrightnessOfChars();
    }
}
