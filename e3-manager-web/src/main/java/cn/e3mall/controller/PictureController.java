package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

@Controller
public class PictureController {

	@Value(value = "${IMAGE_SERVER}")
	private String IMAGE_SERVER;

	// 上传图片或文件
	@RequestMapping("/pic/upload")
	public @ResponseBody String upload(MultipartFile uploadFile) {
		try {
			// 将图片传到图片服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/client.conf");
			String originalFilename = uploadFile.getOriginalFilename();// 获得原来的文件名
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);// 获得扩展名
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);

			// 上传后返回一个url,补充完整
			url = IMAGE_SERVER + url;

			// 封装到map中,成功返回.{"error":0 "url":url}
			Map map = new HashMap<>();
			map.put("error", 0);
			map.put("url", url);
			String json = JsonUtils.objectToJson(map);
			return json;

		} catch (Exception e) {
			e.printStackTrace();
			// 封装到map中,失败返回.{"error":0 "message":"上传失败"}
			Map map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "图片上传失败");
			String json = JsonUtils.objectToJson(map);
			return json;
		}
	}
}
