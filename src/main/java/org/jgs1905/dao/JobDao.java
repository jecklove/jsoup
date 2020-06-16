package org.jgs1905.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.jgs1905.entity.Job;
import org.jgs1905.utils.DataSourceUtil;

/**
 * 51job岗位数据操作类
 * @author junki
 * @date 2020年6月13日
 */
public class JobDao {
	
	public int insert(Job job) throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtil.getDataSource());
		String sql = "insert into job(name,company,address,province,salary,salary_num,date) value(?,?,?,?,?,?,?)";
		int result = qr.update(sql, job.getName(), job.getCompany(), job.getAddress(), job.getProvince(), job.getSalary(), job.getSalary_num(), job.getDate());
		return result;
	}
	
}
