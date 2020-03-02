package com.vaibhav.example;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Main {


    private static SecretKeySpec secretKeySpec;
    private static byte [] key;
    private static final String algo = "AES";

    public static void prepareKey(String myKey){
        MessageDigest messageDigest = null;
        try{
            key = myKey.getBytes(StandardCharsets.UTF_8);
            messageDigest = MessageDigest.getInstance("SHA-1");
            key = messageDigest.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKeySpec = new SecretKeySpec(key, algo);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String toEncrypt, String secret){
        try{
            prepareKey(secret);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(toEncrypt.getBytes("UTF-8")));
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String toDecrypt, String secret){
        try{
            prepareKey(secret);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(toDecrypt)));
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {
        final String secret = "test";

        String str = "original";

        String encrypted = encrypt(str, secret);
        String decrypted = decrypt(encrypted, secret);
        System.out.println("Original: "+ str);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}
