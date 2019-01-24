======= 前台 Angularjs路由配置：state路由建议配置如下，请自行配置到genauth的config.js中 =====
一级目录配置建议 
		.state('${module_name}', {
	        abstract: true,
	        url: "/${module_name}",
	        templateUrl: "views/common/content.html",
	        data: { pageTitle: '${module_name}' }
	    })
二级目录配置建议 
	    .state('${module_name}.${pojo_name}',{
	    	url : "/${pojo_name}",
	    	templateUrl:"views/${module_name}/${pojo_name}.html",
	    	data : { pageTitle:'${pojo_name}管理',urlCode:"${pojo_name}"},
	    	controllerAs:'pageInfo',
	    	controller:'PageController'
	    })
======= 菜单管理数据库配置建议  请自行配置到genauth的菜单管理当中。=============
一级目录配置建议
name:*自定义配置*; state_path:${module_name}; menu_code:${module_name}; controller_code:""; parent:"root"; menu_level:1;
二级目录配置建议
name:*自定义配置 准备用表注释的，还是自己调整把*; state_path:${module_name}.${pojo_name}; menu_code:${pojo_name}; controller_code:${pojo_name}Controller; parent:${module_name}; menu_level:2;
 
	    
	    