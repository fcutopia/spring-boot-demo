package com.fc.springboot.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class CsvUtils {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtils.class);

    /**
     * 从csv文件获取原始idValue样本集
     *
     * @param srcPath
     * @return
     */
    public static Set<String> getCsvData(String srcPath) {
        Set<String> set = new HashSet<>();
        //列名
        String idType = "";
        FileInputStream in = null;
        String charset = "utf-8";
        try {
            in = new FileInputStream(new File(srcPath));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return set;
        }
        int i = 0;
        try (CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(in, charset))).build()) {
            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                String[] next = iterator.next();
                //去除第一行的表头，从第二行开始
                if (i == 0) {
                    //第一列为idType
                    idType = Arrays.asList(next).get(0);
                } else {
                    set.add(Arrays.asList(next).get(0));
                }
                i++;
            }
            return set;
        } catch (Exception e) {
            logger.warn("CSV文件读取异常", e);
            return set;
        }
    }

    public static Set<String> getCsvData(FileInputStream in) {
        Set<String> set = new HashSet<>();
        //列名
        String idType = "";
        String charset = "utf-8";
        int i = 0;
        try (CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(in, charset))).build()) {
            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                String[] next = iterator.next();
                //去除第一行的表头，从第二行开始
                if (i == 0) {
                    //第一列为idType
                    idType = Arrays.asList(next).get(0);
                } else {
                    set.add(Arrays.asList(next).get(0));
                }
                i++;
            }
            return set;
        } catch (Exception e) {
            logger.warn("CSV文件读取异常", e);
            return set;
        }
    }

    /**
     * 生成交集csv文件
     *
     * @param filePath
     * @param list
     */
    public static void writeCsvFile(String filePath, List<String[]> list) {
        ICSVWriter icsvWriter = null;
        try {
            icsvWriter = new CSVWriterBuilder(new FileWriterWithEncoding(filePath, "utf-8"))
                    .withSeparator(ICSVWriter.DEFAULT_SEPARATOR) // 分隔符
                    .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER) // 不使用引号
                    .build();
            icsvWriter.writeAll(list);
            icsvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                icsvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
