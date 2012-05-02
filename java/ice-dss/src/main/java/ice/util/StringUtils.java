package ice.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Provides a class to store commonly used string utility functions.
 *
 * @author HLN Consulting, LLC
 */
public class StringUtils {

    /**
     * Check is a string is empty.
     *
     * @param p_input
     * @return
     */
    public static boolean isEmpty(String p_input) {
        return (p_input == null || p_input.trim().length() == 0);
    }

    /**
     * Check if a string is numeric.
     *
     * @param p_input
     * @return
     */
    public static boolean isNumeric(String p_input) {
        if (isEmpty(p_input)) {
            return false;
        }
        boolean ret = true;  // until proven False
        for (int i = 0; ret && i < p_input.length(); i++) {
            char c = p_input.charAt(i);
            if (!Character.isDigit(c)) {
                ret = false;
            }
        }
        return ret;
    }

    /**
     * Return a hash from a plain text string.
     *
     * @param string
     * @return
     */
    public static String getShaHashFromString(String string) {
        String hash = "";
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(string.getBytes());
            byte[] byteData = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException na) {
            na.printStackTrace();
        }
        return hash;
    }

    /**
     * Returns a 32 character hexadecimal random string.
     *
     * @return
     */
    public static String getHashId() {
        return getHashId(32);
    }

    /**
     * Return a random string of a particular length.
     *
     * @param length
     * @return
     */
    public static String getHashId(int length) {
        MessageDigest md;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append(Integer.toHexString(random.nextInt()));
        }
        return sb.toString().substring(0, length);
    }

    public static String getStringFromArray(String delimiter, Object... objectArray) {
        String[] stringArray = new String[objectArray.length];
        int i = 0;
        for (Object object : objectArray) {
            if (object != null) {
                stringArray[i] = object.toString();
            } else {
                stringArray[i] = null;
            }
            i++;
        }
        return getStringFromArray(stringArray, delimiter);
    }

    public static String getStringFromArray(String delimiter, String... stringArray) {
        return getStringFromArray(stringArray, delimiter);
    }

    public static String getStringFromArray(String[] stringArray, String delimiter) {
        List<String> arrayList = new ArrayList();
        Collections.addAll(arrayList, stringArray);
        return getStringFromArray(arrayList, delimiter);
    }

    /**
     * Return a string from a String array.
     *
     * @param stringArrayList
     * @param delimiter
     * @return String
     */

    public static String getStringFromArray(List<String> stringArrayList, String delimiter ) {
        StringBuilder retValue = new StringBuilder();
        for (String string : stringArrayList) {
            if (string == null) {
                retValue.append("(empty)");
            } else {
                retValue.append(string);
            }
            retValue.append(delimiter);
        }
        return retValue.length() == 0 ? "" : retValue.substring(0, retValue.length()-delimiter.length());
    }

}