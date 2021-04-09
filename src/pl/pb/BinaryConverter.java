package pl.pb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryConverter {
    public static String convertStringToBinary8Bits(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();

    }

    public static String convertStringToBinary4Bits(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%4s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();

    }

    public static String prettyBinary(String binary, int blockSize, String separator) {

        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < binary.length()) {
            result.add(binary.substring(index, Math.min(index + blockSize, binary.length())));
            index += blockSize;
        }
        return String.join(separator, result);
    }

    public static StringBuilder fromBinaryToHex(StringBuilder binaryStr) {
        return new StringBuilder(new BigInteger(binaryStr.toString(), 2).toString(16));
    }

    public static String fromHexToBinary(String hexStr) {
        return new BigInteger(hexStr, 16).toString(2);
    }

    public static String fromBinaryToText(String binaryText) {
        StringBuilder tmp8Bits = new StringBuilder();
        StringBuilder result = new StringBuilder();
        int index = 0;
        for(int i =0; i<8 ;i++){
            for (int j =0; j<8; j++){
                tmp8Bits.append(binaryText.charAt(index++));
            }
            int charCode = Integer.parseInt(tmp8Bits.toString(), 2);
            result.append(Character.toChars((char) charCode));
            DES.cleanStringBuilder(tmp8Bits);
        }
        return result.toString();
    }

}
