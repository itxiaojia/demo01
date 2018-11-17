package com.pyg.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.GoodsService;
import com.pyg.pojo.TbGoods;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import com.pyg.vo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;
import java.util.Map;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference(timeout = 100000000)
	private GoodsService goodsService;

//	@Reference(timeout = 100000000)
//	private ItemPageService itemPageService;
//
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage/{page}/{rows}")
	public PageResult  findPage(@PathVariable int page,@PathVariable int rows){
		//获取商家id
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		TbGoods tbGoods = new TbGoods();
		tbGoods.setSellerId(sellerId);
		return goodsService.findPage(tbGoods,page, rows);
	}
	
	/**
	 * 增加
	 * @param
	 * @return
	 */
	@RequestMapping("/add")
	public PygResult add(@RequestBody Goods goods){
		try {
			//获取商家登陆的id
			String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
			goods.getGoods().setSellerId(sellerId);
			goodsService.add(goods);

			return new PygResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goodsDesc
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody TbGoods goodsDesc){
		try {
			goodsService.update(goodsDesc);
			return new PygResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne/{id}")
	public TbGoods findOne(@PathVariable Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{ids}")
	public PygResult delete(@PathVariable Long [] ids){
		try {
			goodsService.delete(ids);
			return new PygResult(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);
	}

	@RequestMapping("findGoodsBySellerId")
	public List<TbGoods> findGoodsBySellerId(){
		//获取商家id
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		return goodsService.findGoodsBySellerId(sellerId);
	}


	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination topicMarkertableDestination;

	@Autowired
	private Destination topicMarkertable0Destination;

	@RequestMapping("/updateMarketableStatus/{ids}/{status}")
	public PygResult updateMarketableStatus(@PathVariable Long[] ids,@PathVariable String status){
		try {
			goodsService.updateMarketableStatus(ids,status);

			//判断上架状态,如果上架,调用freemarker服务生成静态页面
			if ("1".equals(status)){
				jmsTemplate.send(topicMarkertableDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createObjectMessage(ids);
					}
				});
			}

			if ("0".equals(status)){
				jmsTemplate.send(topicMarkertable0Destination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createObjectMessage(ids);
					}
				});
			}
			//修改成功
			return new PygResult(true,"修改成功");
		} catch (Exception e) {
			e.printStackTrace();  
			//修改失败
			return new PygResult(false,"修改失败");
		}
	}

//	@RequestMapping("getHtml")
//	public void getHtml(Long id){
//		itemPageService.getHtml(id);
//	}
}
