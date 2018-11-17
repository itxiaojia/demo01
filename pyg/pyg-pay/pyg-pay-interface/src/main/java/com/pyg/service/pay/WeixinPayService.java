package com.pyg.service.pay;

import com.pyg.pojo.TbPayLog;

import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/21 16:46
 */
public interface WeixinPayService {

	/**
	 * 生成微信的支付二维码
	 * @param out_trade_no:商户订单号
	 * @param total_fee:金额
	 * @return
	 */
	public Map createNative(String out_trade_no,String total_fee);

	/**
	 * 根据订单号查询订单的状态
	 * @param out_trade_no
	 * @return
	 */
	public Map queryPayStatus(String out_trade_no);

	/**
	 * 根据当前登陆者获取redis中的支付日志对象
	 * @param loginName
	 * @return
	 */
	public TbPayLog queryLogByLoginName(String loginName);

	/**
	 * 根据支付日志中的订单号来修改支付成功后的状态
	 * @param out_trade_no
	 * @param transaction_id
	 */
	void updateOrderStatus(String out_trade_no, String transaction_id);
}
