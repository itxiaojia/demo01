package com.pyg.service.order.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pyg.mapper.TbOrderItemMapper;
import com.pyg.mapper.TbPayLogMapper;
import com.pyg.pojo.*;
import com.pyg.service.order.OrderService;
import com.pyg.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.pojo.TbOrderExample.Criteria;

import com.pyg.utils.PageResult;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Autowired
	private TbPayLogMapper payLogMapper;
	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {

		//添加两张表的数据
		//获取redis中的数据
		List<Cart> cartList=(List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());

		//订单id的列表
		List<String> orderIdList = new ArrayList<>();

		//记录总金额
		double totalFee=0;
		//循环
		for (Cart cart : cartList) {
			//设置订单自增长id
			long orderId = idWorker.nextId();
			order.setOrderId(orderId);
			double payment=0;
			//设置状态
			order.setStatus("1");
			order.setCreateTime(new Date());
			order.setUpdateTime(new Date());

			order.setSellerId(cart.getSellerId());
			//查询订单商品
			List<TbOrderItem> orderItemList = cart.getOrderItemList();
			for (TbOrderItem orderItem : orderItemList) {
				//生成id
				orderItem.setItemId(idWorker.nextId());
				orderItem.setOrderId(orderId);

				payment+=orderItem.getTotalFee().doubleValue();
				orderItemMapper.insertSelective(orderItem);
			}

			//实付金额
			order.setPayment(new BigDecimal(payment));
			totalFee+=order.getPayment().doubleValue();
			//把订单id插入集合中
			orderIdList.add(orderId+"");
			orderMapper.insert(order);

		}
		//插入日志
		if ("1".equals(order.getPaymentType())){
			TbPayLog payLog = new TbPayLog();
			//设置参数
			payLog.setOutTradeNo(idWorker.nextId()+"");
			payLog.setCreateTime(new Date());
			payLog.setPayType(order.getPaymentType());
			String ids=orderIdList.toString().replace("[", "").replace("]", "").replace(" ", "");
			payLog.setOrderList(ids);

			payLog.setTotalFee((long)(totalFee*100));
			payLog.setTradeState("0");
			payLog.setUserId(order.getUserId());
			//插入日志
			payLogMapper.insertSelective(payLog);
			//放入redis缓存
			redisTemplate.boundHashOps("payLog").put(order.getUserId(),payLog );
		}

	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
