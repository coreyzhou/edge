package com.tenpay.springboot.edge.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class HotDataFetchHandler {

	private final static String WEIBO_URL = "http://s.weibo.com/top/summary?cate=realtimehot";
	private final static String BAIDU_URL = "http://top.baidu.com/buzz?b=1&fr=topbuzz_b341_c513"; 
	private final static String ZHIHU_URL = "https://www.zhihu.com/api/v4/search/top_search"; 

	private static List<String> WEIBO_HOT_DATAS = new ArrayList<String>();
	private static List<String> BAIDU_HOT_DATAS = new ArrayList<String>();
	private static List<String> ZHIHU_HOT_DATAS = new ArrayList<String>();

	public List<String> getWeiboDatas() {
		return WEIBO_HOT_DATAS;
	}
	
	public List<String> getBaiduDatas() {
		return BAIDU_HOT_DATAS;
	}
	
	public List<String> getZhihuDatas() {
		return ZHIHU_HOT_DATAS;
	}

	@Scheduled(cron = "0 0/5 * * * *")
	public List<String> fetchWeiboHotData() {
		List<String> allParsedDatas = new ArrayList<String>();
		System.out.println("fetch weibo data entered, " + new Date().toString());
		try {
			Document document = Jsoup.connect(WEIBO_URL).get();
			Elements scripts = document.select("script");
			// 热搜数据存在倒数第四个script中
			Element hotDatas = scripts.get(scripts.size() - 2);
			// 处理热搜数据json格式
			String strHotJson = hotDatas.toString();
			strHotJson = strHotJson.substring(strHotJson.indexOf("{"), strHotJson.indexOf("}") + 1);
			JSONObject json = new JSONObject(strHotJson);
			String html = json.getString("html");
			Document hotData = Parser.parse(html, "hot_json");
			Elements datas = hotData.select("p");
			String words = "";
			for (int i = 0; i < datas.size(); i++) {
				if (i % 3 == 0) {
					Element a = datas.get(i).getElementsByTag("a").get(0);
					words = a.text();
				} else if (i % 3 == 1) {
					String starNum = datas.get(i).text();
					allParsedDatas.add(words + "|" + starNum);
				}
			}
			WEIBO_HOT_DATAS.clear();
			WEIBO_HOT_DATAS.addAll(allParsedDatas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allParsedDatas;
	}

	@Scheduled(cron = "0 0/5 * * * *")
	public List<String> fetchBaiduHotData() {
		List<String> allParsedDatas = new ArrayList<String>();
		System.out.println("fetch baidu data entered, " + new Date().toString());
		try {
			Document document = Jsoup.connect(BAIDU_URL).get();
			Elements words = document.select(".list-title");
			Elements stars = document.select(".last");
			for (int i = 0; i < words.size(); i++) {
				Element word = words.get(i);
				String strWord = word.text();
				String strStar = stars.get(i+2).text();
				allParsedDatas.add(strWord + "|" + strStar);
			}
			BAIDU_HOT_DATAS.clear();
			BAIDU_HOT_DATAS.addAll(allParsedDatas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allParsedDatas;
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public List<String> fetchZhihuHotData() {
		List<String> allParsedDatas = new ArrayList<String>();
		System.out.println("fetch zhihu data entered, " + new Date().toString());
		try {
			Document document = Jsoup.connect(ZHIHU_URL).get();
			
			ZHIHU_HOT_DATAS.clear();
			ZHIHU_HOT_DATAS.addAll(allParsedDatas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allParsedDatas;
	}
}
