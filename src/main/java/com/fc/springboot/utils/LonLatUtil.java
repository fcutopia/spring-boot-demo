//package com.fc.springboot.utils;
//
//import com.opencsv.CSVWriterBuilder;
//import com.opencsv.ICSVWriter;
//import com.yzx.risk.control.constants.Constants;
//import org.apache.commons.io.output.FileWriterWithEncoding;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author: luoyihua     
// * @date: 2021/6/1 8:49 下午  
// * @description: TODO 
// */
//public class LonLatUtil {
//
//    public static double pi = 3.1415926535897932384626;
//    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
//    public static double a = 6378245.0;
//    public static double ee = 0.00669342162296594323;
//
//    public static double transformLat(double x, double y) {
//        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
//        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
//        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
//        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
//        return ret;
//    }
//
//    public static double transformLon(double x, double y) {
//        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
//        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
//        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
//        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
//        return ret;
//    }
//
//    public static double[] transform(double lat, double lon) {
//        if (outOfChina(lat, lon)) {
//            return new double[]{lat, lon};
//        }
//        double dLat = transformLat(lon - 105.0, lat - 35.0);
//        double dLon = transformLon(lon - 105.0, lat - 35.0);
//        double radLat = lat / 180.0 * pi;
//        double magic = Math.sin(radLat);
//        magic = 1 - ee * magic * magic;
//        double sqrtMagic = Math.sqrt(magic);
//        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
//        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
//        double mgLat = lat + dLat;
//        double mgLon = lon + dLon;
//        return new double[]{mgLat, mgLon};
//    }
//
//    /**
//     * 判断是否在中国
//     *
//     * @param lat
//     * @param lon
//     * @return
//     */
//    public static boolean outOfChina(double lat, double lon) {
//        if (lon < 72.004 || lon > 137.8347)
//            return true;
//        if (lat < 0.8293 || lat > 55.8271)
//            return true;
//        return false;
//    }
//
//    /**
//     * 84 ==》 高德
//     *
//     * @param lat
//     * @param lon
//     * @return
//     */
//    public static double[] gps84_To_Gcj02(double lat, double lon) {
//        if (outOfChina(lat, lon)) {
//            return new double[]{lat, lon};
//        }
//        double dLat = transformLat(lon - 105.0, lat - 35.0);
//        double dLon = transformLon(lon - 105.0, lat - 35.0);
//        double radLat = lat / 180.0 * pi;
//        double magic = Math.sin(radLat);
//        magic = 1 - ee * magic * magic;
//        double sqrtMagic = Math.sqrt(magic);
//        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
//        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
//        double mgLat = lat + dLat;
//        double mgLon = lon + dLon;
//        return new double[]{mgLat, mgLon};
//    }
//
//    /**
//     * 高德 ==》 84
//     *
//     * @param lon * @param lat * @return
//     */
//    public static double[] gcj02_To_Gps84(double lat, double lon) {
//        double[] gps = transform(lat, lon);
//        double lontitude = lon * 2 - gps[1];
//        double latitude = lat * 2 - gps[0];
//        return new double[]{latitude, lontitude};
//    }
//
//    /**
//     * 高德 == 》 百度
//     *
//     * @param lat
//     * @param lon
//     */
//    public static double[] gcj02_To_Bd09(double lat, double lon) {
//        double x = lon, y = lat;
//        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
//        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
//        double tempLon = z * Math.cos(theta) + 0.0065;
//        double tempLat = z * Math.sin(theta) + 0.006;
//        double[] gps = {tempLat, tempLon};
//        return gps;
//    }
//
//    /**
//     * 百度 == 》 高德
//     *
//     * @param lat
//     * @param lon
//     */
//    public static double[] bd09_To_Gcj02(double lat, double lon) {
//        double x = lon - 0.0065, y = lat - 0.006;
//        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
//        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
//        double tempLon = z * Math.cos(theta);
//        double tempLat = z * Math.sin(theta);
//        double[] gps = {tempLat, tempLon};
//        return gps;
//    }
//
//    /**
//     * 84 == 》 百度
//     *
//     * @param lat
//     * @param lon
//     * @return
//     */
//    public static double[] gps84_To_bd09(double lat, double lon) {
//        double[] gcj02 = gps84_To_Gcj02(lat, lon);
//        double[] bd09 = gcj02_To_Bd09(gcj02[0], gcj02[1]);
//        return bd09;
//    }
//
//    /**
//     * 百度 == 》 84
//     *
//     * @param lat
//     * @param lon
//     * @return
//     */
//    public static double[] bd09_To_gps84(double lat, double lon) {
//        double[] gcj02 = bd09_To_Gcj02(lat, lon);
//        double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);
//        //保留小数点后六位
//        gps84[0] = retain6(gps84[0]);
//        gps84[1] = retain6(gps84[1]);
//        return gps84;
//    }
//
//
//    /**
//     * 保留小数点后六位
//     *
//     * @param num
//     * @return
//     */
//    private static double retain6(double num) {
//        String result = String.format("%.6f", num);
//        return Double.valueOf(result);
//    }
//
//    public static void main(String[] args) {
//
//        String bdFileName = "/Users/fc/Desktop/lon&lat-rand1w.txt";
//        String gdFileName = "/Users/fc/Desktop/gd.txt";
//        readFile(bdFileName, gdFileName);
//
//        if (1 == 1) {
//            return;
//        }
//        test();
//
//
//        //lon:经度 ：115.007001
//        //lat:纬度 ：36.704997
//        //geohash
//        String str = "ww9bc1c";
//
//        //geohash->gps
//        GeoHash geoHash = GeoHash.decode(str.substring(0, Math.min(12, str.length())));
//        System.out.println("geohash->gps: " + geoHash.getLon() + "," + geoHash.getLat());
//
//        //gps->bd
//        double[] doubles = LonLatUtil.gps84_To_bd09(geoHash.getLon(), geoHash.getLat());
//        System.out.println("gps->bd: " + doubles[0] + "," + doubles[1]);
//
//        //gps -> gd
//        double[] doubles1 = LonLatUtil.gps84_To_Gcj02(geoHash.getLon(), geoHash.getLat());
//        System.out.println("gps-> gd: " + doubles1[0] + "," + doubles1[1]);
//
//        //bd->gps
//        double[] str1 = bd09_To_gps84(doubles[0], doubles[1]);
//
//        System.out.println("bd->gps: " + str1[0] + "," + str1[1]);
//
//        //gps->geohash  （lat,long）
//        System.out.println(GeoHash.encode(str1[1], str1[0]).toHashString());
//
//
//        System.out.println(getGeoHash(3, "115.007,36.705"));
//        ;
//    }
//
//    public static void test() {
//        //bd
//        double lon = 115.01308985335064;
//        double lat = 36.71125986003724;
//        //bd->gps
//        double[] bd_gps = bd09_To_gps84(lat, lon);
//        System.out.println(bd_gps[0] + "," + bd_gps[1]);
//    }
//
//
//    public static String getGeoHash(int type, String value) {
//        Double lon = Double.valueOf(value.split(",")[0]);
//        Double lat = Double.valueOf(value.split(",")[1]);
//
//
//        if (CommonEnum.ParamType.lonlat_gps.val() == type) {
//            return GeoHash.encode(lat, lon, Constants.GEOHASH_PERCISION).toHashString();
//        }
//        if (CommonEnum.ParamType.lonlat_baidu.val() == type) {
//            double[] lonAndLat = LonLatUtil.bd09_To_gps84(lon, lat);
//            return GeoHash.encode(lonAndLat[1], lonAndLat[0], Constants.GEOHASH_PERCISION).toHashString();
//        }
//        if (CommonEnum.ParamType.lonlat_gaode.val() == type) {
//            double[] lonAndLat = LonLatUtil.gcj02_To_Gps84(lon, lat);
//            return GeoHash.encode(lonAndLat[1], lonAndLat[0], Constants.GEOHASH_PERCISION).toHashString();
//        }
//        return null;
//    }
//
//    public static String readFile(String fileName, String destFileName) {
//        // 使用ArrayList来存储每行读取到的字符串
//        StringBuffer sbf = new StringBuffer();
//        try {
//            File file = new File(fileName);
//            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
//            BufferedReader bf = new BufferedReader(inputReader);
//            // 按行读取字符串
//            String str;
//            List<String[]> list = new ArrayList<>();
//            while ((str = bf.readLine()) != null) {
//                System.out.println(str);
//                String[] strings = str.split(",");
//                String[] dataLine = new String[2];
//
//                Double lon = Double.valueOf(strings[0].replace("\"", ""));
//                Double lat = Double.valueOf(strings[1].replace("\"", ""));
//               // double[] bd_gps = bd09_To_gps84(lat, lon);
//                double[] bd_gps = bd09_To_Gcj02(lat, lon);
//
//                dataLine[0] = String.valueOf(bd_gps[1]);
//                dataLine[1] = String.valueOf(bd_gps[0]);
//                list.add(dataLine);
//            }
//            bf.close();
//            inputReader.close();
//            writeCsvFile(destFileName, list);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return sbf.toString();
//    }
//
//    public static void writeCsvFile(String filePath, List<String[]> list) {
//        ICSVWriter icsvWriter = null;
//        try {
//            icsvWriter = new CSVWriterBuilder(new FileWriterWithEncoding(filePath, "utf-8"))
//                    .withSeparator(ICSVWriter.DEFAULT_SEPARATOR) // 分隔符
//                    .withQuoteChar(ICSVWriter.NO_QUOTE_CHARACTER) // 不使用引号
//                    .build();
//            icsvWriter.writeAll(list);
//            icsvWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                icsvWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
