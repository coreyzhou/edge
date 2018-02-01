package com.tenpay.springboot.edge.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tenpay.springboot.edge.handler.HotDataFetchHandler;

@Controller
public class EdgeController {

	@Autowired
	HotDataFetchHandler handler;

	@RequestMapping("/get_hot_datas")
	@ResponseBody
	public String fetchHotDatas() {
		try {
//			List<String> weiboDatas = handler.getWeiboDatas();
//			if (weiboDatas.isEmpty()) {
//				handler.fetchWeiboHotData();
//				weiboDatas = handler.getWeiboDatas();
//			}
			List<String> baiduDatas = handler.getBaiduDatas();
			if (baiduDatas.isEmpty()) {
				handler.fetchBaiduHotData();
				baiduDatas = handler.getBaiduDatas();
			}
			List<String> weiboDatas = handler.getBaiduDatas();

			JSONArray arrWeibo = new JSONArray();
			for (String string : weiboDatas) {
				JSONObject json = new JSONObject();
				String word = string.split("\\|")[0];
				String stars = string.split("\\|")[1];
				json.put("word", word);
				json.put("stars", stars);
				arrWeibo.put(json);
			}

			JSONArray arrBaidu = new JSONArray();
			for (String string : baiduDatas) {
				JSONObject json = new JSONObject();
				String word = string.split("\\|")[0];
				String stars = string.split("\\|")[1];
				json.put("word", word);
				json.put("stars", stars);
				arrBaidu.put(json);
			}
			JSONObject result = new JSONObject();
			result.put("weibo_hot_datas", arrWeibo);
			result.put("baidu_hot_datas", arrBaidu);
			return result.toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
