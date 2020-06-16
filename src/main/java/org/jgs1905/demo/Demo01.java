package org.jgs1905.demo;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 *	爬虫：
 * 		1.使用程序自动收集网络资源
 * 		2.通常来说，收集的都是公开的资源
 * 	
 * 	技术实现：
 * 		1.主流的语言：java、python、go
 * 		2.主流框架：
 * 				java的weblogic、jsoup、jsoupxpath、seimicrawler
 * 				python的scrapy
 * 
 * 	jsoup：
 * 		1.解析html（将html文本解析成java对象）
 * 			Document doc = Jsoup.connect(url).get();
 * 		2.数据抽取
 * 			1.通过多个方法，链式获取数据，类似js
 * 			2.使用选择器，类似jquery
 * 		3.数据修改
 * 		4.html清理（消除不安全的html内容，防止xss攻击）
 * 	
 * 
 * 
 * @author junki
 * @date 2020年6月13日
 */
public class Demo01 {

	@Test
	public void getDocByUrl() throws IOException {
		
		// 目标页面url地址
		String url = "https://search.51job.com/list/000000,000000,0000,00,9,99,java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
		
		// 使用jsoup核心类
		Document doc = Jsoup.connect(url).timeout(1000 * 60).get();
		
		System.out.println(doc);
	}
	
	@Test
	public void getNode() throws IOException {
		String url = "https://search.51job.com/list/000000,000000,0000,00,9,99,java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
		Document doc = Jsoup.connect(url).timeout(1000 * 60).get();
		
		// 获取id=resultList的节点
		Element resultList = doc.getElementById("resultList");
		
		// 再获取class=el的节点
		Elements els = resultList.getElementsByClass("el");
		
		// 移除第一个el节点，该节点是标题
		els.remove(0);
		
		// 遍历els获取每个字段
		for (Element el : els) {
			// 获取岗位名称
			String jobName = el.getElementsByClass("t1").get(0)
							.getElementsByTag("span").get(0)
							.getElementsByTag("a").get(0)
							.text();
			
			// 获取公司名称
			String company = el.getElementsByClass("t2").get(0)
								.getElementsByTag("a").get(0)
								.text();
			
			// 获取地址
			String address = el.getElementsByClass("t3").get(0)
								.text();
			
			// 获取新资
			String salary = el.getElementsByClass("t4").get(0)
								.text();
			
			// 获取时间
			String date = el.getElementsByClass("t5").get(0)
								.text();
			
			System.out.println(jobName + "::" + company + "::" + address + "::" + salary + "::" + date);
		}
		
		/*
		 * <div class="el"> 
			 <p class="t1 "> 
			 	<em class="check" name="delivery_em" onclick="checkboxClick(this)"></em> 
			 	<input class="checkbox" type="checkbox" name="delivery_jobid" value="119495401" jt="0" style="display:none"> 
			 	<span> 
			 		<a target="_blank" title="Java高级工程师" href="https://jobs.51job.com/dongguan-dlz/119495401.html?s=01&amp;t=0" onmousedown=""> Java高级工程师 </a> 
			 	</span> 
			 </p> 
			 <span class="t2">
			 	<a target="_blank" title="广东志邦速运供应链科技有限公司" href="https://jobs.51job.com/all/co2117922.html">广东志邦速运供应链科技有限公司</a>
			 </span> 
			 <span class="t3">东莞-大朗镇</span> 
			 <span class="t4">0.8-1.5万/月</span> 
			 <span class="t5">06-13</span> 
			</div>
		 * 
		 * 
		 */
	}
	
	@Test
	public void selectNode() throws IOException {
		String url = "https://search.51job.com/list/000000,000000,0000,00,9,99,java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
		Document doc = Jsoup.connect(url).timeout(1000 * 60).get();
		
		// 直接获取class=el的节点
		Elements els = doc.select("#resultList .el:not(.title)");
		
		// 遍历els获取每个字段
		for (Element el : els) {
			// 获取岗位名称
			String jobName = el.select(".t1 > span > a").text();
			
			// 获取公司名称
			String company = el.select(".t2 > a").text();
			
			// 获取地址
			String address = el.select(".t3").text();
			
			// 获取新资
			String salary = el.select(".t4").text();
			
			// 获取时间
			String date = el.select(".t5").text();
			
			System.out.println(jobName + "::" + company + "::" + address + "::" + salary + "::" + date);
		}
		
	}
	
}
