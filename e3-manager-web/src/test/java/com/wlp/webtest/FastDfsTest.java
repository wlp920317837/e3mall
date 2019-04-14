package com.wlp.webtest;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDfsTest {

	@Test
	public void testUpload() throws Exception {
		// 创建一个配置文件.文件名随意,内容为tracker服务的地址
		// config/client.conf
		// 使用全局对象加载配置文件
		ClientGlobal.init("E:/workspacetest/e3-manager-web/src/main/resources/config/client.conf");
		// 创建trackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		// 通过trackerClient获得一个trackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		// 创建一个StorageServcer对象的引用,可以为null
		StorageServer storageServer = null;
		// 创建StrorageClient.参数需要trackerServer,StrorageServcer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		// 使用StrorageClient上传文件
		String[] strings = storageClient.upload_file("D:/Pictures/flower.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testFastDfsClient() throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("E:/workspacetest/e3-manager-web/src/main/resources/config/client.conf");
		String string = fastDFSClient.uploadFile("D:/Pictures/777.jpg");
		System.out.println(string);
	}
}
