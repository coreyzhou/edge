package com.tenpay.springboot.edge;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tenpay.springboot.edge.handler.HotDataFetchHandler;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FetchHandlerTest {
	@Resource
	HotDataFetchHandler handler;

	@Test
	public void fetchWeiboHotDataTeset() {
		handler.fetchWeiboHotData();
	}

	@Test
	public void fetchBaiduHotDataTest() {
		handler.fetchBaiduHotData();
	}
}
