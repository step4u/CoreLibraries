package com.coretree.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.coretree.exceptions.CryptoException;

public class Crypto {
	private static String ALGORITHM = "AES";
    private static String TRANSFORMATION = "AES";
    
    public static void setAlgorithm (String algorithm) { ALGORITHM = algorithm; }
    public static String getAlgorithm () { return ALGORITHM; }
    
    public static void setTransformation (String transformation) { TRANSFORMATION = transformation; }
    public static String getTransformation () { return TRANSFORMATION; }
 
    public static void encrypt(String key, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }
 
    public static void decrypt(String key, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }
    
    public static byte[] decrypt(String key, File inputFile) throws CryptoException {
        return doCrypto(Cipher.DECRYPT_MODE, key, inputFile);
    }
 
    private static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws CryptoException {
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
            byte[] outputBytes = null;
        	
            Key secretKey = new SecretKeySpec(key.getBytes(), getAlgorithm());
            
            switch (getAlgorithm()) {
	            case "HmacSHA1":
        			Mac mac = Mac.getInstance(getTransformation());
        			mac.init(secretKey);
        			outputBytes = mac.doFinal(inputBytes);
	            	break;
            	case "AES":
        		default:
                    Cipher cipher = Cipher.getInstance(getTransformation());
                    cipher.init(cipherMode, secretKey);
                    outputBytes = cipher.doFinal(inputBytes);
        			break;
            }
            
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
             
            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
    
    private static byte[] doCrypto(int cipherMode, String key, File inputFile) throws CryptoException {
    	byte[] outputBytes = null;
    	
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), getAlgorithm());
            Cipher cipher = Cipher.getInstance(getTransformation());
            cipher.init(cipherMode, secretKey);
             
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            outputBytes = cipher.doFinal(inputBytes);
            inputStream.close();
            
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
        
        return outputBytes;
    }
}
