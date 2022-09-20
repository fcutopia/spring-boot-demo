package com.fc.springboot.service;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.jetty.util.ajax.JSON;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class HbaseService {

    /**
     * 表名
     */

    // static String tableName = "yzx_dw:user_feature_v2";
    static String tableName = "yzx_dw:user_feature_v2_test";
    static Connection connection = null;
    static String row_key = "002c13d5a8bbf0af726f995c9f0d8605";

    //编码常量
    private static final String ENCODING = "utf-8";


    public static void main(String[] args) {
        try {
            init();
            // putRow(connection, "yzx_dw:user_feature_v2", "002c13d5a8bbf0af726f995c9f0d8605", "inf", "ft_lbs_8w_day_cnt", "56");
            // putRow(connection, "yzx_dw:user_feature_v2", "002c13d5a8bbf0af726f995c9f0d8604", "inf", "ft_lbs_12w_rest_day_cnt", "20");
            // putRow(connection, "yzx_dw:user_feature_v2", "002c13d5a8bbf0af726f995c9f0d8603", "inf", "ft_app_act_30d_short_video_times_diff_m_llm", "758");
            //putRow(connection, "yzx_dw:user_feature_v2", "002c13d5a8bbf0af726f995c9f0d8602", "inf", "ft_app_ins_current_lite_tool_cnt", "1");
            //putRow(connection, "yzx_dw:user_feature_v2", "002c13d5a8bbf0af726f995c9f0d8601", "inf", "ft_lbs_4w_weekly_day_avg", "7");
            // getRow(connection, tableName, row_key, "inf", "ft_lbs_8w_day_cnt");
            //queryBatch();
            querySingle();
           // getStartRowFromEndRow("yzx_dw:id_mapping_pnmd5_sha256_2gid","0000","08999d2b");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据startKey和endKey的范围匹配数据
     * @param tableName
     * @param startKey
     * @param stopKey
     * @return
     */
    public static List<Result> getStartRowFromEndRow(final String tableName, final String startKey, final String stopKey) {
        List<Result> list = new ArrayList<>();

        try {
            init();
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            // 开始的key
            scan.setStartRow(startKey.getBytes(ENCODING));
            // 结束的key
            scan.setStopRow(stopKey.getBytes(ENCODING));
            scan.addColumn("inf".getBytes(), "pnmd5".getBytes());
            ResultScanner rscanner = table.getScanner(scan);
           // rscanner.spliterator().getExactSizeIfKnown()
            convertScanner(rscanner);
           /* for (Result result : rscanner) {
                list.add(result);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> convertScanner(ResultScanner scanner) {
        List<String> pns = new ArrayList<>();
        try {
            Iterator iterator = scanner.iterator();
            while (iterator.hasNext()) {
                Result result = (Result) iterator.next();
                for (Cell cell : result.rawCells()) {
                    if (cell != null) {
                        //列名
                        String tag = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                        //列值
                        String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                        pns.add(value);
                    }
                }
            }
            return pns;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }


    public static JSONObject queryBatchTags(List<String> idvalues) {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        Set<String> tags = new HashSet<>();
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        List<Get> getList = new ArrayList<>();
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            idvalues.forEach(o -> {
                Get get = new Get(o.getBytes());
                get.addFamily("inf".getBytes());
                getList.add(get);
            });
            Result[] results = table.get(getList);
            for (Result result : results) {
                String rowKey = Bytes.toString(result.getRow());
                Map<String, String> map = new HashMap<>();
                for (Cell cell : result.rawCells()) {
                    if (cell != null) {
                        //String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                        //String value1 = Bytes.toString(CellUtil.cloneValue(cell));
                        String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                        String tag = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                        map.put(tag, value);
                        tags.add(tag);
                    }
                }
                resultMap.put(rowKey, map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put("tags", JSON.toString(tags));
            jsonObject.put("data", JSON.toString(resultMap));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;


    }


    public static void querySingle() {
        String id = "21aa5a788edc79674458569e68fce55f";
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(id.getBytes());
            get.addColumn("inf".getBytes(), "huawei".getBytes());
            Result result = table.get(get);
            String rowKey = Bytes.toString(result.getRow());
            System.out.println("rowkey = " + rowKey);

            String value = "";
            for (Cell cell : result.rawCells()) {
                if (cell != null) {
                    value = Bytes.toString(CellUtil.cloneValue(cell));
                    if (StringUtils.isNotBlank(value)) {
                        System.out.println(value);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, Map<String, String>> queryBatch() {

        Map<String, Map<String, String>> resultMap = new HashMap<>();
        List<Get> getList = new ArrayList<>();
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            List<String> rowKeys = new ArrayList<String>();
            rowKeys.add("3112d71d7515e1ba1a66e627c901653e");
            rowKeys.add("4b536fb97c171aa59cc785afe40b8043");
            rowKeys.add("5be712900071877770464504a4a72090");
            rowKeys.forEach(o -> {
                Get get = new Get(o.getBytes());
                get.addFamily("inf".getBytes());
                getList.add(get);
            });
            Result[] results = table.get(getList);
            for (Result result : results) {
                String rowKey = Bytes.toString(result.getRow());
                Map<String, String> map = new HashMap<>();
                for (Cell cell : result.rawCells()) {
                    if (cell != null) {
                        //String value = Bytes.toString(CellUtil.cloneValue(cell));
                        //System.out.println(value);
                        String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                        String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                        String tag = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                        map.put(tag, value);
                        System.out.println(row);
                    }
                }
                resultMap.put(rowKey, map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * @param hBaseConn
     * @param tableName
     * @param rowkey    行
     * @param cfName    列簇
     * @param qualifer  列名
     */
    public static void getRow(Connection hBaseConn, String tableName, String rowkey, String cfName, String qualifer) {
        try {
            Table table = hBaseConn.getTable(TableName.valueOf(tableName));
            Get get = new Get(rowkey.getBytes());
            get.addColumn(cfName.getBytes(), qualifer.getBytes());
            Result result = table.get(get);
            System.out.println("row" + new String(result.getRow()));
            for (Cell cell : result.rawCells()) {
                System.out.println("获取到的值" + Bytes.toString(CellUtil.cloneValue(cell)));
            }
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入数据
     *
     * @param tableName
     * @param rowkey
     * @param cfName
     * @param qualifer
     * @param data
     * @return
     */
    public static boolean putRow(Connection hBaseConn, String tableName, String rowkey, String cfName, String qualifer, String data) {
        try (Table table = hBaseConn.getTable(TableName.valueOf(tableName))) {
            Put put = new Put(Bytes.toBytes(rowkey));
            put.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualifer), Bytes.toBytes(data));
            put.addColumn(Bytes.toBytes(cfName), Bytes.toBytes("age_0_17"), Bytes.toBytes("17"));
            put.addColumn(Bytes.toBytes(cfName), Bytes.toBytes("age_18_35"), Bytes.toBytes("35"));
            table.put(put);
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public static void init() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        //测试环境
        conf.set("hbase.zookeeper.quorum", "192.168.198.151,192.168.198.152,192.168.198.153");
        //正式
        //conf.set("hbase.zookeeper.quorum", "172.18.198.76,172.18.198.77,172.18.198.78");


        conf.set("hbase.zookeeper.property.clientPort", "2181");
        // connection = ConnectionFactory.createConnection(conf, getCountThreadPool());
    }

    public static ThreadPoolExecutor getCountThreadPool() {
        ThreadPoolExecutor countThreadPool = new ThreadPoolExecutor(
                64, 64, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000000),
                new ThreadFactory() {

                    final AtomicLong threadNumber = new AtomicLong(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "hbase-query-thread-" + threadNumber.getAndIncrement());
                        t.setDaemon(true);
                        t.setPriority(Thread.NORM_PRIORITY);
                        return t;
                    }

                }, new ThreadPoolExecutor.CallerRunsPolicy());
        return countThreadPool;
    }

       /*Table table = connection.getTable(TableName.valueOf(tableName));
    // 创建Put实例，并且指定rowKey
    Put put = new Put(Bytes.toBytes("row-1"));
            put.addColumn("inf".getBytes(), "ft_lbs_8w_day_cnt".getBytes(), "56".getBytes());
            table.put(put);*/


}
