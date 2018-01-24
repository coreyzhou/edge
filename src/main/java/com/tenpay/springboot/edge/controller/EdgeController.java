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
			List<String> datas = handler.getLstHotDatas();
			if (datas.isEmpty()) {
				handler.fetchWeiboHotData();
				datas = handler.getLstHotDatas();
			}
			JSONArray arr = new JSONArray();
			for (String string : datas) {
				JSONObject json = new JSONObject();
				String word = string.split("\\|")[0];
				String stars = string.split("\\|")[1];
				json.put("word", word);
				json.put("stars", stars);
				arr.put(json);
			}
			return arr.toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
