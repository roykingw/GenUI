package com.genService.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 这个Config可以增加MyBatis支持。
 * 自动扫描com.genauth.app.mapper包，加载MyBatis的mapper接口类
 * 自动添加mybatis/*Mapper.xml文件，加载MyBatis的xml配置
 * @author roykingw
 */
@Configuration
@ConfigurationProperties(prefix = "appDataSource")
@MapperScan(basePackages={"com.genService.mapper"},sqlSessionTemplateRef="appSqlSessionTemplate")
public class AppMyBatisConfig {

	private String driverClassName;
	private String username;
	private String password;
	private String url;

	@Primary
	@Bean(name = "appDataSource",destroyMethod = "close", initMethod = "init")
	@Qualifier("appDataSource")
	@ConfigurationProperties(prefix="appDataSource")
	public DruidDataSource appDataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		return druidDataSource;
	}

	@Bean(name = "appJdbcTemplate")
	public JdbcTemplate appJdbcTemplate(
			@Qualifier("appDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

    @Bean(name = "appSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("appDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*Mapper.xml"));
        return bean.getObject();
    }

    @Bean(name = "appTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("appDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "appSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("appSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
