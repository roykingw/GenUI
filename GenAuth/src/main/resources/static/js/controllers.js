function MainCtrl($scope, $state, $http, $templateCache) {
	$scope.user = {};// 登陆用户
	$scope.userMenu = {};// 用户菜单权限
	$scope.userPages = {};// 用户页面权限
	// 左侧菜单权限
	// Angularjs页面加载事件
	// $scope.$on('$viewContentLoaded',function(evnet,viewConfig){viewConfig.targetView
	// $scope.$on('$viewContentLoading',function(evnet,viewConfig){viewConfig.targetView
	// $scope.$on('$viewContentLoading',function(evnet,viewConfig){viewConfig.targetView
	// $scope.$on('$stateChangeStart',function(evnet,toState,toParams,fromState,fromParams){
	// $scope.$on('$stateChangeSuccess',function(evnet,toState,toParams,fromState,fromParams){
	// $scope.$on('$stateChangeError',function(evnet,toState,toParams,fromState,fromParams){

	// 视图加载中调用
	$scope.$on('$stateChangeStart',
		function(event, toState, toParams, fromState, fromParams) {
			var ignoreState = "login";
			var stateName = toState.name;
			if (toState.data && toState.data.urlCode) {
				$scope.urlCode = toState.data.urlCode;
			}
			if(toState.data.pageTitle){
				$scope.pageTitle = toState.data.pageTitle;
			}
			if (stateName.substring(stateName.length - ignoreState.lenght) != ignoreState) {// 不以Login结尾的状态加载权限
				var para = {
					"fromState" : fromState.name,
					"toState" : toState.name
				}
				$http.post("userManage/getCurrentUser.do", para).success(function(data) {
					if (!data.userInfo) {
						// $location.path("\login");
						$state.go("login", {"error" : 1});
						try{
							window.event.preventDefault();
						}catch(e){//error ignored
							console.info(e);
						}
						return false;
					} else {
						$scope.userMenu = {};
						$scope.userPages = {};
						$scope.user = data.userInfo;
						var currentUser = data.userInfo;
						if (currentUser.userMenucode) {
							var userMenus = currentUser.userMenucode.split("|");
							for (var i = 0; i < userMenus.length; i++) {
								if (userMenus[i]) {
									$scope.userMenu[userMenus[i]] = true;
								}
							}
						}
						if (currentUser.userPagecode) {
							var userPages = currentUser.userPagecode.split("|");
							for (var i = 0; i < userPages.length; i++) {
								if (userPages[i]) {
									$scope.userPages[userPages[i]] = true;
								}
							}
						}
						//console.info($scope.userMenu);
						//console.info($scope.userPages);
					}
				})
			}
		});

	this.slideInterval = 5000;
};

// 页面controller 带权限验证
var PageController = function($scope, $http, $location, $state, dateInit) {
	// 页面访问权限 加个延迟操作，会在页面加载完成后执行。避免mainctrl还没有读取到用户数据就开始判断了。
	$scope.$on('$viewContentLoaded', function(evnet, viewConfig) {
		var menucode = $state.current.name.substring($state.current.name
				.lastIndexOf(".") + 1);
		if (!$scope.user || !$scope.user.userId) {
			$state.go("login", { "error" : 1 });
			if (event && event.preventDefault) {
				event.preventDefault();
			}
		} else if ($scope.user.userMenucode.indexOf(menucode + "|") < 0) {
			$state.go("login", { "error" : 2 });
			if (event && event.preventDefault) {
				event.preventDefault();
			}
		} else {
			try { pageload(); } catch (e) { }
		}
	});
	load($scope, $http, $location, $state, this);
}
// 页面Controller , 不带权限验证
var whitePageController = function($scope, $http, $location, $state) {
	load($scope, $http, $location, $state, this);
	$scope.$on('$viewContentLoaded', function(evnet, viewConfig) {
		pageload();
	});
}

// 登录页面登陆控制器
// var loginController =
// function($scope,$http,$injector,$cookieStore,$stateParams,$state){
var loginController = function($scope, $http, $injector, locals, $stateParams,
		$state) {
	$scope.userMenu = {};
	$scope.user = {};
	if ($stateParams && $stateParams.error) {
		if ($stateParams.error == 1) {
			swalerror("请登陆后进行访问。", "访问错误");
		} else if ($stateParams.error == 2) {
			swalerror("您没有访问权限。请重新登陆或联系管理员！", "访问错误");
		}
	}
	if (locals.get("userName") && locals.get("userName")!= "false") {
		$scope.remenberMe = true;
		$scope.login_name = locals.get("userName");
		$("#passW").focus();
	}
	$scope.myKeyup = function(e) {
		var keycode = window.event ? e.keyCode : e.which;
		if (keycode == 13) {
			$scope.login();
		}
	};
	$scope.login = function() {
		if (!$scope.login_name || !$scope.login_password) {
			swal({
				title : "登陆错误",
				text : "请输入用户名和密码",
				type : "warning"
			});
			return false;
		}
		var config = {
			"userName" : $scope.login_name,
			"userPassword" : $scope.login_password
		};
		$http.post("userManage/userLogin.do", config).success(function(data) {
			if (data.error) {
				swal({
					title : "登陆错误,请重新登陆",
					text : data.error,
					type : "warning"
				});
				// alert(data.error);
				return false;
			} else {
				if ($scope.remenberMe) {
					locals.set("userName", $scope.login_name);
				} else {
					locals.set("userName", false);
				}
				$state.go("layouts");
			}
		})
	}
	// 忘记密码
	$scope.fogetPassword = function() {
		swalwarn("请联系管理员修改密码", "");
	}
	$("body").addClass("bgc");
}
// 外部内容的Controller。菜单页，上下导航栏。
var ContentController = function($scope, $http, $state) {
	// 修改密码
	$scope.validPass = false;
	$scope.test = function() {
		if (/^[A-Za-z0-9]+$/.test($("#newPassword").val())) {
			$scope.validPass = true;
		} else {
			$scope.validPass = false;
		}
	}
	// 加载所有菜单
	if (!$scope.allMenu) {
		$http.post("userManage/querySubMenu.do").success(function(data) {
			if (data.length > 0) {
				$scope.allMenu = data;
			}
		});
	}
	$scope.logout = function() {
		$http.get("userManage/userLogout.do").success(function() {
			$scope.userMenu = {};
			$scope.user = {};
			// $location.path("\login");
			$state.go("login");
		})
	}

	/*
	 * //加载子菜单 $http.post("userManage/querySubMenu.do").success(function(data) {
	 * $scope.platdata = data.platdata; $scope.userdata = data.userdata;
	 * $scope.rundata = data.rundata; $scope.proddata = data.proddata;
	 * $scope.activedata = data.activedata; $scope.outfare = data.outfare;
	 * $scope.finacial = data.finacial; });
	 * 
	 * //加载子菜单数目 $http.post("userManage/getMenuCount.do").success(function(data) {
	 * $scope.menucount = data.menucount; });
	 */

}
//通用查询导出模版controller
var GRTemplateController = function($scope, $http, $location, $state) {
	var pageBean = {};
	// 日期插件设置
	var dateInit = function() {
		$('.input-daterange').datepicker({
			format : "yyyymmdd",
			keyboardNavigation : false,
			forceParse : true,
			autoclose : true,
		});
	}
	$scope.exportinfo = function() {
		var exportUrl;
		if ($scope.urlCode) {
			exportUrl = $scope.urlCode + "/exportData.do";
		} else {
			swallErr("请在config.js中配置URLCode");
		}
		var queryStr = param($scope.$$childHead.queryBean);
		if (queryStr) {
			exportUrl += "?" + queryStr;
		}
		window.location.href = exportUrl;
	}

	// 数据加载和渲染
	var loadinfo = function(isReload) {
		// 首次查询设置currPage=1
		if (isReload) {
			pageBean.currPage = '1';
		}
		if (!pageBean.currPage) {
			pageBean.currPage = '1';
		}
		// 设置每页显示条数。默认30
		 pageBean.pageSize=4;
		// 查询前情况page
		pageBean.page = {};
		var queryUrl;
		if ($scope.urlCode) {
			queryUrl = $scope.urlCode + "/queryData.do";
		} else {
			swallErr("请在config.js中配置URLCode");
		}
		// 获取第一个子$scope的变量
		var queryStr = param($scope.$$childHead.queryBean);
		if (queryStr) {
			queryUrl += "?" + queryStr;
		}
		// 数据查询
		$http.post(queryUrl, pageBean).success(
				function(response) {
					pageBean = response;// 接替后台穿过来的json
					$scope.pages = pageBean.page;// 数据集合
					$scope.tableHeaders = pageBean.tableHeaders
					$scope.pager = {};
					setPagination(pageBean.currPage, pageBean.totalPageCount,
							pageBean.totalCount, $scope.pager);
				})
		var setPagination = function(currPage, totalPageCount, totalCount,angula) {
			angula.totalPageCount = totalPageCount;// 总页数
			angula.totalCount = totalCount;// 总记录数
			angula.currPage = currPage; // 当前页
			angula.paginationClick = function(direction) {
				var currPage = parseInt(this.currPage);
				var totalPageCount = parseInt(this.totalPageCount);
				if (direction == 'left') {
					if ((currPage - 1) <= 0) {
						return false;
					}
					pageBean.currPage = (currPage - 1);
				} else if (direction == 'right') {
					if ((currPage + 1) > totalPageCount) {
						return false;
					}
					pageBean.currPage = (currPage + 1);
				}
				pageBean.pagingType = direction;
				loadinfo(false);
			}
		}
	}
	$scope.reloadinfo = function() { loadinfo(true); }
	load($scope, $http, $location, $state, this);
	try { pageload(); } catch (e) { console.info(e)}
}

var SnapShotController = function($scope, $http, $compile, $stateParams,$state) {
	//权限控制。只要登录就能访问
	var menucode = $state.current.name.substring($state.current.name
			.lastIndexOf(".") + 1);
	//不知道为什么， mainController里设置的$scope数据这里没有继承到。 过后再来看把。
	//if (!$scope.user || !$scope.user.userId) {
	//	swalerror("请先登录系统", "访问错误");
	//	return false;
	//}
	//需要配置快照ID
	if (!$stateParams || !$stateParams.id) {
		swalerror("请配置快照ID", "访问错误");
		return false;
	}
	var param = {"snapshotId":$stateParams.id};
	// 数据查询
	$http.post("SnapShot/getSnapShot.do", param).success(
			function(response) {
				var snapInfo = response.snapInfo;// 接替后台穿过来的json
				var htmlCode = snapInfo.html;
				var pageValues = snapInfo.pagevalue;
				try{
					var pageObj = JSON.parse(pageValues);
					for(var pageKey in pageObj){
						if(pageObj[pageKey]){
							$scope[pageKey]=pageObj[pageKey];
						}
					}
					//装载模版
					//var template = angular.element(html);
				    var mobileDialogElement = $compile(htmlCode)($scope);
				    angular.element(document.body).append(mobileDialogElement);
				}catch(e){console.info(e)}
				//装载 $scope里的值
				/*if(pageValues){
					var valuePairs = pageValues.split(",");
					for( var i = 0 ; i < valuePairs.length; i++ ){
						if(valuePairs[i]){
							var ngModel = valuePairs[i].split(":")[0];
							var ngModelValue = valuePairs[i].split(":")[1];
							console.info("ngModel = "+ngModel+" ngModelValue = "+ngModelValue);
							//如果没有点。可以直接设置。
							if(ngModel.indexOf(".")<0){
								$scope[ngModel]=ngModelValue;
							}else{
								//如果有点，需要按结构一层一层设置JSON对象。
								var keyList = ngModel.split(".");
								$scope[keyList[0]]={};
								var tmpScop = $scope[keyList[0]];
								for(var i = 1 ; i < keyList.length; i ++){
									if(i < keyList.length -1){
										tmpScop[keyList[i]]={};
									}else{
										tmpScop[keyList[i]]=ngModelValue;
									}
								}
								$scope[keyList[0]]=tmpScop;
							}
						}
					}
				}*/
				//去掉页面事件
			    $("input").attr("disabled", "true");
		        $("select").attr("disabled", "true");
		        $("textarea").attr("disabled", "true");
		        $("button").attr("disabled", "true");
		        document.onkeydown = function (eve) {
		            var event = window.event || eve;
		            event.keyCode = 0;
		            event.returnvalue = false;
		        }
			})
}

angular.module('inspinia').controller("layoutController", whitePageController)// 首页不进行权限验证
		.controller("PageController", PageController)
		.controller("loginController", loginController)
		.controller('MainCtrl', MainCtrl)
		.controller("ContentController", ContentController)
		.controller("SnapShotController", SnapShotController)
		.controller("GRTemplateController", GRTemplateController);
