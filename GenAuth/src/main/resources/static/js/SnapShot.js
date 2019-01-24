var SnapShot = {
	config:{
		savePath:"",
		getPath:""
	},
	//参数：scope:$scope对象。
	//url:快照结果的requestUri。建议从ajax请求的回调中获取。
	//optname:快照标题菜单名后的操作。建议传入对应按钮的text
	saveSnapShot:function(scope,url,optName){
		if(!scope && typeof(scope)!="object"){
			swallerror("请传入$scope对象。","快照保存失败");
			return false;
		}
		//把scope中的变量全部保存下来。
		var data = {};
		data.menuCode = $state.current.name;
		data.requestURI=url;
		data.opt=optName;
		data.html=$("#mainView").html();
		var scopeValues={};
		for(var key in scope){
			if(key.indexOf("\$") !=0 && typeof(scope[key])!="function"){
				scopeValues[key]=scope[key];
			}
		}
		data.values = JSON.stringify(scopeValues);
		try{
			$http.post("SnapShot/save.do",data).success(function(resp){
				console.info(resp.resCount);
			})
		}catch(e){
			console.info("保存快照失败",e);
		}
	}
	
		
}