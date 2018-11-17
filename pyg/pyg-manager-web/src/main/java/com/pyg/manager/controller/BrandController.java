package com.pyg.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.BrandService;
import com.pyg.pojo.QueryVo;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/17 18:46
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

	//引入服务层远程对象
	@Reference(timeout = 10000000)
	private BrandService brandService;

	/**
	 *	查询所有品牌数据
	 */
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		List<TbBrand> list = brandService.findAll();
		return list;
	}

	/**
	 * 分页查询品牌数据
	 * 返回pageResult
	 */
	@RequestMapping("/findPage/{page}/{rows}")
	public PageResult findPage(@PathVariable Integer page,@PathVariable Integer rows){
		PageResult pageResult = brandService.findPage(page, rows);
		return pageResult;
	}

	/**
	 * 需求:添加品牌
	 */
	@RequestMapping("/save")
	public PygResult add(@RequestBody TbBrand tbBrand){
		try {
			brandService.insert(tbBrand);
			//保存成功
			return new PygResult(true,"添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			//保存失败
			return new PygResult(false,"添加失败");
		}
	}

	/**
	 * 需求:根据id查询
	 */
	@RequestMapping("/findOne/{id}")
	public TbBrand findOne(@PathVariable Long id){
		TbBrand brand = brandService.findOne(id);
		return brand;
	}
	/**
	 * 需求:根据id更新品牌数据
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody TbBrand tbBrand){
		try {
			brandService.update(tbBrand);
			//更新成功
			return new PygResult(true,"更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			//更新失败
			return new PygResult(false,"更新失败");
		}
	}

	/**
	 *	需求:删除品牌数据
	 */
	@RequestMapping("/delete/{ids}")
	public PygResult delete(@PathVariable Long[] ids){
		try {
			brandService.delete(ids);
			//删除成功
			return new PygResult(true,"删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			//删除失败
			return new PygResult(false,"删除失败");
		}
	}

	/**
	 * 需求:条件查询品牌数据 并分页
	 */
	@RequestMapping("/findQv")
	public PageResult findQv(@RequestBody TbBrand tbBrand,Integer page,Integer rows){
		PageResult page1 = brandService.findPage(tbBrand, page, rows);
		System.out.println(page1.getTotal());
		System.out.println("================");
		System.out.println(page1.getRows());
		return brandService.findPage(tbBrand,page,rows);
	}
	/**
	 * 需求:查询品牌数据,进行下拉列表展示
	 */
	@RequestMapping("/findBrandList")
	public List<Map> findBrandList(){
		List<Map> brandList = brandService.findBrandList();
		return brandList;
	}
}
