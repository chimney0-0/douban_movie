package spider;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;

public class MovieProcessor implements PageProcessor {

    String mainPage = "https://movie.douban.com/people/BBmachtalles/collect";
    Integer totalPage = 1;

    private JSONObject jsonObject = new JSONObject();

    private Site site = Site.me().setCharset("utf-8").setRetryTimes(1).setDomain("www.douban.com")
            .setSleepTime(10000).setRetrySleepTime(10000);

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
            Elements items = document.select("grid_view item");
            for (Element item : items) {
                String name = item.select(".title em").text();
                String intro = item.select(".intro").text();
                Date date = new Date(item.select(".date").text());
                String tags = item.select(".tags").text();
                Integer year = null; //年份
                String nation = ""; //国家地区
                String director = ""; //导演
                String series = ""; //系列

                if (!tags.equals("")) {
                    String[] strings = tags.split(" ");

                }

            }
        }

    }

    public Site getSite() {
        return site;
    }


    //执行一次爬虫任务
    public String getMovieJson(MovieProcessor processor) {
        Spider.create(processor).addUrl().thread(3).run();

        return jsonObject.toJSONString();
    }

}
