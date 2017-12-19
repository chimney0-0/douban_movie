package spider;

import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieProcessor implements PageProcessor {

    List<Map<String, Object>> records = new ArrayList<>();

    String mainPage = "https://movie.douban.com/people/BBmachtalles/collect";
    Integer totalPage = 1;

    private Site site = Site.me().setCharset("utf-8").setRetryTimes(1).setDomain("www.douban.com")
            .setSleepTime(8000).setRetrySleepTime(10000);

    public void process(Page page) {
        String url = page.getUrl().toString();
        String text = page.getRawText();
        Document document = Jsoup.parse(text);

        //先从首页拿到总页数
        if (url.equals(mainPage)) {
            totalPage = Integer.parseInt(document.select(".paginator .thispage").attr("data-total-page"));
            for (int i = 0; i < totalPage; i++) {
                page.addTargetRequest("https://movie.douban.com/people/BBmachtalles/collect?start=" + (15 * i) + "&sort=time&rating=all&filter=all&mode=grid");
            }
        }
        //对于每个分页，存储条目信息
        else {
            Elements items = document.select(".grid-view .item");
            for (Element item : items) {
                String name = item.select(".title em").text();
                String intro = item.select(".intro").text();
                String date = item.select(".date").text();
                String tags = item.select(".tags").text();
                Integer year = null; //年份
                String nation = ""; //国家地区
                String director = ""; //导演
                String series = ""; //系列

                if (!tags.equals("")) {
                    String[] strings = tags.split(" ");
                    for (String tag : strings) {
                        if (tag.matches("\\d+")) year = Integer.parseInt(tag);
                        else if (tag.matches("=.*?=")) {
                            tag = tag.substring(1, tag.length() - 1);
                            nation = nation.equals("") ? tag : nation + ", " + tag;
                        } else if (tag.matches("【.*?】")) {
                            tag = tag.substring(1, tag.length() - 1);
                            series = series.equals("") ? tag : series + ", " + tag;
                        } else if (!tag.equals("标签:")) {
                            director = director.equals("") ? tag : director + ", " + tag;
                        }
                    }
                }

                Map<String, Object> info = new HashMap<>();
                info.put("name", name);
                info.put("intro", intro);
                info.put("date", date);
                info.put("year", year);
                info.put("director", director);
                info.put("nation", nation);
                info.put("series", series);
                records.add(info);
            }
        }

    }

    public Site getSite() {
        return site;
    }


    //执行一次爬虫任务
    public String crawlMovie(MovieProcessor processor) {
        Spider.create(processor).addUrl(mainPage).thread(3).run();
        return JSON.toJSONString(records);
    }


    public static void main(String[] args) {
        MovieProcessor processor = new MovieProcessor();
//        Spider.create(processor).addUrl("https://movie.douban.com/people/BBmachtalles/collect").run();
        System.out.println(processor.crawlMovie(processor));
    }

}
