package pl.pb;

public class KeyGenerator {
    //64 ->58b
    private static final int[] permutationOrder = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4};
    //58 -> 48
    private static final int[] permutationOrder2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32};

    private static final int[] shifts = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    public static String[] generator() {
        String[] subKeys = new String[16];
        String key = "IEOFIT#1";
        StringBuilder leftSide = new StringBuilder();
        StringBuilder rightSide = new StringBuilder();
        StringBuilder binaryKey = new StringBuilder(BinaryConverter.convertStringToBinary8Bits(key));
//        System.out.println(binaryKey.toString());
        //permutacja klucza w kolejnosci pc1 // podzielenie na 2 strony 28bitowe//redukcja do 56bitow
        permutationWithSides(binaryKey, leftSide, rightSide, permutationOrder);
        //System.out.println("x. \t"+BinaryConverter.prettyBinary(binaryKey.toString(),8," "));
        fillSubKeys(subKeys, leftSide, rightSide, binaryKey);
        return subKeys;
    }


    public static void fillSubKeys(String[] subKeys, StringBuilder leftSide, StringBuilder rightSide, StringBuilder binaryKey) {
        for (int i = 0; i < subKeys.length; i++) {
            shiftLeftBothSides(leftSide, rightSide, shifts[i]);
            combineTwoSides(binaryKey, leftSide, rightSide);
            subKeys[i] = permutation(binaryKey, permutationOrder2);
        }
        int x = 1;

        //cout all subKeys
//        for (String k : subKeys) {
//            System.out.print(x++ +".\t ");
//            System.out.println(BinaryConverter.prettyBinary(k,8," "));
//        }
    }

    public static void shiftRight(StringBuilder binaryText) {
        String shiftedBinaryTmp = binaryText.charAt(binaryText.length() - 1) + binaryText.substring(0, binaryText.length() - 1);
        binaryText.setLength(0);
        binaryText.append(shiftedBinaryTmp);
    }

    public static void combineTwoSides(StringBuilder binaryText, StringBuilder left, StringBuilder right) {
        binaryText.setLength(0);
        binaryText.append(left);
        binaryText.append(right);
    }

    public static void shiftLeft(StringBuilder binaryText) {
        String shiftedBinaryTmp = binaryText.substring(1, binaryText.length()) + binaryText.charAt(0);
        binaryText.setLength(0);
        binaryText.append(shiftedBinaryTmp);
    }

    public static void shiftRightBothSides(StringBuilder left, StringBuilder right) {
        shiftRight(left);
        shiftRight(right);
    }

    public static void shiftLeftBothSides(StringBuilder left, StringBuilder right, int times) {
        for (int i = 0; i < times; i++) {
            shiftLeft(left);
            shiftLeft(right);
        }
    }

    public static void permutationWithSides(StringBuilder binaryText, StringBuilder leftSide, StringBuilder rightSide, int[] pc) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        //permutacja klucza w kolejnosci pc1
        for (int index : pc) {
            result.append(binaryText.charAt(index - 1));
            if (i++ < (pc.length / 2)) {
                leftSide.append(binaryText.charAt(index - 1));
            } else rightSide.append(binaryText.charAt(index - 1));
        }
        binaryText.setLength(0);
        binaryText.append(result.toString());
    }

    public static String permutation(StringBuilder binaryText, int[] pc) {
        StringBuilder result = new StringBuilder();

        for (int index : pc) {
            result.append(binaryText.charAt(index - 1));
        }
        return result.toString();
    }
}
