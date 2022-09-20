package com.fc.springboot.utils;


import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @ClassName DesUtils
 * @Description
 * @Author fc
 * @Date 2022/3/2 11:21 上午
 * @Version 1.0
 **/
@Component
public class DesUtils {


    public static Cipher en_cipher = null;

    public static Cipher de_cipher = null;


    @PostConstruct
    public void init() {
        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            en_cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            en_cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
            de_cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            de_cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
        } catch (Exception e) {
            throw new RuntimeException("Error initializing Cipher class. Cause: " + e);
        }
    }


    static String strEnc = "d0836940940787aee5610cfb6ebff42184a61d0ee1d2882f7c43d7459786287b5aceefaeb1c6ec6f4d126b4c60c449dd87d3932cf4a8360a7ace65e200593254e23a4106b32ef853bf326d44b9a2ea1d35411661ce95e6df54f3b8e4a6b568f0a7cc0b6fa54a3ed17d0826f1b07486eb4545268e6ab6bae804c4989066284d6bb2c773e0e3eed604d5eff9e63bc21ed0252ccee06b3ce0a2cf13880002dfb772aaaf933d01ea4d65b63474b891f174e0cbf722663490c4ecc0c71f2bd3560a261692efda1b1f210df4d7e6cfc3e731d5a1f7908c37a6951ed919cd4fa76759a2e294b589a55f5ae23e94fb85fe1a1839e0e8f565fa7da27723d31c44c807aaa76c21570920625db9655badbd08282a0f2b6356f34608890f3a4778911dc6d9926d2040366c7ee569476ba4df7dad953c4eca76e8af55902143baeb7493ba05d177c0c19c1dce1e81ab877efbd66ee911712368ee659c81ef44f8a0097ee19f5fd87fee32c0a975d3c18875e4f1808cc101ad43bc5ad32039";
    public static String key = "ffnR0djm";




    public static void main(String[] args) throws Exception {
        String sourcePath = "/Users/fc/Documents/work/pn_md5_2.csv";
        String encPath = "/Users/fc/Documents/work/encode/enc.txt";
        String decPath = "/Users/fc/Documents/work/encode/dec.csv";


        /*encrypt(sourcePath, encPath);
        decrypt(encPath, decPath);*/

        /*encryptV2(sourcePath, encPath);
        decryptV2(encPath, decPath);*/

        // test();

        // test2();


        /*DES_CBC_Encrypt_V2(sourcePath, encPath);
        DES_CBC_Decrypt_v2(encPath, decPath);*/
    }

    public void encrypt(String destFile, List<String> list) throws Exception {
        try {
            OutputStream out = new FileOutputStream(destFile);
            for (String data : list) {
                byte[] decStr = DES_CBC_Encrypt(data.getBytes(), key.getBytes(StandardCharsets.UTF_8));
                out.write(byteToHexString(decStr).getBytes(StandardCharsets.UTF_8));
                out.write("\r\n".getBytes(StandardCharsets.UTF_8));
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void encrypt(InputStream inputStream, String destFile) {
        try {
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputReader);
            OutputStream out = new FileOutputStream(destFile);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                byte[] decStr = DES_CBC_Encrypt(str.getBytes(), key.getBytes());
                out.write(byteToHexString(decStr).getBytes(StandardCharsets.UTF_8));
                out.write("\r\n".getBytes());
            }
            bf.close();
            inputReader.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decrypt(String encPath, String dest) throws Exception {
        InputStream inputStream = new FileInputStream(encPath);
        OutputStream out = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = inputStream.read(buffer)) >= 0) {
            byte[] bytes = new String(buffer).replace("\r\n", "").getBytes(StandardCharsets.UTF_8);

            out.write(DES_CBC_Decrypt(bytes, key.getBytes(StandardCharsets.UTF_8)), 0, r);
        }
        out.close();
        inputStream.close();
    }

    public void encryptV2(String filePath, String destFile) throws Exception {
        try {
            File file = new File(filePath);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);
            OutputStream out = new FileOutputStream(destFile);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                byte[] decStr = DES_CBC_Encrypt(str.getBytes(), key.getBytes());
                out.write(byteToHexString(decStr).getBytes(StandardCharsets.UTF_8));
                out.write("\r\n".getBytes());
            }
            bf.close();
            inputReader.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void en() throws IOException {
        String content1 = "fengchao123";
        String content2 = "chineseisgreat";
        String encPath = "/Users/fc/Documents/work/encode/enc.txt";
        OutputStream out = new FileOutputStream(encPath);
        List<String> list = new ArrayList<>();
        list.add(content2);
        list.add(content1);
        for (String content : list) {
            byte[] encrypted = DES_CBC_Encrypt(content.getBytes(), key.getBytes());
            String str = byteToHexString(encrypted);
            out.write(str.getBytes(StandardCharsets.UTF_8));
            out.write("\r\n".getBytes());
        }
        out.flush();
        out.close();
    }


    public void decryptV2(String encPath, String dectFile) throws Exception {
        try {
            File file = new File(encPath);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);
            OutputStream out = new FileOutputStream(dectFile);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                System.out.println(str);
                out.write(DES_CBC_Decrypt(hexToByteArray(str), key.getBytes()));
                out.write("\r\n".getBytes());
            }
            out.flush();
            bf.close();
            inputReader.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void de() throws IOException {
        String key = "ffnR0djm";
        String encPath = "/Users/fc/Documents/work/encode/enc.txt";
        try {
            File file = new File(encPath);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                //String str = byteToHexString(buffer).replaceAll("\n","");
                System.out.println("解密后_2：" + new String(DES_CBC_Decrypt(hexToByteArray(str), key.getBytes())));
            }
            bf.close();
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void DES_CBC_Encrypt_V2(String file, String dest) {

        try {
            InputStream is = new FileInputStream(file);
            OutputStream out = new FileOutputStream(dest);

            DESKeySpec keySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
            CipherInputStream cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
            cis.close();
            is.close();
            out.close();
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
    }

    private byte[] DES_CBC_Decrypt_v2(String file, String dest) {
        try {
            InputStream is = new FileInputStream(file);
            OutputStream out = new FileOutputStream(dest);

            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyBytes));

            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                System.out.println();
                cos.write(buffer, 0, r);
            }
            cos.close();
            out.close();
            is.close();
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
        return null;
    }


    public byte[] DES_CBC_Encrypt(byte[] content, byte[] keyBytes) {
        try {
            byte[] result = en_cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    private byte[] DES_CBC_Decrypt(byte[] content, byte[] keyBytes) {
        try {
            byte[] result = de_cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
        return null;
    }


    public void test2() throws IOException {
        en();
        de();
    }


    public void test() throws IOException {
        String content = "fengchao123";
        String key = "ffnR0djm";
        System.out.println("加密前：" + content);
        byte[] encrypted = DES_CBC_Encrypt(content.getBytes(), key.getBytes());
        System.out.println("加密后：" + byteToHexString(encrypted));
        String encPath = "/Users/fc/Documents/work/encode/enc.txt";
        OutputStream out = new FileOutputStream(encPath);
        out.write(encrypted);

        InputStream is = new FileInputStream(encPath);
        byte[] buffer = new byte[1024];
        is.read(buffer);
        System.out.println(new String(DES_CBC_Decrypt(buffer, key.getBytes())));


        byte[] decrypted = DES_CBC_Decrypt(encrypted, key.getBytes());
        System.out.println("解密后：" + new String(decrypted));


        System.out.println("解密后_2：" + new String(DES_CBC_Decrypt(hexToByteArray(strEnc), key.getBytes())));
    }


    private static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String sTemp;
        for (int i = 0; i < bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    public static String readFile(String filePath) {
        StringBuffer sbf = new StringBuffer();
        try {
            File file = new File(filePath);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                sbf.append(str).append(",");
            }
            bf.close();
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sbf.toString();
    }


}
