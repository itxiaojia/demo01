package com.itheima.freemarker.test;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/12 16:50
 */
public class FreemarkerTest {

	public static void main(String[] args) throws Exception {

		//第一步：创建一个 Configuration 对象，直接 new 一个对象。构造方法的参数就是 freemarker的版本号。
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
		//第二步：设置模板文件所在的路径。
		configuration.setDirectoryForTemplateLoading(new File("D:\\ideawork\\pyg\\itheima_freemarker\\src\\main\\resources\\config"));
		//第三步：设置模板文件使用的字符集。一般就是 utf-8.
		configuration.setDefaultEncoding("utf-8");
		//第四步：加载一个模板，创建一个模板对象。
		Template template = configuration.getTemplate("test.ftl");
		//第五步：创建一个模板使用的数据集，可以是 pojo 也可以是 map。一般是 Map。
		Map map=new HashMap();
		map.put("name", "张三");
		map.put("message","欢迎来到java世界" );
		map.put("success",false );

		List goodsList=new ArrayList();
		Map goods1=new HashMap();
		goods1.put("name", "苹果");
		goods1.put("price", 5.8);
		Map goods2=new HashMap();
		goods2.put("name", "香蕉");
		goods2.put("price", 2.5);
		Map goods3=new HashMap();
		goods3.put("name", "橘子");
		goods3.put("price", 3.2);
		goodsList.add(goods1);
		goodsList.add(goods2);
		goodsList.add(goods3);
		map.put("goodsList", goodsList);

		map.put("usermessage","这是引入的模版" );
		map.put("date",new Date());
		map.put("userName","张三丰" );

		//数字
		map.put("goodsId", 123456789);

		//第六步：创建一个 Writer 对象，一般创建一 FileWriter 对象，指定生成的文件名。
		FileWriter fileWriter = new FileWriter(new File("D:\\ideawork\\pyg1\\item\\test58.html"));
		//第七步：调用模板对象的 process 方法输出文件。
		template.process(map,fileWriter );
		//第八步：关闭流
		fileWriter.close();

	}
}
