package com.pyg.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbPayLog;
import com.pyg.service.pay.WeixinPayService;
import com.pyg.utils.IdWorker;
import com.pyg.utils.PygResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/21 17:13
 */
@RestController
@RequestMapping("/pay")
public class PayController {

	@Reference(timeout = 10000)
	private WeixinPayService weixinPayService;

	@RequestMapping("/createNative")
	public Map createNative() {

		//根据用户获取id
		String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
		//通过service获取支付日志
		TbPayLog payLog = weixinPayService.queryLogByLoginName(loginName);
		Map resultMap = weixinPayService.createNative(payLog.getOutTradeNo(), "1");
		return resultMap;
	}

	@RequestMapping("/queryPayStatus")
	public PygResult queryPayStatus(String out_trade_no) {
		PygResult pygResult = null;
		int i=0;
		while (true) {
			Map map = weixinPayService.queryPayStatus(out_trade_no);

			if (map == null) {
				pygResult = new PygResult(false, "支付失败");
				break;
			}
			if ("SUCCESS".equals(map.get("return_code"))) {
				pygResult = new PygResult(true, "支付成功");
				//修改订单状态
				weixinPayService.updateOrderStatus(out_trade_no,(String)(map.get("transaction_id")));
				break;
			}
			i++;
			if (i==2){
				pygResult=new PygResult(false,"二维码超时");
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return pygResult;
	}
}
