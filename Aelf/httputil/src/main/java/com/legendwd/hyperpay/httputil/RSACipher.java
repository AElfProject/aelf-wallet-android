package com.legendwd.hyperpay.httputil;


import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACipher {

    KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;
    byte[] encryptedBytes, decryptedBytes;
    Cipher cipher, cipher1;
    String encrypted, decrypted;

    public static PublicKey stringToPublicKey(String publicKeyString)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {

        try {
            if (publicKeyString.contains("-----BEGIN PUBLIC KEY-----") || publicKeyString.contains("-----END PUBLIC KEY-----"))
                publicKeyString = publicKeyString.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
            publicKeyString = publicKeyString.trim();
            byte[] keyBytes = Base64.decode(publicKeyString, Base64.DEFAULT);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(spec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void main(String[] args) throws Exception {

        String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAt56NgDQDnEXsOuI4z2s5\n" +
                "6P1PjaqAy2lxEjmpz3SefShd3JWKJzTM9bbJ0/QoPOOsLcCxsNVYdNQamsQ9XP8v\n" +
                "QvcdT8d6DUIwMHUyW+R4Jx5agrd/cSCChLUg07SjfNENU4LhfFxXZWKhrnyJlmGk\n" +
                "GoQeYs/GLTYmYB7Ufx1hq/GgjAUICY4BfNpzc82NN4dtli3nb+y6463kFta7zMfo\n" +
                "ZTdhNDH8wdCLYHxA4uQFJqJgGrPxiPxDr4XPLxT9o5xuE2rJx34E25mDGIzFjDEY\n" +
                "8sjE/ghUnX0T6dmWL1Bh9OTgTfGeNb+kNkPL7A7xMH+XjH1glmOWoEJEbT5CF+Hz\n" +
                "5lOnbyWfVUV33IEpRlo5Ep/KgjXbBnVdIdPuDkDRUI2jtnvMt8x/WEb3nS/WHaK6\n" +
                "i5UUJDjAkcpGNRhsZVBxYzGbgh87LA8TMUP9grmgzPJTiT33FSURWr0HJq1C/KhB\n" +
                "NImlT4DJVvNxYxfpOwOxuB6fkKFPkY9MaJDnUXl/VIdJp0wuXe7XQ+pdUSII1B8X\n" +
                "gJH0YcyDzSKCDWL66PbXJKee46WfU1lAtrofcML9mWRA8CEIY+xWpchMRTvzHoDb\n" +
                "qQiR5dVZN9kM7HEWnra9k5ghDBdrIe+pJYc997di9d7gq4FjmXg130HDe4/Geovc\n" +
                "MPSi0Mn0Yfdj/29sk5fpsHsCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----";

        String context = "moon123456";

        RSACipher rsaCipher = new RSACipher();
        String enStr = rsaCipher.encrypt(context, publicKey);
        //    DLog.v("加密后文字: \r\n" + enStr);


    }

    /**
     * Encrypt plain text to RSA encrypted and Base64 encoded string
     *
     * @param // args[0] should be plain text that will be encrypted
     *           If args[1] is be, it should be RSA public key to be used as encrypt public key
     * @return a encrypted string that Base64 encoded
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String encrypt(String context, String publicKey)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {

        PublicKey rsaPublicKey = stringToPublicKey(publicKey);

        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        encryptedBytes = cipher.doFinal(context.getBytes("UTF-8"));

        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    public String encrypt(String context)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException,
            IOException {

        String publicKey = ServiceGenerator.publicKey;

        String enCrypt = context;

        PublicKey rsaPublicKey = stringToPublicKey(publicKey);
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        encryptedBytes = cipher.doFinal(context.getBytes("UTF-8"));
        enCrypt = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);


        return enCrypt;
    }

    public String decrypt(String result)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {

        cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedBytes = cipher1.doFinal(Base64.decode(result, Base64.DEFAULT));
        decrypted = new String(decryptedBytes);

        return decrypted;
    }

    public String getPublicKey(String option)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {

        switch (option) {

            case "pkcs1-pem":
                String pkcs1pem = "-----BEGIN RSA PUBLIC KEY-----\n";
                pkcs1pem += Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
                pkcs1pem += "-----END RSA PUBLIC KEY-----";

                return pkcs1pem;

            case "pkcs8-pem":
                String pkcs8pem = "-----BEGIN PUBLIC KEY-----\n";
                pkcs8pem += Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
                pkcs8pem += "-----END PUBLIC KEY-----";

                return pkcs8pem;

            case "base64":
                return Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);

            default:
                return null;

        }

    }
}
