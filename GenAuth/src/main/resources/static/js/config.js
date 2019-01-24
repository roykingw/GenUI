/**
 * INSPINIA - Responsive Admin Theme
 * Copyright 2015 Webapplayers.com
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */

function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider, KeepaliveProvider) {
    // Configure Idle settings
    IdleProvider.idle(5); // in seconds
    IdleProvider.timeout(120); // in seconds

    $urlRouterProvider.otherwise("/login");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    var state=$stateProvider
    .state('manage', {
        abstract: true,
        url: "/manage",
        templateUrl: "views/common/content.html",
        data: { pageTitle: 'manage' }
    }).state('manage.usermanage', {
        url: "/usermanage",
        templateUrl: "views/manage/usermanage.html",
        data: { pageTitle: 'usermanage' },
        controller:'PageController',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    { files: [ 'css/plugins/iCheck/custom.css','js/plugins/iCheck/icheck.min.js'] }
                ]);
            }
        }
    }).state('manage.platUserManage', {
        url: "/platUserManage",//平台用户管理
        templateUrl: "views/manage/platUserManage.html",
        data: { pageTitle: 'platUserManage' },
        controller:'PageController',
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    { files: [ 'css/plugins/iCheck/custom.css','js/plugins/iCheck/icheck.min.js'] }
                ]);
            }
        }
    }).state('manage.platMenuManage', {
        url: "/platMenuManage",//平台菜单管理
        templateUrl: "views/manage/platMenuManage.html",
        data: { pageTitle: 'platMenuManage' },
        controller:'PageController'
    }).state('manage.druidManage', {
        url: "/druidManage",//druid监控页面
        templateUrl: "views/manage/druidManage.html",
        data: { pageTitle: 'druidManage' },
        controller:'PageController'
    }).state('manage.realTimeLog', {
        url: "/realTimeLog",//实时日志监控页面
        templateUrl: "views/manage/realTimeLog.html",
        data: { pageTitle: 'realTimeLog' },
        controller:'PageController'
    })
    //genServer报表
    .state('genServer', {
        abstract: true,
        url: "/genServer",
        templateUrl: "views/common/content.html",
        data: { pageTitle: 'genReport' }
    }).state('genServer.sysDict',{
    	url : "/sysDict",
    	templateUrl:"views/genServer/sysDict.html",
    	data : { pageTitle:'genServer字典管理',urlCode:"sysDict"},
    	controllerAs:'pageInfo',
    	controller:'PageController'
    })
    
    .state('genReport', {
        abstract: true,
        url: "/genReport",
        templateUrl: "views/common/content.html",
        data: { pageTitle: 'genReport' }
    })
    .state("genReport.testGen1",{
    	url:"/testGen1",
    	templateUrl: "views/pages/GenReportDemo.html",
    	controller:'PageController',
    	controllerAs:'usersfxg'
    }).state("genReport.uiblackInfo",{
    	url:"/uiblackInfo",
    	templateUrl: "views/pages/UiBlackInfo.html",
    	controller:'PageController',
    	controllerAs:'pageInfo',
    	data:{ urlCode:"UiBlackInfo" } 
    	//这个urlCode配置为对应后台访问路径的地址前缀
    	//即controller类上配置的requestMapping
    }).state("genReport.uiblackInfo3",{
    	url:"/uiblackInfo3",
    	templateUrl: "views/pages/UiBlackInfo3.html",
    	controller:'PageController',
    	controllerAs:'pageInfo',
    	data:{ urlCode:"UiBlackInfo" } 
    	//这个urlCode配置为对应后台访问路径的地址前缀
    	//即controller类上配置的requestMapping
    }).state("genReport.snapshot",{
    	url:"/snapshot",
    	templateUrl: "views/pages/SnapShot.html",
    	controller:'PageController',
    	controllerAs:'pageInfo',
    	data:{ urlCode:"SnapShot" } 
    })
    .state('blackManage', {
	        abstract: true,
	        url: "/blackManage",
	        templateUrl: "views/common/content.html",
	        data: { pageTitle: 'blackManage' }
    }).state('blackManage.BlackInfo',{
    	url : "/BlackInfo",
    	templateUrl:"views/blackManage/BlackInfo.html",
    	data : { pageTitle:'BlackInfo管理',urlCode:"BlackInfo"},
    	controllerAs:'pageInfo',
    	controller:'PageController'
    })
	 .state('blackManage.Userinfotest',{
	    	url : "/Userinfotest",
	    	templateUrl:"views/userInfoTest/Userinfotest.html",
	    	data : { pageTitle:'Userinfotest管理',urlCode:"Userinfotest"},
	    	controllerAs:'pageInfo',
	    	controller:'PageController'
	    })
    .state('otherReport', {
        abstract: true,
        url: "/otherReport",
        templateUrl: "views/common/content.html",
        data: { pageTitle: 'otherReport' }
    }).state("otherReport.testOther1",{
    	url:"/testOther1",
    	templateUrl: "views/pages/OtherReportDemo.html",
    	controller:'PageController'
    }).state("otherReport.AngularDemo",{
    	url:"/AngularDemo",
    	templateUrl: "views/pages/AngularDemo.html",
    	controller:'PageController'
    })
    .state("login",{
    	url:"/login?error",
    	templateUrl:"views/login.html",
    	data: { pageTitle: 'login' },
    	controller:'loginController'
    }).state('snapshotInfo', {
        url: "/snapshotInfo?id",
        templateUrl: "views/pages/snapshotInfo.html",
        data: { pageTitle: '快照' },
    	controller:'SnapShotController'
    })
    .state('cboard', {
        abstract: true,
        url: "/cboardReport",
        templateUrl: "views/common/content.html",
        data: { pageTitle: 'report' }
    }).state('cboard.dashboard', {
        url: '/{id}',
        params: {id: null},
        templateUrl: 'views/cboard/view.html',
        controller: 'dashboardViewCtrl'
        /*,resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    { files: [ 'http://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM',
                    	"js/cboard/plugins/echart/echarts.min.js",
                    	"js/cboard/plugins/echart/echarts-bmap.min.js",
                    	"js/cboard/plugins/echart/theme-fin1.js",
                    	"js/cboard/plugins/echart/echarts-wordcloud.min.js",
                    	"js/cboard/plugins/echart/echarts-liquidfill.min.js",
                    	"js/cboard/Settings.js",
                    	"js/cboard/util/CBoardEChartUtils.js",
                    	"js/cboard/util/CBoardEChartRender.js",
                    	"js/cboard/util/CBoardGisRender.js",
                    	"js/cboard/util/CBoardCommonUtils.js",
                    	"js/cboard/util/CBoardKpiRender.js",
                    	"js/cboard/util/CBoardTableRender.js",
                    	"js/cboard/util/CBoardMapRender.js",
                    	"js/cboard/util/CBoardJsTreeUtils.js",
                    	"js/cboard/util/CBoardHeatMapRender.js",
                    	"js/cboard/util/CBoardBmapRender.js",
                    	"js/cboard/underscore-min.js",
                    	"js/cboard/numbro.min.js",
                    	"js/cboard/service/util/ModalUtils.js",
                    	"js/cboard/controller/utils/paramSelector.js",
                    	"js/cboard/service/data/dataService.js",
                    	"js/cboard/service/chart/chartDataProcess.js",
                    	"js/cboard/service/chart/chartFunnelService.js",
                    	"js/cboard/service/chart/chartKpiService.js",
                    	"js/cboard/service/chart/chartLineService.js",
                    	"js/cboard/service/chart/chartContrastService.js",
                    	"js/cboard/service/chart/chartPieService.js",
                    	"js/cboard/service/chart/chartSankeyService.js",
                    	"js/cboard/service/chart/chartRadarService.js",
                    	"js/cboard/service/chart/chartService.js",
                    	"js/cboard/service/chart/chartMapService.js",
                    	"js/cboard/service/chart/chartTableService.js",
                    	"js/cboard/service/chart/chartScatterService.js",
                    	"js/cboard/service/chart/chartGaugeService.js",
                    	"js/cboard/service/chart/chartWordCloudService.js",
                    	"js/cboard/service/chart/chartTreeMapService.js",
                    	"js/cboard/service/chart/chartAreaMapService.js",
                    	"js/cboard/service/chart/chartHeatMapCalendarService.js",
                    	"js/cboard/service/chart/chartHeatMapTableService.js",
                    	"js/cboard/service/chart/chartLiquidFillService.js",
                    	"js/cboard/service/chart/chartChinaMapService.js",
                    	"js/cboard/service/chart/chartChinaMapBmapService.js",
                    	"js/cboard/service/chart/chartRelationService.js",
                    	"js/cboard/service/updater/updateService.js",
                    	"js/cboard/directive/dashboardWidget.js",
                    	"js/cboard/controller/dashboard/dashboardViewCtrl.js",
                    	"js/cboard/controller/dashboard/paramCtrl.js",
                    	"js/cboard/plugins/crossTable/plugin.js",
                    	"js/cboard/plugins/FineMap/plugin.js",
                    	"css/cboard/cboard.css",
                    	"css/cboard/AdminLTE.css"] }
                ]);
            }
        }*/
    })
    .state('dashboards_top', {
        abstract: true,
        url: "/dashboards_top",
        templateUrl: "views/common/content_top_navigation.html",
        data: { pageTitle: 'test1-demo4' }
    })
    .state('dashboards_top.dashboard_4', {
        url: "/dashboard_4",
        templateUrl: "views/dashboard_4.html",
        data: { pageTitle: 'test1-bingtu' },
    })
    .state('dashboards', {
        abstract: true,
        url: "/dashboards",
        templateUrl: "views/common/content.html",
        data: { pageTitle: 'test1' }
    })
    .state('layouts', {
        url: "/layouts",
        templateUrl: "views/layouts.html",
        data: { pageTitle: 'Layouts' },
        controller:'layoutController',
        cache:"false"
    }).state('dashboards.dashboard_4', {
        url: "/dashboard_4",
        templateUrl: "views/dashboard_4.html",
        controller:'userManage2C',
        controllerAs:'userManage2',
        data: { pageTitle: 'test1-demo1' },
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                    	files: [ 'css/plugins/iCheck/custom.css','js/plugins/iCheck/icheck.min.js']
                    }
                ]);
            }
        }
    });
}

/**
 * 说明：
 * $http post支持消息体传参的相关配置
 * 对中文参数使用了 encodeURIComponent 转码utf-8 后台要用URLDecoder.decode解码
 * 使用.then(function(data){})封装回调函数,返回的内容在data.data中 data还有一些响应状态的参数。使用.success(function(data){})封装，返回的内容就在data中。
 * 发送HTTP post请求示例  $http.post("module4OneAction/exportRegisterInfo.do",{starttime:'2015'}).then(function(data){}, function(){});
 * $http.post(url,参数JSON).then(function(data){})
 */
var httpconfig=function($httpProvider) {
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

    $httpProvider.defaults.transformRequest = [function(data) {
        var param = function(obj) {
            var query = '';
            var name, value, fullSubName, subName, subValue, innerObj, i;

            for (name in obj) {
                value = obj[name];

                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value !== undefined && value !== null) {
                    query += encodeURIComponent(encodeURIComponent(name)) + '='
                            + encodeURIComponent(encodeURIComponent(value)) + '&';
                }
            }

            return query.length ? query.substr(0, query.length - 1) : query;
        };

        return angular.isObject(data) && String(data) !== '[object File]'
                ? param(data)
                : data;
    }];
}

angular
    .module('inspinia')
    .config(config)
    .config(httpconfig)
    .run(function($rootScope, $state) {
        $rootScope.$state = $state;
    });



