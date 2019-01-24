package com.genauth.sys.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 多数据源支持。
 * 直接使用jdbcTemplate的配置方式。比MyBatis简单一点
 * @author roykingw
 *
 */
@Configuration
public class MetaDatasourceConfig {

	@Primary
	@Bean(name = "metadatasource",destroyMethod = "close", initMethod = "init")
    //需要配置InitMethod，初始化了之后才能被druid监控到。
	@Qualifier("metadatasource")
    @ConfigurationProperties(prefix="metadatasource")
    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
        DruidDataSource druidDataSource = new DruidDataSource();  
        return druidDataSource;
    }
	
	@Primary
	@Bean(name = "metaJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(
            @Qualifier("metadatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
	
	  @Bean(name = "metaTransactionManager")
	  @Primary
	  public DataSourceTransactionManager testTransactionManager(@Qualifier("metadatasource") DataSource dataSource) {
	      return new DataSourceTransactionManager(dataSource);
	  }

//	@Bean(name = "appDataSource",destroyMethod = "close", initMethod = "init")
//    @Qualifier("appDataSource")
//    @ConfigurationProperties(prefix="appDataSource")
//    public DruidDataSource appDataSource() {
////        return DataSourceBuilder.create().build();
//        DruidDataSource druidDataSource = new DruidDataSource();
//        return druidDataSource;
//    }
//
//	@Bean(name = "appJdbcTemplate")
//    public JdbcTemplate appJdbcTemplate(
//            @Qualifier("appDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
}
