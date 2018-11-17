package com.pyg.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.SpecificationService;
import com.pyg.pojo.TbSpecification;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import com.pyg.vo.Specifiaction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/22 21:32
 */
@RestController
@RequestMapping("/specification")
public class SpecificationController {
	//引入服务层远程对象
	@Reference(timeout = 100000)
	private SpecificationService specificationService;

	/**
	 *	查询所有规格数据
	 */
	@RequestMapping("/findAll")
	public List<TbSpecification> findAll(){
		List<TbSpecification> list = specificationService.findAll();
		return list;
	}

	/**
	 * 分页查询规格数据
	 * 返回pageResult
	 */
	@RequestMapping("/findPage/{page}/{rows}")
	public PageResult findPage(@PathVariable Integer page, @PathVariable Integer rows){
		PageResult pageResult = specificationService.findPage(page, rows);
		return pageResult;
	}

	/**
	 * 需求:添加规格
	 */
	@RequestMapping("/save")
	public PygResult add(@RequestBody Specifiaction specifiaction){
		try {
			specificationService.insert(specifiaction);
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
	public Specifiaction findOne(@PathVariable Long id){
		Specifiaction specifiaction = specificationService.findOne(id);
		return specifiaction;
	}
	/**
	 * 需求:根据id更新规格数据
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody Specifiaction specifiaction){
		try {
			specificationService.update(specifiaction);
			//更新成功
			return new PygResult(true,"更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			//更新失败
			return new PygResult(false,"更新失败");
		}
	}

	/**
	 *	需求:删除规格数据
	 */
	@RequestMapping("/delete/{ids}")
	public PygResult delete(@PathVariable Long[] ids){
		try {
			specificationService.delete(ids);
			//删除成功
			return new PygResult(true,"删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			//删除失败
			return new PygResult(false,"删除失败");
		}
	}
	/**
	 * 需求:查询规格数据进行多项选择
	 */
	@RequestMapping("/findSpecList")
	public List<Map> findSpecList(){
		List<Map> specList = specificationService.findSpecList();
		return specList;
	}
}
