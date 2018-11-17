package com.pyg.service.pay.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.mapper.TbPayLogMapper;
import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbPayLog;
import com.pyg.service.pay.WeixinPayService;
import com.pyg.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/21 16:50
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

	@Value("${appid}")
	private String appid;
	@Value("${partner}")
	private String partner;
	@Value("${partnerkey}")
	private String partnerkey;
	@Value("${notifyurl}")
	private String notifyurl;

	@Override
	public Map createNative(String out_trade_no, String total_fee) {
		try {
			//1,参数设置
			Map map = new HashMap();
			map.put("appid", appid);
			map.put("mch_id", partner);
			//获取随机字符串
			map.put("nonce_str", WXPayUtil.generateNonceStr());
			map.put("body", "品优购商品");
			map.put("out_trade_no", out_trade_no);
			map.put("total_fee", total_fee);
			map.put("spbill_create_ip", "127.0.0.1");
			map.put("notify_url", notifyurl);
			map.put("trade_type", "NATIVE");
			//2,使用HttpClient调用微信支付的接口
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			httpClient.setHttps(true);
			//3,设置参数
			httpClient.setXmlParam(WXPayUtil.generateSignedXml(map, partnerkey));
			//4,post请求
			httpClient.post();
			//5,获取返回结果集
			String resultXml = httpClient.getContent();
			//将xml数据转成map
			Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
			//6,封装返回结果
			Map m = new HashMap();
			m.put("code_url", resultMap.get("code_url"));
			m.put("result_code", resultMap.get("result_code"));
			m.put("out_trade_no", out_trade_no);
			m.put("total_fee", total_fee);
			return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map queryPayStatus(String out_trade_no) {
		try {
			//1,参数设置
			Map map = new HashMap();
			map.put("appid", appid);
			map.put("mch_id", partner);
			//获取随机字符串
			map.put("nonce_str", WXPayUtil.generateNonceStr());
			map.put("out_trade_no", out_trade_no);
			//2,使用HttpClient调用微信支付的接口
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery\n" +
					"\n");
			httpClient.setXmlParam(WXPayUtil.generateSignedXml(map, partnerkey));
			//3,设置参数
			//4,post请求
			httpClient.setHttps(true);
			httpClient.post();
			//5,返回结果集
			String resultXml = httpClient.getContent();
			//6,封装返回结果
			Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public TbPayLog queryLogByLoginName(String loginName) {
		TbPayLog payLog =(TbPayLog)redisTemplate.boundHashOps("payLog").get(loginName);
		return payLog;
	}

	@Autowired
	private TbPayLogMapper payLogMapper;

	@Autowired
	private TbOrderMapper orderMapper;

	@Override
	public void updateOrderStatus(String out_trade_no, String transaction_id) {
		//根据订单号查出支付日志对象
		TbPayLog payLog = payLogMapper.selectByPrimaryKey(out_trade_no);
		//设置支付时间
		payLog.setPayTime(new Date());
		//修改支付状态
		payLog.setTradeState("1");
		payLog.setTransactionId(transaction_id);

		//支付日志修改
		payLogMapper.updateByPrimaryKeySelective(payLog);
		//获取关联订单的id
		String orderList = payLog.getOrderList();
		String[] ids = orderList.split(",");
		for (String id : ids) {
			TbOrder order = orderMapper.selectByPrimaryKey(new Long(id));
			//修改订单状态
			order.setStatus("2");
			//修改
			orderMapper.updateByPrimaryKeySelective(order);
		}
	}
}
