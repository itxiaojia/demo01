package com.pyg.sms.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/15 20:24
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

	@Value("${product}")
	private String product;

	@Value("${domain}")
	private String domain;

	@Value("${accessKeyId}")
	private String accessKeyId;

	@Value("${accessKeySecret}")
	private String accessKeySecret;

	@RequestMapping(value = "/sendSms",method = RequestMethod.POST )
	public Map sendSms(String phone_number,String sign_name,String template_code,String template_param){

		try {
			//可自助调整超时时间
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");

			//初始化acsClient,暂不支持region化
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			IAcsClient acsClient = new DefaultAcsClient(profile);

			//组装请求对象-具体描述见控制台-文档部分内容
			SendSmsRequest request = new SendSmsRequest();
			//必填:待发送手机号
			request.setPhoneNumbers(phone_number);
			//必填:短信签名-可在短信控制台中找到
			request.setSignName(sign_name);
			//必填:短信模板-可在短信控制台中找到
			request.setTemplateCode(template_code);
			//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam(template_param);

			//选填-上行短信扩展码(无特殊需求用户请忽略此字段)
			//request.setSmsUpExtendCode("90997");

			//可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			request.setOutId("yourOutId");

			//hint 此处可能会抛出异常，注意catch

			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

			//封装返回值
			Map map=new HashMap();
			map.put("Code",sendSmsResponse.getCode() );
			map.put("Message",sendSmsResponse.getMessage() );
			map.put("RequestId",sendSmsResponse.getRequestId() );
			map.put("BizId",sendSmsResponse.getBizId() );

			return map;
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return null;
	}
}
