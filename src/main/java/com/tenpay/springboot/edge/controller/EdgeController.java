package com.tenpay.springboot.edge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tenpay.springboot.edge.handler.HotDataFetchHandler;

@RestController
public class EdgeController {
	@Autowired
	HotDataFetchHandler handler;

	@RequestMapping("/")
	@ResponseBody
	public String fetchHotDatas() {
		try {
			List<String> datas;
			datas = handler.fetchWeiboHotData();
			String strData = "";
			for (String hot : datas) {
				String word = hot.split("\\|")[0];
				String star = hot.split("\\|")[1];
				strData += ("<p><a target='_blank' href='http://s.weibo.com/weibo/" + word + "'>" + word
						+ "</a>&nbsp;&nbsp;" + star + "</p>");
			}
			return strData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
