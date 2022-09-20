package com.fc.springboot.service;


import org.apache.hadoop.hbase.client.Result;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @ClassName IHbaseService
 * @Description
 * @Author fc
 * @Date 2022/5/5 5:19 下午
 * @Version 1.0
 **/
public interface IHbaseService {

    /**
     * 创建表
     *
     * @param tableName
     * @param familyNames 列簇
     * @throws IOException
     */
    public void createTable(String tableName, String... familyNames) throws IOException;

    /**
     * 删除表
     *
     * @param tableName
     * @throws IOException
     */
    public void deleteTable(String tableName) throws IOException;

    /**
     * 查询全表的数据
     *
     * @param tablename
     * @return
     */
    public List<Result> scaner(String tablename);

    /**
     * 根据rowKey查询单条记录
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    public Result getRow(String tableName, String rowKey);

    /**
     * 根据regxKey正则匹配数据
     *
     * @param tableName
     * @param regxKey
     * @return
     */
    public List<Result> getRegexRow(String tableName, String regxKey);

    /**
     * 根据regxKey正则匹配数据,取出num条
     *
     * @param tableName
     * @param regxKey
     * @param num
     * @return
     */
    public List<Result> getRegexRow(String tableName, String regxKey, int num);

    /**
     * 根据startKey和endKey的范围匹配数据
     *
     * @param tableName
     * @param startKey
     * @param stopKey
     * @return
     */
    public List<Result> getStartRowAndEndRow(String tableName, String startKey, String stopKey);

    /**
     * 确定startKey和endKey的范围，根据regKey匹配数据
     *
     * @param tableName
     * @param startKey
     * @param stopKey
     * @param regxKey
     * @return
     */
    public List<Result> getRegexRow(String tableName, String startKey, String stopKey, String regxKey);

    /**
     * 确定startKey和endKey的范围，根据regKey匹配数据,取出num条
     *
     * @param tableName
     * @param startKey
     * @param stopKey
     * @param regxKey
     * @param num
     * @return
     */
    public List<Result> getRegexRow(String tableName, String startKey, String stopKey, String regxKey, int num);

    /**
     * 添加数据
     *
     * @param rowKey
     * @param tableName
     * @param column
     * @param value
     */
    public Boolean addData(String rowKey, String tableName, String familyName, String[] column, String[] value);

    /**
     * 批量添加数据
     *
     * @param tableName
     * @param familyName
     * @param list
     * @return
     */
    public Boolean addDataBatch(final String tableName, final String familyName, List<Map<String, String>> list);

    /**
     * 删除记录
     *
     * @param tableName
     * @param rowKeys
     */
    public Boolean delRecord(String tableName, String... rowKeys);

    /**
     * 修改一条数据
     *
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param column
     * @param value
     * @throws IOException
     */
    public Boolean updateTable(String tableName, String rowKey, String familyName, String column[], String value[]) throws IOException;

    /**
     * 批量修改数据
     *
     * @param tableName
     * @param familyName
     * @param list
     * @return
     */
    public Boolean updateTableBatch(final String tableName, final String familyName, List<Map<String, String>> list);

    /**
     * 查找最新的一条数据,或者说倒序查询
     *
     * @param tableName
     * @return
     */
    public Result getNewRow(String tableName);

    /**
     * 正则查出所有匹配的key
     *
     * @param tableName
     * @param regxKey
     * @return
     */
    public List<String> queryKeys(String tableName, String regxKey);

    /**
     * 增加表中对应字段的值
     *
     * @param tableName
     * @param cf
     * @param rowKey
     * @param column
     * @param num
     * @return
     */
    long incrQualifier(String tableName, String cf, String rowKey, String column, long num);

    /**
     * 查找最新的一条数据,或者说倒序查询
     *
     * @param tableName
     * @param regxKey
     * @return
     */
    Result getNewRow(String tableName, String regxKey);

    /**
     * 根据列值查询（类似外键关联）
     *
     * @param tableName
     * @param cf
     * @param column
     * @param value
     * @return
     */
    List<Result> getBySingleColumnValueFilter(String tableName, String cf, String column, String value);

    /**
     * 根据多列列值查询（类似外键关联）
     *
     * @param tableName
     * @param cf
     * @param column
     * @param value
     * @return
     */
    List<Result> getByColumnsValueFilter(String tableName, String cf, String[] column, String[] value);

    /**
     * 根据多列列值查询分页
     *
     * @param tableName
     * @param cf
     * @param column
     * @param value
     * @return
     */
    List<Result> getByColumnsValueFilterByPage(String startRowKey, int pageSize, String tableName, String cf, String[] column, String[] value);

    /**
     * 根据时间查询数据
     *
     * @param tableName
     * @param cf
     * @param column
     * @param value
     * @return
     */
    List<Result> getByTimeValue(String tableName, String cf, String[] column, String[] value);

    /**
     * 利用HBase协处理器Coprocessor,统计行
     *
     * @param tablename
     * @return
     * @throws Throwable
     */
    public Long rowCountByCoprocessor(String tablename, String cf, String[] column, String[] value) throws Throwable;

    /**
     * Result数据转成Map
     *
     * @param result
     * @param rowNum
     * @return
     */
    public Map<String, String> mapRow(Result result, int rowNum);

    /**
     * @Description: 通过pageNumber，pageSize，查询起始页
     * @Param: [tableName, pageNumber, pageSize]
     * @return: java.lang.String
     * @Author: zhoujie
     * @Date: 2018/11/8
     */

    String getStartRowKey(String tableName, Integer pageNumber, Integer pageSize) throws Throwable;

    /**
     * 列表所有数据
     *
     * @param tableName
     * @return
     */
    public List<Map<String, String>> listAll(String tableName);


}
