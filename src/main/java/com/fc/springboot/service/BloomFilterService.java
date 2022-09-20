package com.fc.springboot.service;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName BloomFilterService
 * @Description
 * @Author fc
 * @Date 2022/4/20 7:55 下午
 * @Version 1.0
 **/
public class BloomFilterService {


    public static BloomFilter<CharSequence> bloomFilter;

    public static String DEFAULT_SUFFIX = "_intersection.csv";

    public static void main(String[] args) {
        String filterPath = System.getProperty("filter_path");
        String sourceFilePath = System.getProperty("source_file_path");
        // filterPath = "/Users/fc/Documents/work/code/springboot/src/main/resources/bloomFilter/bloomFilter_test.txt";
        // sourceFilePath = "/Users/fc/Downloads/0413_01.csv";
        bloomFilterTest(filterPath, sourceFilePath);
    }

    public static void bloomFilterTest(String filterPath, String sourceFilePath) {
        if (StringUtils.isAnyBlank(filterPath, sourceFilePath)) {
            System.out.println("参数为空 filterPath={}" + filterPath + ",sourceFilePath=" + sourceFilePath);
            return;
        }
        try {
            init(filterPath);
            String intersectionPath = filePath(sourceFilePath);
            intersect(sourceFilePath, intersectionPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化生成布隆过滤器对象
     *
     * @param filterPath
     * @throws IOException
     */
    public static void init(String filterPath) throws IOException {
        System.out.println("当前路径=" + System.getProperty("user.dir"));
        System.out.println("开始生成布隆过滤器，文件路径=" + filterPath);
        FileInputStream inputStream = new FileInputStream(filterPath);
        bloomFilter = BloomFilter.readFrom(inputStream, Funnels.stringFunnel(Charsets.UTF_8));
        System.out.println("生成布隆过滤器完成");
    }


    /**
     * 求取交集
     *
     * @param sourceFilePath   源文件路径
     * @param intersectionPath 交集文件保存路径
     * @throws FileNotFoundException
     */
    public static void intersect(String sourceFilePath, String intersectionPath) throws FileNotFoundException {
        FileInputStream in = new FileInputStream(sourceFilePath);
        String charset = "utf-8";
        List<String[]> intersectionList = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(in, charset))).build()) {
            Iterator<String[]> iterator = csvReader.iterator();
            String id = "";
            while (iterator.hasNext()) {
                String[] next = iterator.next();
                id = Arrays.asList(next).get(0);
                if (bloomFilter.mightContain(id)) {
                    intersectionList.add(next);
                }
            }
            //保存交集文件到指定csv文件中
            writeCsvFile(intersectionPath, intersectionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据源文件生成交集文件名称
     *
     * @param sourceFilePath 源文件路径
     * @return
     */
    public static String filePath(String sourceFilePath) {
        List<String> list = Arrays.asList(sourceFilePath.split("/"));
        StringBuffer sbf = new StringBuffer();
        list.stream().limit(list.size() - 1).forEach(o -> {
            if (StringUtils.isNotBlank(o)) {
                sbf.append("/").append(o);
            }
        });
        String fileNameStr = list.get(list.size() - 1);
        String newPath = fileNameStr.substring(0, fileNameStr.length() - 4) + DEFAULT_SUFFIX;
        sbf.append("/").append(newPath);
        return sbf.toString();
    }

    /**
     * 交集保存至目标csv文件
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
