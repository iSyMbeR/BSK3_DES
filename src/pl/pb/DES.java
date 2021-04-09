package pl.pb;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class DES {

    private static final int[] permutationOrderExtendingRightSide = {32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};
    private static final int[] P = {16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};
    private static final int[] finalPermutation = {40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
    private static final int[] permutationOrder =
            {58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};

    public static String[] encrypt(String message, String[] subKey, InputType inputType) {
        StringBuilder[] blocksOfMessage = new StringBuilder[(int) Math.ceil(message.length() / 8.0)];
        String[] encryptedMessage = new String[blocksOfMessage.length];
        for (int i = 0; i < blocksOfMessage.length; i++) {
            blocksOfMessage[i] = new StringBuilder("");
        }

        int blockNr = 0;
        for (int i = 0; i < message.length(); i++) {
            if (((i + 1) % 8) == 0) {
                blocksOfMessage[blockNr].append(message.charAt(i));
                blockNr++;
                continue;
            }
            blocksOfMessage[blockNr].append(message.charAt(i));
        }
        int i = 0;
        for (StringBuilder x : blocksOfMessage) {
            encryptedMessage[i++] = runDesEncrypt(x.toString(), subKey, inputType);
        }
        System.out.println("message : ' " + message + " ' encrypted : \n" + Arrays.toString(encryptedMessage));
        return encryptedMessage;
    }

    public static String runDesEncrypt(String message, String[] subKey, InputType inputType) {
        if (inputType.equals(InputType.INPUT_TEXT)) {
            message = BinaryConverter.convertStringToBinary8Bits(message);
        }
        message = validateMessage(message);
        StringBuilder plainText = new StringBuilder(message);
        StringBuilder encryptedText = new StringBuilder();
        StringBuilder leftSide = new StringBuilder();
        StringBuilder rightSide = new StringBuilder();
        StringBuilder tmpValue = new StringBuilder();
        StringBuilder tableData = new StringBuilder();
        StringBuilder newLeftSide = new StringBuilder();
        StringBuilder newRightSide = new StringBuilder();
        KeyGenerator.permutationWithSides(plainText, leftSide, rightSide, permutationOrder);

        //padding prawej strony
        for (String sKey : subKey) {
            tmpValue.append(KeyGenerator.permutation(rightSide, permutationOrderExtendingRightSide));
            tableData.append(KeyGenerator.permutation(xorAndSort(tmpValue, sKey), P));
            newRightSide.append(xor(tableData, leftSide.toString()));
            newLeftSide.append(rightSide.toString());

            cleanStringBuilder(rightSide, leftSide);
            rightSide.append(newRightSide);
            leftSide.append(newLeftSide);
            cleanStringBuilder(newLeftSide, newRightSide, tmpValue, tableData);
        }
        cleanStringBuilder(tmpValue);
        tmpValue.append(rightSide);
        tmpValue.append(leftSide);
        encryptedText.append(KeyGenerator.permutation(tmpValue, finalPermutation));
        // System.out.println("encrypted text: " + BinaryConverter.prettyBinary(BinaryConverter.fromBinaryToHex(encryptedText).toString(), 2, " ").toUpperCase(Locale.ROOT));
        return BinaryConverter.fromBinaryToHex(encryptedText).toString();
    }

    private static String validateMessage(String message) {
        if (message.length() < 64) {
            StringBuilder messageBuilder = new StringBuilder(message);
            while (messageBuilder.length() != 64) {
                messageBuilder.append("0");
            }
            message = messageBuilder.toString();
        }
        return message;
    }

    private static String validateHexMessage(String message) {
        if (message.length() < 64) {
            StringBuilder messageBuilder = new StringBuilder(message);
            while (messageBuilder.length() != 64) {
                messageBuilder.reverse();
                messageBuilder.append("0");
                messageBuilder.reverse();
            }
            message = messageBuilder.toString();
        }
        return message;
    }

    public static String decrypt(String[] message, String[] subKey) {
        StringBuilder decryptedText = new StringBuilder();
        reverseSubKeys(subKey);
        for (String mess : message) {
            decryptedText.append(runDesDecrypt(mess, subKey));
        }
        System.out.println("Decrypted message: " + decryptedText);
        return decryptedText.toString();
    }

    public static String runDesDecrypt(String message, String[] subKey) {
        message = BinaryConverter.fromHexToBinary(message);
        message = validateHexMessage(message);
        StringBuilder plainText = new StringBuilder(message);
        StringBuilder encryptedText = new StringBuilder();
        StringBuilder leftSide = new StringBuilder();
        StringBuilder rightSide = new StringBuilder();
        StringBuilder tmpValue = new StringBuilder();
        StringBuilder tableData = new StringBuilder();
        StringBuilder newLeftSide = new StringBuilder();
        StringBuilder newRightSide = new StringBuilder();

        KeyGenerator.permutationWithSides(plainText, leftSide, rightSide, permutationOrder);

        //padding prawej strony
        for (String sKey : subKey) {
            tmpValue.append(KeyGenerator.permutation(rightSide, permutationOrderExtendingRightSide));
            tableData.append(KeyGenerator.permutation(xorAndSort(tmpValue, sKey), P));
            newRightSide.append(xor(tableData, leftSide.toString()));
            newLeftSide.append(rightSide.toString());

            cleanStringBuilder(rightSide, leftSide);
            rightSide.append(newRightSide);
            leftSide.append(newLeftSide);
            cleanStringBuilder(newLeftSide, newRightSide, tmpValue, tableData);
        }
        cleanStringBuilder(tmpValue);
        tmpValue.append(rightSide);
        tmpValue.append(leftSide);
        encryptedText.append(KeyGenerator.permutation(tmpValue, finalPermutation));
        //System.out.println("decrypted text : " + BinaryConverter.fromBinaryToText(encryptedText.toString()));
        return BinaryConverter.fromBinaryToText(encryptedText.toString());
    }

    public static void cleanStringBuilder(StringBuilder... stringBuilder) {
        for (StringBuilder s : stringBuilder) {
            s.setLength(0);
        }
    }

    public static String xor(StringBuilder rightSide, String subKey) {
        StringBuilder valueAfterXor = new StringBuilder();
        for (int i = 0; i < rightSide.length(); i++) {
            valueAfterXor.append(rightSide.toString().charAt(i) ^ subKey.charAt(i));
        }
        return valueAfterXor.toString();
    }

    public static void reverseSubKeys(String[] subKeys) {
        Collections.reverse(Arrays.asList(subKeys));
        System.out.println(subKeys[0]);
    }

    ///funkcja f(R,K);
    public static StringBuilder xorAndSort(StringBuilder rightSide, String subKey) {
        boolean startChecker = true;
        StringBuilder result = new StringBuilder();
        StringBuilder rowBinary = new StringBuilder();
        StringBuilder columnBinary = new StringBuilder();
        String valueAfterXor = xor(rightSide, subKey);
        int table = 0;
        for (int i = 0; i < valueAfterXor.length(); i++) {
            if (rowBinary.length() == 2) {
                startChecker = true;
                cleanStringBuilder(rowBinary);
                cleanStringBuilder(columnBinary);
            }
            if (startChecker) {
                rowBinary.append(valueAfterXor.charAt(i));
                startChecker = false;
                continue;
            }
            if (((i + 1) % 6) == 0) {
                rowBinary.append(valueAfterXor.charAt(i));
                startChecker = true;
                int valueFromTable = STable.S[table++][Integer.parseInt(rowBinary.toString(), 2)][Integer.parseInt(columnBinary.toString(), 2)];
                int diff = 4 - Integer.toBinaryString(valueFromTable).length();
                if (diff != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    //dopisuje 0 jezeli mniej niz 4 bity
                    for (int j = 0; j < diff; j++) {
                        stringBuilder.append("0");
                    }
                    stringBuilder.append(Integer.toBinaryString(valueFromTable));
                    result.append(stringBuilder);
                    continue;
                }
                result.append(Integer.toBinaryString(valueFromTable));
                continue;
            }
            columnBinary.append(valueAfterXor.charAt(i));
        }
        return result;
    }

}
