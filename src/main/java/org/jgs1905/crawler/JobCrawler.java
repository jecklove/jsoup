package org.jgs1905.crawler;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jgs1905.dao.JobDao;
import org.jgs1905.entity.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 爬取51job岗位数据
 * 
 * @author junki
 * @date 2020年6月13日
 */
public class JobCrawler {
	
	public static void main(String[] args) {
		
		String url = "https://search.51job.com/list/000000,000000,0000,00,9,99,java,2,${pageNum}.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
		ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
		for (int i = 1; i <= 1337; i++) {
			int pageNum = i;
			newCachedThreadPool.submit(new Thread(() ->  {
				try {
					JobCrawler jobCrawler = new JobCrawler();
					jobCrawler.getOnePage(url.replace("${pageNum}", pageNum + ""));
					System.out.println("第" + pageNum + "页爬取完毕！");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}));
		}
		
	}

	// 爬取一页数据
	private void getOnePage(String url) throws IOException, ParseException, SQLException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		JobDao jobDao = new JobDao();
		
		Document doc = Jsoup.connect(url).timeout(1000 * 60 * 60).get();
		
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
			
			// 清洗出省份
			String province = null;
			if (address.contains("-")) {
				province = address.split("-")[0];
			} else {
				province = address;
			}
			
			// 获取薪资
			String salary = el.select(".t4").text();
			
			// 清洗出薪资数据
			// 数字：有-和没有-
			// 单位：百、千、万
			// 周期：时、日、月、年
			Double salary_num = 0D;
			String salaryStr = salary;
			if (!salaryStr.trim().equals("")) {
				if (salaryStr.contains("-")) {
					salaryStr = salaryStr.split("-")[1];
				}

				if (salaryStr.contains("百")) {
					salary_num = Double.valueOf(salaryStr.split("百")[0]) * 100;
				} else if (salaryStr.contains("千")) {
					salary_num = Double.valueOf(salaryStr.split("千")[0]) * 1000;
				} else if (salaryStr.contains("万")) {
					salary_num = Double.valueOf(salaryStr.split("万")[0]) * 10000;
				}
				
				switch (salaryStr.split("/")[1]) {
				case "小时":
					salary_num = salary_num * 24 * 31;
					break;
				case "天":
					salary_num = salary_num * 31;
					break;
				case "月":
					break;
				case "年":
					salary_num = salary_num / 12;
					break;

				default:
					break;
				}
				
				salary_num = Math.round(salary_num * 100) / 100.0;
			}
			
			
			// 获取时间
			String date = LocalDate.now().getYear() + "-" + el.select(".t5").text();
			
			
			// 保存到实体类
			Job job = Job.builder().name(jobName).company(company).address(address).salary(salary)
						.date(sdf.parse(date))
						.province(province)
						.salary_num(salary_num)
						.build();
			
			jobDao.insert(job);
			
		}
	}
	
}
