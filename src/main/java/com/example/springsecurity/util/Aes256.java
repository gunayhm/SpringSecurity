package com.example.springsecurity.util;

import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

@Slf4j
public class Aes256 {
  public static String encrypt(String plainText, String secret, String salt) {

    byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    try {
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
      String AES256Encrypt =  Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")));
      return Base64.getEncoder().encodeToString(AES256Encrypt.getBytes());
    } catch (Exception e) {
      log.error(e.getMessage(), e);

      throw new RuntimeException(e);
    }
  }

  public static String decrypt(String valueToDecrypt, String secret, String salt) {

    byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    try {
      byte[] decodedBytes = Base64.getDecoder().decode(valueToDecrypt);
      String decodedText = new String(decodedBytes);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
      return new String(cipher.doFinal(Base64.getDecoder().decode(decodedText)));
    } catch (Exception e) {
      log.error(e.getMessage(), e);

      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {

    System.out.println(decrypt("UEZ4SElES2xLZkRlcExTeFRFL3phQT09", "1234", "8787"));

  }

}