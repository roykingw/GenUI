//过滤器
var nullFilter=function(){
	return function(str){
		if(str==undefined||str==null||str.trim()=='null'||str.trim()==''){
			return '';
		}
		return str;
	}
}

angular
    .module('inspinia')
    .filter('nullFilter',nullFilter);
