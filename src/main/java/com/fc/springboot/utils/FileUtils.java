package com.fc.springboot.utils;

import java.io.*;

/**
 * @ClassName FileUtils
 * @Description
 * @Author fc
 * @Date 2022/2/24 10:18 上午
 * @Version 1.0
 **/
public class FileUtils {

    /**
     * 文件file进行加密
     *
     * @param fileUrl 文件路径
     * @param key     密码
     * @throws Exception
     */
    public static void encrypt(String fileUrl, String key) throws Exception {
        File file = new File(fileUrl);
        String path = file.getPath();
        if (!file.exists()) {
            return;
        }
        int index = path.lastIndexOf("/");
        String destFile = path.substring(0, index) + "/" + "def.csv";
        File dest = new File(destFile);
        InputStream in = new FileInputStream(fileUrl);
        OutputStream out = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int r;
        byte[] buffer2 = new byte[1024];
        while ((r = in.read(buffer)) > 0) {
            for (int i = 0; i < r; i++) {
                byte b = buffer[i];
                buffer2[i] = b == 255 ? 0 : ++b;
            }
            out.write(buffer2, 0, r);
            out.flush();
        }
        in.close();
        out.close();
        file.delete();
        dest.renameTo(new File(fileUrl));
        appendMethodA(fileUrl, key);
        System.out.println("加密成功");
    }

    /**
     * @param fileName
     * @param content  密钥
     */
    public static void appendMethodA(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密
     *
     * @param fileUrl   源文件
     * @param tempUrl   临时文件
     * @param keyLength 密码长度
     * @return
     * @throws Exception
     */
    public static String decrypt(String fileUrl, String tempUrl, int keyLength) throws Exception {
        File file = new File(fileUrl);
        if (!file.exists()) {
            return null;
        }
        File dest = new File(tempUrl);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        InputStream is = new FileInputStream(fileUrl);
        OutputStream out = new FileOutputStream(tempUrl);
        byte[] buffer = new byte[1024];
        byte[] buffer2 = new byte[1024];
        byte bMax = (byte) 255;
        long size = file.length() - keyLength;
        int mod = (int) (size % 1024);
        int div = (int) (size >> 10);
        int count = mod == 0 ? div : (div + 1);
        int k = 1, r;
        while ((k <= count && (r = is.read(buffer)) > 0)) {
            if (mod != 0 && k == count) {
                r = mod;
            }
            for (int i = 0; i < r; i++) {
                byte b = buffer[i];
                buffer2[i] = b == 0 ? bMax : --b;
            }
            out.write(buffer2, 0, r);
            k++;
        }
        out.close();
        is.close();
        System.out.println("解密成功");
        return tempUrl;
    }

    public static void main(String[] args) {


        if (1 == 1) {
            createFile();
            return;
        }
        String key = "fc@9527";
        String fileUrl = "/Users/fc/Documents/work/pn_md5_2.csv";
        try {
            //encrypt(fileUrl,key);
            String tempUrl = "/Users/fc/Documents/work/abc.csv";
            // decrypt(fileUrl, tempUrl, 2);

            delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete() {
        File file = new File("/Users/fc/Documents/work/code/springboot/src/main/resources/bloomFilter/bloomFilter_test.txt");
        file.delete();
    }


    public static void createFile() {
        String filePath = "/Users/fc/Documents/work/code/springboot/src/main/resources/bloomFilter/config.json";
        String contents = "{\"components\": {    \"reader_0\": {      \"module\": \"Reader\",      \"output\": {        \"data\": [          \"data\"        ]      }    },    \"dataio_0\": {      \"module\": \"DataIO\",      \"input\": {        \"data\": {          \"data\": [            \"reader_0.data\"          ]        }      },      \"output\": {        \"data\": [          \"data\"        ],        \"model\": [          \"model\"        ]      }    },    \"intersection_0\": {      \"module\": \"Intersection\",      \"input\": {        \"data\": {          \"data\": [            \"dataio_0.data\"          ]        }      },      \"output\": {        \"data\": [          \"data\"        ]      }    },    \"hetero_feature_binning_0\": {      \"module\": \"HeteroFeatureBinning\",      \"input\": {        \"data\": {          \"data\": [            \"intersection_0.data\"          ]        }      },      \"output\": {        \"data\": [          \"data\"        ],        \"model\": [          \"model\"        ]      }    },    \"hetero_feature_selection_0\": {      \"module\": \"HeteroFeatureSelection\",      \"input\": {        \"data\": {          \"data\": [            \"hetero_feature_binning_0.data\"          ]        },        \"isometric_model\": [          \"hetero_feature_binning_0.model\"        ]      },      \"output\": {        \"data\": [          \"data\"        ],        \"model\": [          \"model\"        ]      }    },    \"feature_scale_0\": {      \"module\": \"FeatureScale\",      \"input\": {        \"data\": {          \"data\": [            \"hetero_feature_selection_0.data\"          ]        }      },      \"output\": {        \"data\": [          \"data\"        ],        \"model\": [          \"model\"        ]      }    },    \"hetero_data_split_0\": {      \"module\": \"HeteroDataSplit\",      \"input\": {        \"data\": {          \"data\": [            \"feature_scale_0.data\"          ]        }      },      \"output\": {        \"data\": [          \"train_data\",          \"validate_data\",          \"test_data\"        ]      }    },    \"hetero_lr_0\": {      \"module\": \"HeteroLR\",      \"input\": {        \"data\": {          \"train_data\": [            \"hetero_data_split_0.train_data\"          ],          \"validate_data\": [            \"hetero_data_split_0.test_data\"          ]        }      },      \"output\": {        \"data\": [          \"data\"        ],        \"model\": [          \"model\"        ]      }    },    \"evaluation_0\": {      \"module\": \"Evaluation\",      \"input\": {        \"data\": {          \"data\": [            \"hetero_lr_0.data\"          ]        }      },      \"output\": {        \"data\": [          \"data\"        ]      }    }  }}";
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            writesome(file, contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writesome(File file, String str) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(str);
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


