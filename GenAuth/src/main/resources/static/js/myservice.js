
//查询日期初始化
var dateInit=function() {
    this.mydays = function (days,$http) {

    }
}

angular
    .module('inspinia')
    .service('dateInit',dateInit)
    //angular-cookies.js 不知道怎么用， 只有把他的源代码搬过来用了，可注入$cookieStore操作cookies
    .factory('$cookies', ['$rootScope', '$browser', function($rootScope, $browser) {
        var cookies = {},
            lastCookies = {},
            lastBrowserCookies,
            runEval = false,
            copy = angular.copy,
            isUndefined = angular.isUndefined;
        $browser.addPollFn(function() {
            var currentCookies = $browser.cookies();
            if (lastBrowserCookies != currentCookies) { //relies on browser.cookies() impl
                lastBrowserCookies = currentCookies;
                copy(currentCookies, lastCookies);
                copy(currentCookies, cookies);
                if (runEval) $rootScope.$apply();
            }
        })();
        runEval = true;
        $rootScope.$watch(push);
        return cookies;
        function push() {
            var name,
                value,
                browserCookies,
                updated;
            for (name in lastCookies) {
                if (isUndefined(cookies[name])) {
                    $browser.cookies(name, undefined);
                }
            }
            for (name in cookies) {
                value = cookies[name];
                if (!angular.isString(value)) {
                    value = '' + value;
                    cookies[name] = value;
                }
                if (value !== lastCookies[name]) {
                    $browser.cookies(name, value);
                    updated = true;
                }
            }
            if (updated) {
                updated = false;
                browserCookies = $browser.cookies();

                for (name in cookies) {
                    if (cookies[name] !== browserCookies[name]) {
                        //delete or reset all cookies that the browser dropped from $cookies
                        if (isUndefined(browserCookies[name])) {
                            delete cookies[name];
                        } else {
                            cookies[name] = browserCookies[name];
                        }
                        updated = true;
                    }
                }
            }
        }
    }]).
factory('$cookieStore', ['$cookies', function($cookies) {
    return {
        get: function(key) {
            var value = $cookies[key];
            return value ? angular.fromJson(value) : value;
        },

        put: function(key, value) {
            $cookies[key] = angular.toJson(value);
        },

        remove: function(key) {
            delete $cookies[key];
        }
    };

}])
    .factory('locals',['$window',function($window){
        return{        //存储单个属性
            set :function(key,value){
                $window.localStorage[key]=value;
            },        //读取单个属性
            get:function(key,defaultValue){
                return  $window.localStorage[key] || defaultValue;
            },        //存储对象，以JSON格式存储
            setObject:function(key,value){
                $window.localStorage[key]=JSON.stringify(value);
            },        //读取对象
            getObject: function (key) {
                return JSON.parse($window.localStorage[key] || '{}');
            }

        }
    }]);