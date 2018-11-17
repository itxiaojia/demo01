package com.pyg.shop.controller;

import com.pyg.utils.FastDFSClient;
import com.pyg.utils.PygResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/27 15:46
 * 图片上传
 */
@RestController
public class UploadController {

	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;

	@RequestMapping("/upload")
	public PygResult upload(MultipartFile file){
		//1,获取文件扩展名
		String originalFilename = file.getOriginalFilename();
		String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);

		try {
			//2,创建一个fastDfs客户端
			FastDFSClient fastDFSClient=new FastDFSClient("classpath:conf/client.conf");

			//3,执行上传处理
			String path = fastDFSClient.uploadFile(file.getBytes(), extName);

			//4,拼接返回的url地址
			String url=FILE_SERVER_URL+path;
			return new PygResult(true,url);

		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false,"上传失败");
		}

	}
}
