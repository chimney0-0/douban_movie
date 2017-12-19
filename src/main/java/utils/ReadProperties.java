package utils;

import java.io.*;
import java.util.Properties;

/**
 * 获取配置
 */
public class ReadProperties {

    public String flag;
    public String interval;
    public String jsonpath;


    public ReadProperties() {

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
            jsonpath = properties.getProperty("path");
            jsonpath = new String(jsonpath.getBytes("ISO8859-1"), "utf-8");//路径转化为utf-8编码
        } catch (FileNotFoundException e) {
            System.out.println(path + " 找不到配置文件！");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
