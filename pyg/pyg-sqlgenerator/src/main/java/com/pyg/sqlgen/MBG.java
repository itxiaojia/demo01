package com.pyg.sqlgen;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/22 19:15
 */
public class MBG {

	public static void main(String[] args) throws Exception {

		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File("D:\\ideawork\\pyg\\pyg-sqlgenerator\\src\\main\\resources\\generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);

	}
}
