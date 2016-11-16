package com.mobiletrain.encryptiondecryption;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by idea on 2016/10/28.
 */
public class MyEncDecUtil {

    private static final String TAG = "test";
    public static final String MODULUS = "100631058000714094813874361191853577129731636346684218206605779824931626830750623070803100189781211343851763275329364056640619755337779928985272486091431384128027213365372009648233171894708338213168824861061809490615593530405056055952622249066180336803996949444124622212096805545953751253607916170340397933039";
    public static final String PRIVATE_EXPONENT = "26900155715313643087786516528374548998821559381075740707715132776187148793016466508650068087107695523642202737697714709374658856733792614490943874205956727606674634563665154616758939576547663715234643273055658829482813503959459653708062875625210008961239643775661357655599312857249418610810177817213648575161";
    public static final String PUBLIC_EXPONENT = "65537";

    public static String createMessageDigest(String msg, String charset) {
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(msg.getBytes(charset));
            Log.d(TAG, "createMessageDigest:bytes.length=" + bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                String s = String.format("%02x", b) + ":";
                result += s;
            }
            result = result.substring(0, result.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String encodeBase64(String msg, String charset) {
        String result = null;
        try {
            result = Base64.encodeToString(msg.getBytes(charset), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decodeBase64(String secret, String charset) {
        byte[] bytes = Base64.decode(secret, Base64.DEFAULT);
        String result = new String(bytes);
        return result;
    }

    public static String encodeDES(String src, String charset, String keyStr) {
        String result = null;
        try {
            SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "DES");//根据算法和秘钥字符串，创建秘钥

            Cipher cipher = Cipher.getInstance("DES");//根据算法创建密码对象
            cipher.init(Cipher.ENCRYPT_MODE, key);//指定为编码模式，指定秘钥
            byte[] bytes = cipher.doFinal(src.getBytes(charset));//执行得到密文的字节数组
            result = Base64.encodeToString(bytes, Base64.DEFAULT);//密码也采用64种基本字符，否则会乱码
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String decodeDES(String secret, String charset, String keyStr) {
        String result = null;
        try {
            SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "DES");

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bytes = cipher.doFinal(Base64.decode(secret, Base64.DEFAULT));
            result = new String(bytes, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encodeRSA(String src,String charset,String publicKeyStr){
        String result = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new RSAPublicKeySpec(new BigInteger(MODULUS), new BigInteger(PUBLIC_EXPONENT)));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte[] bytes = cipher.doFinal(src.getBytes(charset));
            result  = Base64.encodeToString(bytes,Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decodeRSA(String secret,String charset,String privateKeyStr){
        String result = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new RSAPrivateKeySpec(new BigInteger(MODULUS), new BigInteger(PRIVATE_EXPONENT)));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            byte[] bytes = cipher.doFinal(Base64.decode(secret,Base64.DEFAULT));
            result = new String(bytes,charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
