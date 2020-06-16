package org.jgs1905.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 数据源工具类（使用阿里的Druid连接池）
 * @author junki
 * @date 2020年4月16日
 */
public final class DataSourceUtil {

	private DataSourceUtil() {};
	
	private static DataSource dataSource = null;
	
	// 实现每个线程只有一个Connection实例
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
	
	// 用于保存properties配置文件中的数据
	private static Properties pro = new Properties();
	
	static {
		
		// 1.通过类加载器加载src目录下的配置文件，生成输入流。
		InputStream is = DataSourceUtil.class.getClassLoader().getResourceAsStream("druid.properties");
		
		// 2.将输入流加载到properties集合中
		try {
			pro.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 3.创建druid连接池对象
		try {
			dataSource = DruidDataSourceFactory.createDataSource(pro);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取连接池对象
	 * @return 连接池对象
	 */
	public static DataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * 获取数据库连接
	 * @return 数据库连接
	 */
	public static Connection getConnection() {
		if (threadLocal.get() == null) {
			try {
				threadLocal.set(dataSource.getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return threadLocal.get();
	}
	
	/**
	 * 释放资源
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			threadLocal.remove();
		}
	}
	
	
}
