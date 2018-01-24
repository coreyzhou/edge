package com.tenpay.springboot.edge.handler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HotDataFetchHandler {

	private final static String WEIBO_URL = "http://s.weibo.com/top/summary?cate=realtimehot";

	private List<String> lstHotDatas = new ArrayList<String>();

	public List<String> getLstHotDatas() {
		return lstHotDatas;
	}

	@Scheduled(cron = "0 0/5 * * * ? ")
	public List<String> fetchWeiboHotData() {
		List<String> allParsedDatas = new ArrayList<String>();
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
			lstHotDatas.clear();
			lstHotDatas.addAll(allParsedDatas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allParsedDatas;
	}
}
