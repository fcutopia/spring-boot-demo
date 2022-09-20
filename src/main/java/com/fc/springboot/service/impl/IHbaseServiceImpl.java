package com.fc.springboot.service.impl;


//import com.swstsoft.constant.ConstantUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
//import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;*/
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;

import com.fc.springboot.service.IHbaseService;

/**
 * @ClassName IHbaseServiceImpl
 * @Description
 * @Author fc
 * @Date 2022/5/5 5:20 下午
 * @Version 1.0
 **/
public class IHbaseServiceImpl /*implements IHbaseService*/ {

   /* @Autowired
    private HbaseTemplate hbaseTemplate;

    //编码常量
    private static final String ENCODING = "utf-8";

    @Override
    public void createTable(String tableName, String... familyNames) throws IOException {
        Configuration configuration = hbaseTemplate.getConfiguration();
        HBaseAdmin admin = new HBaseAdmin(configuration);
        HTableDescriptor htd = new HTableDescriptor(tableName);
        for (String st : familyNames) {
            htd.addFamily(new HColumnDescriptor(st));
        }
        admin.createTable(htd);
        admin.close();
    }

    @Override
    public void deleteTable(String tableName) throws IOException {
        Configuration configuration = hbaseTemplate.getConfiguration();
        HBaseAdmin admin = new HBaseAdmin(configuration);
        if (!admin.isTableDisabled(tableName)) {
            admin.disableTable(tableName);
        }
        admin.deleteTable(tableName);
        admin.close();
    }

    @Override
    public List<Result> scaner(final String tableName) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                Scan scan = new Scan();
                ResultScanner rs = table.getScanner(scan);
                for (Result result : rs) {
                    list.add(result);
                }
                return list;
            }
        });
    }

    @Override
    public Result getRow(final String tableName, final String rowKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<Result>() {
            @Override
            public Result doInTable(HTableInterface table) throws Throwable {
                Get get = new Get(rowKey.getBytes(ENCODING));
                return table.get(get);
            }
        });
    }

    @Override
    public List<Result> getRegexRow(final String tableName, final String regxKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, rc);
                Scan scan = new Scan();
                scan.setFilter(rowFilter);
                ResultScanner rs = table.getScanner(scan);
                for (Result result : rs) {
                    list.add(result);
                }
                return list;
            }
        });
    }

    @Override
    public List<Result> getRegexRow(final String tableName, final String regxKey, final int num) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rf = new RowFilter(CompareOp.EQUAL, rc);
                if (num > 0) {
                    // 过滤获取的条数
                    Filter filterNum = new PageFilter(num);// 每页展示条数
                    fl.addFilter(filterNum);
                } // 过滤器的添加
                fl.addFilter(rf);
                Scan scan = new Scan();
                scan.setFilter(fl);// 为查询设置过滤器的list
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }
        });
    }


    @Override
    public List<Result> getStartRowAndEndRow(final String tableName, final String startKey, final String stopKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                // 过滤器的添加
                Scan scan = new Scan();
                scan.setStartRow(startKey.getBytes(ENCODING));// 开始的key
                scan.setStopRow(stopKey.getBytes(ENCODING));// 结束的key
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }
        });
    }

    @Override
    public List<Result> getRegexRow(final String tableName, final String startKey, final String stopKey, final String regxKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                // 设置正则过滤器
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rf = new RowFilter(CompareOp.EQUAL, rc);
                // 过滤器的添加
                Scan scan = new Scan();
                scan.setStartRow(startKey.getBytes(ENCODING));// 开始的key
                scan.setStopRow(stopKey.getBytes(ENCODING));// 结束的key
                scan.setFilter(rf);// 为查询设置过滤器的list
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }
        });
    }


    @Override
    public List<Result> getRegexRow(final String tableName, final String startKey, final String stopKey, final String regxKey, final int num) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                // 设置正则过滤器
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rf = new RowFilter(CompareOp.EQUAL, rc);
                if (num > 0) {
                    // 过滤获取的条数
                    Filter filterNum = new PageFilter(num);// 每页展示条数
                    fl.addFilter(filterNum);
                }
                // 过滤器的添加
                fl.addFilter(rf);
                // 过滤器的添加
                Scan scan = new Scan();
                scan.setStartRow(startKey.getBytes(ENCODING));// 开始的key
                scan.setStopRow(stopKey.getBytes(ENCODING));// 结束的key
                scan.setFilter(fl);// 为查询设置过滤器的list
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }
        });
    }

    @Override
    public Boolean addData(final String rowKey, final String tableName, final String familyName, final String[] column, final String[] value) {
        return hbaseTemplate.execute(tableName, new TableCallback<Boolean>() {
            @Override
            public Boolean doInTable(HTableInterface table) throws Throwable {
                Put put = new Put(Bytes.toBytes(rowKey));
                // 设置rowkey
                for (int j = 0; j < column.length; j++) {
                    put.add(Bytes.toBytes(familyName), Bytes.toBytes(column[j]), Bytes.toBytes(value[j]));
                }
                table.put(put);
                return true;
            }
        });
    }

    @Override
    public Boolean addDataBatch(final String tableName, final String familyName, List<Map<String, String>> list) {
        return hbaseTemplate.execute(tableName, new TableCallback<Boolean>() {
            @Override
            public Boolean doInTable(HTableInterface table) throws Throwable {
                List<Put> puts = new ArrayList<>();
                for (Map<String, String> map : list) {
                    // 设置rowkey
                    Put put = new Put(Bytes.toBytes(map.get("rowKey")));
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        if ("rowKey".equals(entry.getKey()))
                            continue;
                        put.add(Bytes.toBytes(familyName), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
                    }
                    puts.add(put);
                }
                table.put(puts);
                return true;
            }
        });
    }


    @Override
    public Boolean delRecord(final String tableName, final String... rowKeys) {
        return hbaseTemplate.execute(tableName, new TableCallback<Boolean>() {
            @Override
            public Boolean doInTable(HTableInterface table) throws Throwable {
                List<Delete> list = new ArrayList<>();
                for (String rowKey : rowKeys) {
                    Delete del = new Delete(Bytes.toBytes(rowKey));
                    list.add(del);
                }
                table.delete(list);
                return true;
            }
        });
    }

    @Override
    public Boolean updateTable(final String tableName, final String rowKey, final String familyName, final String[] column, final String[] value) throws IOException {
        return hbaseTemplate.execute(tableName, new TableCallback<Boolean>() {
            @Override
            public Boolean doInTable(HTableInterface table) throws Throwable {
                Put put = new Put(Bytes.toBytes(rowKey));
                for (int j = 0; j < column.length; j++) {
                    put.add(Bytes.toBytes(familyName), Bytes.toBytes(column[j]), Bytes.toBytes(value[j]));
                }
                table.put(put);
                return true;
            }
        });
    }

    @Override
    public Boolean updateTableBatch(final String tableName, final String familyName, List<Map<String, String>> list) {
        return hbaseTemplate.execute(tableName, new TableCallback<Boolean>() {
            @Override
            public Boolean doInTable(HTableInterface table) throws Throwable {
                List<Put> puts = new ArrayList<>();
                for (Map<String, String> map : list) {
                    // 设置rowkey
                    Put put = new Put(Bytes.toBytes(map.get("rowKey")));
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        put.add(Bytes.toBytes(familyName), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
                    }
                    puts.add(put);
                }
                table.put(puts);
                return true;
            }
        });
    }

    @Override
    public Result getNewRow(final String tableName) {
        return hbaseTemplate.execute(tableName, new TableCallback<Result>() {
            @Override
            public Result doInTable(HTableInterface table) throws Throwable {
                Filter filterNum = new PageFilter(1);// 每页展示条数
                Scan scan = new Scan();
                scan.setFilter(filterNum);
                scan.setReversed(true);
                ResultScanner scanner = table.getScanner(scan);
                return scanner.next();
            }
        });
    }

    @Override
    public Result getNewRow(final String tableName, final String regxKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<Result>() {
            @Override
            public Result doInTable(HTableInterface table) throws Throwable {
                FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rf = new RowFilter(CompareOp.EQUAL, rc);
                Filter filterNum = new PageFilter(1);// 每页展示条数
                fl.addFilter(rf);
                fl.addFilter(filterNum);
                Scan scan = new Scan();
                scan.setFilter(fl);
                scan.setReversed(true);
                ResultScanner scanner = table.getScanner(scan);
                return scanner.next();
            }
        });
    }

    @Override
    public List<String> queryKeys(final String tableName, final String regxKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<String>>() {
            List<String> list = new ArrayList<>();

            @Override
            public List<String> doInTable(HTableInterface table) throws Throwable {
                PrefixFilter filter = new PrefixFilter(regxKey.getBytes(ENCODING));
                Scan scan = new Scan();
                scan.setFilter(filter);
                ResultScanner scanner = table.getScanner(scan);
                for (Result rs : scanner) {
                    list.add(new String(rs.getRow()));
                }
                return list;
            }
        });
    }

    @Override
    public long incrQualifier(final String tableName, final String cf, final String rowKey, final String column, final long num) {
        return hbaseTemplate.execute(tableName, new TableCallback<Long>() {
            @Override
            public Long doInTable(HTableInterface table) throws Throwable {
                long qualifie = table.incrementColumnValue(rowKey.getBytes(ENCODING), cf.getBytes(ENCODING), column.getBytes(ENCODING), num);
                return qualifie;
            }
        });
    }

    @Override
    public List<Result> getByColumnsValueFilter(String tableName, String cf, String[] column, String[] value) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();
            FilterList filterList = new FilterList();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                // 要查询的字段数组集
                Scan scan = new Scan();
                for (int i = 0; i < column.length; i++) {
                    SingleColumnValueFilter b = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes(column[i]), CompareOp.EQUAL, new RegexStringComparator(value[i]));
                    filterList.addFilter(b);
                }
                scan.setFilter(filterList);
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }
        });
    }

    @Override
    public List<Result> getByColumnsValueFilterByPage(String startRowKey, int pageSize, String tableName, String cf, String[] column, String[] value) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();
            FilterList filterList = new FilterList();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                // 要查询的字段数组集
                Scan scan = new Scan();
                //与时间相关的提出来
                for (int i = 0; i < column.length; i++) {
//                    起始时间和开始时间的范围查询
                    if (column[i].equals("startTime")) {
                        SingleColumnValueFilter startTime = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes("createTimeString"), CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(value[i]));
                        filterList.addFilter(startTime);
                    } else if (column[i].equals("endTime")) {
                        SingleColumnValueFilter endTime = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes("createTimeString"), CompareOp.LESS_OR_EQUAL, Bytes.toBytes(value[i]));
                        filterList.addFilter(endTime);
                    } else {
                        SingleColumnValueFilter b = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes(column[i]), CompareOp.EQUAL, new RegexStringComparator(value[i]));
                        filterList.addFilter(b);
                    }
                }
                scan.setStartRow(Bytes.toBytes(startRowKey));
                PageFilter pageFilter = new PageFilter(pageSize);
                filterList.addFilter(pageFilter);
                scan.setFilter(filterList);
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }
        });
    }

    @Override
    public List<Result> getByTimeValue(String tableName, String cf, String[] column, String[] value) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();
            FilterList filterList = new FilterList();

            @Override
            public List<Result> doInTable(HTableInterface hTableInterface) throws Throwable {
                // 要查询的字段数组集
                Scan scan = new Scan();
                //与时间相关的提出来
                for (int i = 0; i < column.length; i++) {
//                    起始时间和开始时间的范围查询
                    if (column[i].equals("startTime")) {
                        SingleColumnValueFilter startTime = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes("createTimeString"), CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(value[i]));
                        filterList.addFilter(startTime);
                    } else if (column[i].equals("endTime")) {
                        SingleColumnValueFilter endTime = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes("createTimeString"), CompareOp.LESS_OR_EQUAL, Bytes.toBytes(value[i]));
                        filterList.addFilter(endTime);
                    } else {
                        SingleColumnValueFilter b = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes(column[i]), CompareOp.EQUAL, new RegexStringComparator(value[i]));
                        filterList.addFilter(b);
                    }
                }
                scan.setFilter(filterList);
                ResultScanner rscanner = hTableInterface.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }
        });
    }

    @Override
    public List<Result> getBySingleColumnValueFilter(final String tableName, final String cf, final String column, final String value) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                // 要查询的字段
                Scan scan = new Scan();
                SingleColumnValueFilter b = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes(column), CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(value)));
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, b);
                scan.setFilter(filterList);
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }
        });
    }

    *//**
     * 分页获取StartRowKey
     *
     * @param tableName
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     *//*
    public String getStartRowKey(String tableName, Integer pageNumber, Integer pageSize) throws Exception {
        if (pageNumber <= 1) {
            return null;
        }
        String startRowKey = null;
        for (int i = 1; i < pageNumber; i++) {
            String finalStartRowKey = startRowKey;
            // TODO 待优化
            List<Result> results = hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
                @Override
                public List<Result> doInTable(HTableInterface table) throws Throwable {
                    Scan scan = new Scan();
                    if (!StringUtils.isBlank(finalStartRowKey)) {
                        scan.setStartRow(finalStartRowKey.getBytes());
                    }
                    Filter pageFilter = new PageFilter(pageSize + 1);
                    scan.setFilter(pageFilter);
                    ResultScanner scanner = table.getScanner(scan);
                    Iterator<Result> iterator = scanner.iterator();
                    List<Result> list = new ArrayList<>();
                    while (iterator.hasNext()) {
                        list.add(iterator.next());
                    }
                    return list;
                }
            });
            for (Result result : results) {
                startRowKey = Bytes.toString(result.getRow());
            }
        }
        return startRowKey;
    }

    *//**
     * 列表所有数据
     *
     * @param tableName
     * @return
     *//*
    @Override
    public List<Map<String, String>> listAll(String tableName) {
        List<Result> scaner = scaner(tableName);
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < scaner.size(); i++) {
            Map<String, String> map = mapRow(scaner.get(i), i);
            list.add(map);
        }
        return list;
    }

    *//**
     * 利用HBase协处理器Coprocessor,统计行
     *
     * @param
     * @return
     * @throws Throwable
     *//*
    public Long rowCountByCoprocessor(String tableName,String cf,String[] column, String[] value) throws Throwable {
        Configuration configuration = hbaseTemplate.getConfiguration();
//        //提前创建connection和conf
//        HBaseAdmin admin = new HBaseAdmin(configuration);
//        TableName name = TableName.valueOf(tablename);
//        //先disable表，添加协处理器后再enable表
//        admin.disableTable(name);
//        HTableDescriptor descriptor = admin.getTableDescriptor(name);
//        String coprocessorClass = "org.apache.hadoop.hbase.coprocessor.AggregateImplementation";
//        if (!descriptor.hasCoprocessor(coprocessorClass)) {
//            descriptor.addCoprocessor(coprocessorClass);
//        }
//        admin.modifyTable(name, descriptor);
//        admin.enableTable(name);
        Scan scan = new Scan();
        FilterList filterList = new FilterList();
        //与时间相关的提出来
        for (int i = 0; i < column.length; i++) {
//                    起始时间和开始时间的范围查询
            if (column[i].equals("startTime")) {
                SingleColumnValueFilter startTime = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes("createTimeString"), CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(value[i]));
                filterList.addFilter(startTime);
            } else if (column[i].equals("endTime")) {
                SingleColumnValueFilter endTime = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes("createTimeString"), CompareOp.LESS_OR_EQUAL, Bytes.toBytes(value[i]));
                filterList.addFilter(endTime);
            } else {
                SingleColumnValueFilter b = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes(column[i]), CompareOp.EQUAL, new RegexStringComparator(value[i]));
                filterList.addFilter(b);
            }
        }
        scan.setFilter(filterList);

        AggregationClient aggregationClient = new AggregationClient(configuration);
        long rowCount = aggregationClient.rowCount(TableName.valueOf(tableName), new LongColumnInterpreter(), scan);
        return rowCount;
    }

    *//**
     * Result数据转成Map
     *
     * @param result
     * @param rowNum
     * @return
     *//*
    public Map<String, String> mapRow(Result result, int rowNum) {
        if (result.isEmpty()) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put(ConstantUtil.ROW_KRY, Bytes.toString(result.getRow()));
        NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> ResultMap = result.getMap();
        Set<Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>>> resultEntries = ResultMap.entrySet();
        for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> resultEntry : resultEntries) {
            NavigableMap<byte[], NavigableMap<Long, byte[]>> familyMap = resultEntry.getValue();
            Set<Map.Entry<byte[], NavigableMap<Long, byte[]>>> familyEntries = familyMap.entrySet();
            for (Map.Entry<byte[], NavigableMap<Long, byte[]>> family : familyEntries) {
                NavigableMap<Long, byte[]> columnMap = family.getValue();
                Set<Map.Entry<Long, byte[]>> columnEntries = columnMap.entrySet();
                for (Map.Entry<Long, byte[]> column : columnEntries) {
                    map.put(Bytes.toString(family.getKey()), Bytes.toString(column.getValue()));
                }
            }
        }
        return map;
    }
*/
}
