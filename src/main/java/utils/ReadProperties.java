package utils;

import java.io.*;
import java.util.Properties;

/**
 * 获取配置
 */
public class ReadProperties {

    public static String flag;
    public static String interval;
    public static String path;


    static {

        //读取当前目录
        String rootPath = System.getProperty("user.dir");
        //得到配置文件路径
        String path = rootPath + File.separator + "properties.properties";
        try {
            InputStream in = new FileInputStream(path);
            Properties properties = new Properties();
            properties.load(in);
            //获取数据库参数
            flag = properties.getProperty("flag");
            interval = properties.getProperty("interval");
            path = properties.getProperty("path");
        } catch (FileNotFoundException e) {
            System.out.println(path + " 找不到配置文件！");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
