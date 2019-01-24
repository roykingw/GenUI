<meta charset="utf-8">
<div class="row animated fadeInRight">
	<div class="col-lg-12">
		<div class="ibox float-e-margins">
			<div class="ibox-title">
				<h5>{{ pageTitle }}</h5>
			</div>
			<div class="ibox-content">
				<#list all_modules as row_modules>
				<div class="row">
					<#list row_modules as module>
					<div class="col-sm-3 m-b-xs" style="overflow: hidden">
						<div class="input-group">
						<span class="input-group-addon">${module.remarks}</span> <input type="text"
								class="input-sm form-control" value="" ng-model="queryBean.${module.java_name}" />
						</div>
					</div>
					</#list>
				</div>
				</#list>
				
				<div class="row">
					<div class="col-sm-12">
						<div class="input-group" style="padding-right: 50%">
							<span class="input-group-btn">
								<button type="button" ng-click="reloadinfo()"
									class="btn btn-sm btn-primary" ng-if="userPages.${pojo_name}_query">
									查询</button>
							</span> <span class="input-group-btn">
								<button type="button" ng-click="exportinfo()"
									class="btn btn-sm btn-primary"
									ng-if="userPages.${pojo_name}_export">导出</button>
							</span>
						</div>
					</div>
				</div>
				
				<hr />
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>#</th>
								<th>操作</th>
								<th style="word-break: keep-all; white-space: nowrap;"
								 ng-repeat="head in tableHeaders track by $index">{{ head }}</th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="page in pages track by $index">
								<td><label class="label label-primary">{{ $index+1 }}</label></td>
								<td><a class="label label-primary" ng-click="deleteData(pageT[$index])" title="删除"><i class="fa fa-minus-square-o" aria-hidden="true"></i></a>
								<a class="label label-primary" ng-click="showData(pageT[$index])" title="修改"><i class="fa fa-pencil-square-o"/></a></td>
								<td ng-repeat="value in page track by $index">{{ value }}</td>
							</tr>
						</tbody>
					</table>
				</div>
				<nav>
					<ul class="pager">
						<li><a href="javascript:void(0)"
							ng-click="pageInfo.paginationClick('left')">上一页</a></li>
						<li><a href="javascript:void(0)"
							ng-click="pageInfo.paginationClick('right')">下一页</a></li>
					</ul>
				</nav>
				<pre>当前页:{{ pageInfo.currPage  }}&nbsp;&nbsp;&nbsp;总页数:{{ pageInfo.totalPageCount }}&nbsp;&nbsp;&nbsp;总记录数:{{ pageInfo.totalCount }}</pre>
			</div>
		</div>
	</div>
</div>

<div class="modal inmodal fade" id="modifyModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">{{ pageTitle }}<small>此模版主键列目前不能修改</small></h4>
			</div>
			<div class="modal-body">
				<form class="bs-example bs-example-form" role="form">
					<#list all_modules as row_modules>
						<#list row_modules as module>
					<div class="input-group">
						<span class="input-group-addon">${module.remarks}:</span>
						<input type="text" class="form-control<#if module.type_name=="DATE"> layDate <#elseif module.type_name="TIMESTAMP"> layDateTime</#if>" ng-model="modifydata.${module.java_name}" AutoComplete="off" <#if module.isPK =="true">readOnly="readOnly"</#if> />
					</div><br>
					</#list></#list>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" ng-click="modifyData()">确定</button>
			</div>
		</div>
	</div>
</div>

<script>
  var pageBean = {};
  //日期插件设置
  var dateInit=function(){
	lay(".layDate").each(function(){
    	laydate.render({
    		  elem: this, //指定元素
    		  type: 'date', //type可选值：year,month,time,datetime
    		  trigger: 'click',
    		  //,range: true //选择范围
    		  format: 'yyyy-MM-dd',
    		  done:function(value,date,endDate){
    			  try{
    				  var ngModelVal = $(this)[0].elem.attr("ng-model").split(".");
    				  if(!$scope[ngModelVal[0]]){
    					  $scope[ngModelVal[0]] = {};
    				  }
    				  $scope[ngModelVal[0]][ngModelVal[1]]=value;
    				  //$scope[$(this)[0].elem.attr("ng-model")]=value;
    				  $scope.$apply()}
    			  catch(e){console.info(e)}
    		  }
    		});
    })
	//layDate控件 选择秒。
    lay(".layDateTime").each(function(){
    	laydate.render({
    		  elem: this, //指定元素
    		  type: 'datetime', //type可选值：year,month,time,datetime
    		  trigger: 'click',
    		  //,range: true //选择范围
    		  format: 'yyyy-MM-dd HH:mm:ss',
    		  done:function(value,date,endDate){
    			  try{
    				  var ngModelVal = $(this)[0].elem.attr("ng-model").split(".");
    				  if(!$scope[ngModelVal[0]]){
    					  $scope[ngModelVal[0]] = {};
    				  }
    				  $scope[ngModelVal[0]][ngModelVal[1]]=value;
    				  //$scope[$(this)[0].elem.attr("ng-model")]=value;
    				  $scope.$apply()}
    			  catch(e){console.info(e)}
    		  }
    		});
    })
  }
  //页面初始函数
  var pageload=function(){
	  dateInit();
	  loadinfo(true);
	  $scope.queryBean = {};
	  $scope.exportinfo=function(){
		var exportUrl;
		if(!$scope.urlCode){
			swalerror("请在config.js中配置urlcode","数据处理错误");
		}else{
			exportUrl = $scope.urlCode+"/exportData.do";
		}
		var queryStr = param($scope.queryBean);
	    if(queryStr){
	    	exportUrl += "?"+queryStr;
	    }
	    window.location.href=exportUrl;
	  }	  
	  $scope.reloadinfo = function(){
		  loadinfo(true);
	  }
	  $scope.showData = function(data){
		  $scope.modifydata = data;
		  $("#modifyModal").modal("show");
	  }
	  $scope.modifyData = function(data){
	  	swal({
				title:"修改数据",
				text:"确认修改数据?",
				type: "info",
		        showCancelButton: true,
		        cancelButtonText:"关闭",
		        confirmButtonColor: "#DD6B55",
		        confirmButtonText: "确认",
		        closeOnConfirm: true
			},function(){
				$http.post($scope.urlCode + "/updateData.do",$scope.modifydata)
				  .success(function(data){
				  //如果后台处理太快，会造成后面的提示框会跟着一起关闭的问题。请酌情自行处理。
				  	if(data.resCode && -1 == data.resCode){
						  swalerror(data.message,"数据处理错误");
					  }else{
						  $scope.modifydata = {};
						  $("#modifyModal").modal("hide");
						  loadinfo(); 
						  //如果后台处理太快，会造成这个提示框被一起关闭。加半秒的延迟。
						  setTimeout(function(){
						  	swalsucc(data.message,"数据更新成功");
						  },500);
					  }
				  })
			})
	  }
	  $scope.deleteData = function(data){
		  swal({
				title:"删除数据",
				text:"确定要删除该数据吗？",
				type: "warning",
		        showCancelButton: true,
		        cancelButtonText:"关闭",
		        confirmButtonColor: "#DD6B55",
		        confirmButtonText: "确认",
		        closeOnConfirm: false,
		        closeOnCancel: false
			},function(){
				$http.post($scope.urlCode + "/deleteData.do",data)
				  .success(function(data){
					  //如果后台处理太快，会造成后面的提示框会跟着一起关闭的问题。请酌情自行处理。
					  if(data.resCode && -1 == data.resCode){
						  swalerror(data.message,"数据处理错误");
					  }else{
						  swalsucc("删除数据成功");
						  loadinfo();
					  }
				  })
			})
	  }
  }   
  
  //数据加载和渲染
  var loadinfo=function(isReload){
	//首次查询设置currPage=1
	if(isReload){
		pageBean.currPage='1';
	}
	if(!pageBean.currPage){
		pageBean.currPage='1';
	}
	//设置每页显示条数。默认30
	//pageBean.pageSize=30;
	//查询前情况page
	pageBean.page={};
	var queryUrl;
	if ($scope.urlCode) {
		queryUrl = $scope.urlCode + "/queryData.do";
	} else {
		swallErr("请在config.js中配置URLCode");
	}
    //查询用post传参
    for(var key in $scope.queryBean){
    	pageBean[key]=$scope.queryBean[key];
    }
	//数据查询
	$http.post(queryUrl, pageBean).success(
			function(response) {
				if(response.error){
					swalerror("查询出错，请重试，或联系管理员查看后台日志");
				}else{
					pageBean = response;//接替后台穿过来的json
					$scope.pages = pageBean.page;//Bean中配置了column 的 excelHeader 的结果集。 这是只需要直接展示的结果集。
					$scope.pageT = pageBean.pageT;//Bean中配置了column 属性的结果集。 这是整个DB查询出来的结果集。 
					$scope.tableHeaders = pageBean.tableHeaders
	
					setPagination(pageBean.currPage, pageBean.totalPageCount,
							pageBean.totalCount, angula);
				}
			})
	}
</script>