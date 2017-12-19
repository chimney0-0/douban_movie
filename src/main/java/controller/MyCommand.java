package controller;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import spider.MovieProcessor;
import us.codecraft.webmagic.Spider;
import utils.ReadProperties;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyCommand {

    static Logger logger = Logger.getLogger(MyCommand.class);

    public static void read() {
        logger.info("开始读取配置...");
        int interval = Integer.parseInt(new ReadProperties().interval);
        int flag = Integer.parseInt(new ReadProperties().flag);
        if (flag == 0) {
            logger.info("当前状态为停止，将在" + interval + "分钟后重新读取配置");
        } else if (flag == 1) {
            logger.info("开始跑取...");
            run();
            logger.info("json文件已生成，将在" + interval + "分钟后重新读取配置");
        } else {
            logger.info("flag填写不正确，将在" + interval + "分钟后重新读取配置");
        }
        try {
            Thread.sleep(interval * 1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        read();
    }

    public static void run() {
        String path = new ReadProperties().jsonpath;
        try {
            MovieProcessor processor = new MovieProcessor();
            String json = processor.crawlMovie(processor);
            File file = new File(path);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            file.createNewFile();
            BufferedWriter br = new BufferedWriter(new FileWriter(new File(path)));
            br.write(json);
            br.close();
        } catch (IOException e) {
            logger.info(path + "路径有误！");
        }
    }


    public static void main(String[] args) {
        BasicConfigurator.configure();
        read();
    }


}
