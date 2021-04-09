package pl.pb;


public class Main {

    public static void main(String[] args) {

        var key = KeyGenerator.generator();
        String message = "With 2,500 to 3,000 words, you can understand 90% of everyday English conversations, English newspaper and magazine articles, and English used in the workplace. The remaining 10% you'll be able to learn from context, or ask questions about. However, it's essential to learn the right English vocabulary words, so you don't waste your time trying to memorize a huge collection with very little benefit. The list below seems long, but when you can use all these words with confidence, your English vocabulary will be fully functional.";
        String[] encrypted = DES.encrypt(message, key);
        String decrypted = DES.decrypt(encrypted, key);
    }

}
