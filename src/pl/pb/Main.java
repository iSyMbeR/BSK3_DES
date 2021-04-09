package pl.pb;


public class Main {

    public static void main(String[] args) {
        String key = "IEOFIT#1";
        var generatedKey = KeyGenerator.generator(key);
        String plainText = "bank be enpe pari bas ";
        String[] encrypted = DES.encrypt(plainText, generatedKey, InputType.INPUT_TEXT);
        String decrypted = DES.decrypt(encrypted, generatedKey);
    }

}
