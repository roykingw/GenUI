package com.genauth.sys.config;

/**
 * 这个Config可以增加MyBatis支持。
 * 自动扫描com.genauth.app.mapper包，加载MyBatis的mapper接口类
 * 自动添加mybatis/*Mapper.xml文件，加载MyBatis的xml配置
 * @author roykingw
 *
 */
//@Configuration
//@ConfigurationProperties(prefix = "metaDataSource")
//@MapperScan(basePackages={"com.genauth.sys.mapper"},sqlSessionTemplateRef="metaSqlSessionTemplate")
public class MetaMyBatisConfig {

//	private String driverClassName;
//	private String username;
//	private String password;
//	private String url;
	
//	@Bean(name = "appDataSource")
//    public DataSource testDataSource() {
//        return DataSourceBuilder.create().driverClassName(driverClassName).username(username).password(password).url(url).build();
//    }

//    @Bean(name = "metaSqlSessionFactory")
//    @Primary
//    public SqlSessionFactory testSqlSessionFactory(@Qualifier("metadatasource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*Mapper.xml"));
//        return bean.getObject();
//    }
//
//    @Bean(name = "metaTransactionManager")
//    @Primary
//    public DataSourceTransactionManager testTransactionManager(@Qualifier("metadatasource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "metaSqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("metaSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//	public String getDriverClassName() {
//		return driverClassName;
//	}
//
//	public void setDriverClassName(String driverClassName) {
//		this.driverClassName = driverClassName;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getUrl() {
//		return url;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
}
