package com.fc.springboot.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAAlgorithm {

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";

    public RSAAlgorithm() {
    }

    public static Map<String, String> createKeys(int keySize) {
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException var8) {
            throw new IllegalArgumentException("No such algorithm-->[RSA]");
        }

        kpg.initialize(keySize);
        KeyPair keyPair = kpg.generateKeyPair();
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }

    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, 1, data.getBytes("UTF-8"), publicKey.getModulus().bitLength()));
        } catch (Exception var3) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", var3);
        }
    }

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, privateKey);
            return new String(rsaSplitCodec(cipher, 2, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), "UTF-8");
        } catch (Exception var3) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", var3);
        }
    }

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, 1, data.getBytes("UTF-8"), privateKey.getModulus().bitLength()));
        } catch (Exception var3) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", var3);
        }
    }

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, publicKey);
            return new String(rsaSplitCodec(cipher, 2, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), "UTF-8");
        } catch (Exception var3) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", var3);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock;
        if (opmode == 2) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;

        try {
            while (datas.length > offSet) {
                byte[] buff;
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }

                out.write(buff, 0, buff.length);
                ++i;
                offSet = i * maxBlock;
            }
        } catch (Exception var10) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", var10);
        }

        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> keyMap = createKeys(1024);
        String publicKey = (String) keyMap.get("publicKey");
        String privateKey = (String) keyMap.get("privateKey");
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);
        System.out.println("公钥加密——私钥解密");
        String str = "站在大明门前守卫的禁卫军，事先没有接到\n有关的命令，但看到大批盛装的官员来临，也就\n以为确系举行大典，因而未加询问。进大明门即\n为皇城。文武百官看到端门午门之前气氛平静，\n城楼上下也无朝会的迹象，既无几案，站队点名\n的御史和御前侍卫“大汉将军”也不见踪影，不免\n心中揣测，互相询问：所谓午朝是否讹传？";
        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        RSAPublicKey rsaPublicKey = getPublicKey(publicKey);
        String encodedData = publicEncrypt(str, rsaPublicKey);
        BigInteger modulus1 = rsaPublicKey.getModulus();
        System.out.println("modulus n：\r\n" + modulus1);
        System.out.println("公钥  e：\r\n" + rsaPublicKey.getPublicExponent());
        System.out.println("密文：\r\n" + encodedData);
        RSAPrivateKey rsaPrivateKey = getPrivateKey(privateKey);
        String decodedData = privateDecrypt(encodedData, rsaPrivateKey);
        System.out.println("私钥  d：\r\n" + rsaPrivateKey.getPrivateExponent());
        System.out.println("解密后文字: \r\n" + decodedData);
    }
}
