package com.suchuner.shop.test;

import java.io.IOException;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class PictrueTest {
	@Test
	public void testupload(){
		/*需求:使用图片服务器的客户端进行图片文件上传操作
		 *步骤:
		 *1.添加tracker的配置文件,就是告诉图片服务器的ip地址 
		 *2.在程序中加载配置文件的位置(全路径:注意java中路径用的是左斜杆"/")
		 *3.TrackerClient trackerClient =new TrackerClient()对象
		 *4.用new出来的对象得到TrackerServer trackerServer = trackerClient.getConnection();对象
		 *5.声明一个StorageServer storageServer = null;
		 *6.new 一个StorageClient storageClient =new StorageClient(trackerServer, storageServer);
		 *		它的构造器需要用到trackerClient,和StorageServer
		 *7.调用该对象的上传文件的方法,此处用到的是upload_appender_file(String local_filename, String file_ext_name, NameValuePair[] meta_list) 
		 *8.返回数组的名字串,用foreach得到每一个上传文件的位置
		 * */
		try {
			ClientGlobal.init("C:/Users/dell/suchuner/shop-manager-web/src/main/resources/conf/tracker.conf");
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			StorageServer storageServer = null;
			StorageClient storageClient =new StorageClient(trackerServer, storageServer);
			String[] strings = storageClient.upload_appender_file("C:/Users/dell/Pictures/壁纸/1539342191809.jpg", "jpg", null);
			for (String string : strings) {
				System.out.println(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
