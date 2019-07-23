package RestClient;

import java.io.File;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import com.alibaba.fastjson.JSON;
import com.qa.util.ReadExcel;
import com.qa.util.TestBase;
import com.qa.util.TestUtil;

import net.sf.json.JSONObject;
public class Execut extends RestClient{
	String host;
	static String url;
	static RestClient restClient;
    static CloseableHttpResponse closeableHttpResponse;
	final static Logger Log = Logger.getLogger(Execut.class);
	//读取excle测试案例
	public  List read(File file){
		ReadExcel obj = new ReadExcel();
        List excelList = obj.readExcel(file);
		return excelList;
	}

	public List selectmethod(File file) throws ClientProtocolException, IOException{
			TestBase tb=new TestBase();
			restClient = new RestClient();
			List resList=new ArrayList();//存储响应后的数据
	        List excelList = read(file);
	        for (int i = 0; i < excelList.size(); i++) {
	            List list = (List) excelList.get(i);
	            url= tb.prop.getProperty("HOST")+list.get(0);
	            System.out.println(url);
	            if(list.get(1).equals("get")){
	                System.out.print("调用get方法");
	                Log.info("开始执行用例...");
	         		closeableHttpResponse = restClient.get(url);

	             }else if(list.get(1).equals("post")){
	            	System.out.print("调用post方法");
	            	String data = (String) list.get(3);
	            	JSONObject object = JSONObject.fromObject(data);//String转为json对象
					System.out.println(data);
	            	String userJsonString = JSON.toJSONString(object);//json转json字符串
					System.out.print(userJsonString);
	            	Log.info("开始执行用例...");
	        		//准备请求头信息
	        		HashMap<String,String> headermap = new HashMap<String,String>();
	        		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
	        		//调用post
	        		closeableHttpResponse = restClient.post(url, data, headermap);

	             }else if(list.get(1).equals("put")){

	            	 System.out.print("调用put方法");
		            	String data = (String) list.get(3);
		            	JSONObject object = JSONObject.fromObject(data);//String转为json对象
		            	String userJsonString = JSON.toJSONString(object);//json转json字符串

		            	Log.info("开始执行用例...");
		        		//准备请求头信息
		        		HashMap<String,String> headermap = new HashMap<String,String>();
		        		headermap.put("Content-Type", "application/json"); //这个在postman中可以查询到
		        		//调用post
		        		closeableHttpResponse = restClient.put(url, userJsonString, headermap);
	             }else if(list.get(1).equals("Delete")){
	            	 System.out.print("调用Delete方法");
		             Log.info("开始执行用例...");
		         	 closeableHttpResponse = restClient.delete(url);
	             }

	            System.out.println(closeableHttpResponse);
	            resList.add(closeableHttpResponse);
	        }
			return resList;

	}
}