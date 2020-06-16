package org.jgs1905.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 51job岗位实体
 * @author junki
 * @date 2020年6月13日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
	
	private Long id;
	
	// 岗位名称
	private String name;
	
	// 公司名称
	private String company;
	
	// 地址
	private String address;
	
	// 省份
	private String province;
	
	// 薪资
	private String salary;
	
	// 薪资数值(元)
	private Double salary_num;
	
	// 发布时间
	private Date date;
	
}
