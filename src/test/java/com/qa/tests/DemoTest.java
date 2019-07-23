package com.qa.tests;

import java.io.File;


import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import com.qa.util.CopyFile;
import com.qa.util.WriteExcel;

import RestClient.Execut;
import RestClient.RestClient;
import net.sf.json.JSONObject;

public class DemoTest extends Execut{

  @Test
  public void testA() throws ClientProtocolException, IOException {
	  String datapath="D:\\3.xls";
	  Execut exe1=new Execut();
	  WriteExcel write=new WriteExcel();//excle写入
	  CopyFile copy=new CopyFile();//excle复制
	  copy.copyFiles(datapath,"\\result.xls");
	  RestClient restClient = new RestClient();
	  File file = new File(datapath);
	  List response = exe1.selectmethod(file);
	  List excelList = read(file);
	  for(int i=0;i<response.size();i++){
		  List list = (List) excelList.get(i);
		  int liststatuscode =Integer.valueOf((String) list.get(4)) ;//响应码正确值,string转integer
		  String type=(String) list.get(1);//请求方法
		  CloseableHttpResponse res = (CloseableHttpResponse)response.get(i);
		  int statusCode = restClient.getStatusCode(res);//获取响应码
//		  System.out.println(statusCode);
//          System.out.println(res);
//          System.out.println(i);
		  if(res.getEntity()!=null){
		  String responseString = EntityUtils.toString(res.getEntity(),"UTF-8"); ////获取响应实体把响应内容存储在字符串对象
//		  System.out.println(responseString);
		  write.writeExcel(responseString,i+1, 6, copy.path+"\\result.xls");
		  }
		  if(liststatuscode==statusCode){
			  write.writeExcel(String.valueOf(statusCode)+"pass",i+1, 5, copy.path+"\\result.xls");
			  System.out.println(type+"请求，"+"返回值正确");
		  }else{
			  write.writeExcel(String.valueOf(statusCode)+"fail",i+1, 5, copy.path+"\\result.xls");
			  System.out.println(type+"请求，"+"返回值错误");
		   }

  }
}
}