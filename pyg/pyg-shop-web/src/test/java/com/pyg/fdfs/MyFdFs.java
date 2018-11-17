package com.pyg.fdfs;

import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/27 11:47
 */
public class MyFdFs {

	/**
	 * 需求:使用fdfs实现图片上传
	 */
	@Test
	public void uploadPic() throws Exception {
		//指定图片服务器连接地址
		String conf="D:\\ideawork\\pyg\\pyg-shop-web\\src\\main\\resources\\conf\\client.conf";

		//指定上传图片
		String picPath="F:\\1234.jpg";

		//创建配置文件,连接图片服务器
		ClientGlobal.init(conf);

		//创建服务器调度客户端对象
		TrackerClient tc=new TrackerClient();
		//从对象中获取服务对象
		TrackerServer trackerServer = tc.getConnection();

		StorageServer storageServer=null;
		//创建stroge存储客户端对象
		StorageClient sc=new StorageClient(trackerServer, storageServer);

		//上传图片
		String[] urls = sc.upload_file(picPath, "jpg", null);
		for (String url : urls) {
			System.out.println(url);
		}
	}
}
