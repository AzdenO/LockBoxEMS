package org.vigilance.lockbox.abstracts;
/**
 * Java class to handle encryption and decryption mechanics as well as to hold encryption key and verify the key
 * @author Declan R.A Wadsworth
 * @version 0.1
 */


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class Secure {
    private SecretKeySpec safe_key = null;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Secure(){
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean verify(String password, String sample){
        genKey(password);
        try{
            decryptSample(sample);
        }catch(Exception e){
            return false;
        }
        return true;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method to generate a SecretKeySpec from a users password
     * @param newpass represents the string password we want to create a key from
     */
    public void genKey(String newpass){
        byte[] bytes = (newpass).getBytes(StandardCharsets.UTF_8);
        try{
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            bytes = hash.digest(bytes);
            bytes = Arrays.copyOf(bytes, 16);
            this.safe_key = new SecretKeySpec(bytes, "AES");

        }catch(Exception e){
            ;//an unlikely error, only really if updates not performed and libraries become dated
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void newPassword(String password){
        genKey(password);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String encrypt(String plaintext){
        try{
            Cipher encoder = Cipher.getInstance("AES");
            encoder.init(Cipher.ENCRYPT_MODE, this.safe_key);
            return(Base64.getEncoder().encodeToString(encoder.doFinal(plaintext.getBytes(StandardCharsets.UTF_8))));
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("not decrypted");
        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String decrypt(String ciphertext){
        try{
            Cipher encoder = Cipher.getInstance("AES");
            encoder.init(Cipher.DECRYPT_MODE, this.safe_key);
            return(new String(encoder.doFinal(Base64.getDecoder().decode(ciphertext))));

        }catch(Exception e){

        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String decryptSample(String sample) throws Exception{

        Cipher encoder = Cipher.getInstance("AES");
        encoder.init(Cipher.DECRYPT_MODE, this.safe_key);
        return(new String(encoder.doFinal(Base64.getDecoder().decode(sample))));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public byte[] decryptBytes(byte[] cipherData){
        try{

            Cipher encoder = Cipher.getInstance("AES");
            encoder.init(Cipher.DECRYPT_MODE, this.safe_key);
            return encoder.doFinal(cipherData);

        }catch(Exception e){

        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public byte[] encryptBytes(byte[] cipherData){
        try{

            Cipher encoder = Cipher.getInstance("AES");
            encoder.init(Cipher.ENCRYPT_MODE, this.safe_key);
            return encoder.doFinal(cipherData);

        }catch(Exception e){

        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}




















