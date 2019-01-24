/**
 * pageTitle - Directive for set Page title - mata title
 */
function pageTitle($rootScope, $timeout) {
    return {
        link: function(scope, element) {
            var listener = function(event, toState, toParams, fromState, fromParams) {
                // Default title - load on Dashboard 1
                var title = 'INSPINIA | Responsive Admin Theme';
                // Create your own title pattern
                if (toState.data && toState.data.pageTitle) title = 'GR | ' + toState.data.pageTitle;
                $timeout(function() {
                    element.text(title);
                });
            };
            $rootScope.$on('$stateChangeStart', listener);
        }
    }
};

/**
 * sideNavigation - Directive for run metsiMenu on sidebar navigation
 */
function sideNavigation($timeout) {
    return {
        restrict: 'A',
        link: function(scope, element) {
    		// Call the metsiMenu plugin and plug it to sidebar navigation
            $timeout(function(){
            	//增加一个role=config的属性，这样点开一个菜单其他父菜单不自动收回。
            	if($(element).attr("role") && $(element).attr("role").indexOf("config")>=0){
            		element.metisMenu({
            			toggle: false
                	});
            	}else{
            		element.metisMenu();
            	}
            });
        }
    };
};

/**
 * responsibleVideo - Directive for responsive video
 */
function responsiveVideo() {
    return {
        restrict: 'A',
        link:  function(scope, element) {
            var figure = element;
            var video = element.children();
            video
                .attr('data-aspectRatio', video.height() / video.width())
                .removeAttr('height')
                .removeAttr('width')

            //We can use $watch on $window.innerWidth also.
            $(window).resize(function() {
                var newWidth = figure.width();
                video
                    .width(newWidth)
                    .height(newWidth * video.attr('data-aspectRatio'));
            }).resize();
        }
    }
}

/**
 * iboxTools - Directive for iBox tools elements in right corner of ibox
 */
function iboxTools($timeout) {
    return {
        restrict: 'A',
        scope: true,
        templateUrl: 'views/common/ibox_tools.html',
        controller: function ($scope, $element) {
            // Function for collapse ibox
            $scope.showhide = function () {
                var ibox = $element.closest('div.ibox');
                var icon = $element.find('i:first');
                var content = ibox.find('div.ibox-content');
                content.slideToggle(200);
                // Toggle icon from up to down
                icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
                ibox.toggleClass('').toggleClass('border-bottom');
                $timeout(function () {
                    ibox.resize();
                    ibox.find('[id^=map-]').resize();
                }, 50);
            },
                // Function for close ibox
                $scope.closebox = function () {
                    var ibox = $element.closest('div.ibox');
                    ibox.remove();
                }
        }
    };
};

/**
 * minimalizaSidebar - Directive for minimalize sidebar
*/
function minimalizaSidebar($timeout) {
    return {
        restrict: 'A',
        template: '<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="" ng-click="minimalize()"><i class="fa fa-bars"></i></a>',
        controller: function ($scope, $element) {
            $scope.minimalize = function () {
                $("body").toggleClass("mini-navbar");
                if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
                    // Hide menu in order to smoothly turn on when maximize menu
                    $('#side-menu').hide();
                    // For smoothly turn on menu
                    setTimeout(
                        function () {
                            $('#side-menu').fadeIn(500);
                        }, 100);
                } else if ($('body').hasClass('fixed-sidebar')){
                    $('#side-menu').hide();
                    setTimeout(
                        function () {
                            $('#side-menu').fadeIn(500);
                        }, 300);
                } else {
                    // Remove all inline style from jquery fadeIn function to reset menu state
                    $('#side-menu').removeAttr('style');
                }
            }
        }
    };
};


function closeOffCanvas() {
    return {
        restrict: 'A',
        template: '<a class="close-canvas-menu" ng-click="closeOffCanvas()"><i class="fa fa-times"></i></a>',
        controller: function ($scope, $element) {
            $scope.closeOffCanvas = function () {
                $("body").toggleClass("mini-navbar");
            }
        }
    };
}


/**
 * icheck - Directive for custom checkbox icheck
 */
function icheck($timeout) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function($scope, element, $attrs, ngModel) {
            return $timeout(function() {
                var value;
                value = $attrs['value'];

                $scope.$watch($attrs['ngModel'], function(newValue){
                    $(element).iCheck('update');
                })

                return $(element).iCheck({
                    checkboxClass: 'icheckbox_square-green',
                    radioClass: 'iradio_square-green'

                }).on('ifChanged', function(event) {
                        if ($(element).attr('type') === 'checkbox' && $attrs['ngModel']) {
                            $scope.$apply(function() {
                                return ngModel.$setViewValue(event.target.checked);
                            });
                        }
                        if ($(element).attr('type') === 'radio' && $attrs['ngModel']) {
                            return $scope.$apply(function() {
                                return ngModel.$setViewValue(value);
                            });
                        }
                    });
            });
        }
    };
}

/**
 * ionRangeSlider - Directive for Ion Range Slider
 */
function ionRangeSlider() {
    return {
        restrict: 'A',
        scope: {
            rangeOptions: '='
        },
        link: function (scope, elem, attrs) {
            elem.ionRangeSlider(scope.rangeOptions);
        }
    }
}

/**
 * fullScroll - Directive for slimScroll with 100%
 */
function fullScroll($timeout){
    return {
        restrict: 'A',
        link: function(scope, element) {
            $timeout(function(){
                element.slimscroll({
                    height: '100%',
                    railOpacity: 0.9
                });

            });
        }
    };
}

function select2($timeout){
    return {
        restrict: 'A',
        link: function(scope, element) {
        	console.info($(element).attr("class")+"=="+$(element).parents(".modal").size());
            $timeout(function(){
            	if($(element).parents(".modal").size()>0){
            		console.info("select2 with modal");
            		element.select2({
            			placeholder: "--选择已有数据--",
            			allowClear: true,
            			width:'100%',
            			dropdownParent : $("#modifyModal")
            		});
            	}else{
            		console.info("select2 without modal");
            		element.select2({
            			placeholder: "--选择已有数据--",
            			allowClear: true,
            			width:'100%'
            		});
            	}

            });
        }
    };
}

function dictpicker(){
    return {
        restrict: 'E',
        scope:{dictItems:"@"},
        //required: 'ngModel',
        template: function(tElement , tAttrs){
        	var html = '<select >'+
			'<option value="">--请选择--</option>'+
			'<option ng-repeat="item in dictItems track by $index" value="{{ item.VALUE }}">'+
				'{{ item.LABEL}}'+
			'</option></select>';
        	return html;
        },
		replace:true,
    	link: function(scope, element,attrs) {
    		var dictitems = [{'key':'key1','value':'value1'},{'key':'key2','value':'value2'},{'key':'key3','value':'value3'}];
    		scope.dictItems=dictitems;
    		$(element).select2({
            	placeholder: "--选择已有数据--",
        		allowClear: true,
        		width:'100%'
            });
        	/**var param={"dictType":attrs.dictType};
        	$http.post("sysdict/queryListLabel.do",param).success(
				function(data) {
    				scope.dictItems=data;
    				$(element).select2({
		            	placeholder: "--选择已有数据--",
		        		allowClear: true,
		        		width:'100%'
		            });
				});*/
        }
    }
}

function particleground($timeout){
	return {
        restrict: 'A',
        link: function(scope, element) {
    		// Call the metsiMenu plugin and plug it to sidebar navigation
            $timeout(function(){
        		element.particleground({
        	        dotColor: '#E8DFE8',
        	        lineColor: '#133b88'
        	    });
            });
        }
    };
}

/**
 * Pass all functions into module
 */
angular
    .module('inspinia')
    .directive('pageTitle', pageTitle)
    .directive('sideNavigation', sideNavigation)
    .directive('iboxTools', iboxTools)
    .directive('minimalizaSidebar', minimalizaSidebar)
    .directive('icheck', icheck)
    .directive('responsiveVideo', responsiveVideo)
    .directive('fullScroll', fullScroll)
    .directive('closeOffCanvas', closeOffCanvas)
    .directive('dictpicker', dictpicker)
    .directive('particleground', particleground)
