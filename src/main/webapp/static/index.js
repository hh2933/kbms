(function(w){
	var x = {
		dom:{
			dMenu:$(".xMenu"),
			dRightbox:$(".xRightbox")
		},
		init:function(){
			var _self = this;
			_self.addEvent();
		},
		addEvent:function(){
			var _self = this;

			$(window).resize();
            _self.dom.dMenu.on(function(){

            });
			//侧栏导航
			_self.dom.dMenu.delegate("a","click",function(){
				var _this = $(this),
				_ulArr = _self.dom.dMenu.find("ul"),
				_ul = _this.next(),
				_em = _this.find("em");
				if(!_em.hasClass("xMenuShow")&!_this.hasClass("xMenuTwo")){
					$(".xMenuShow").removeClass("xMenuShow");
					$(".xMenuonefocus").removeClass("xMenuonefocus");
                    $(".xMenuonefocus1").removeClass("xMenuonefocus1");
                    $(".xMenuFocus1").removeClass("xMenuFocus1");
                    $(".xMenuFocus").removeClass("xMenuFocus");
					_ulArr.slideUp(1);
					if(_this.hasClass("xMenuOne")){
						_em.addClass("xMenuShow");
                        /*_em.addClass("em10");*/
						_ul.slideDown(1);
						//有二级栏目，触发二级栏目第一个点击
                        _this.addClass("xMenuonefocus");
                        _this.addClass("xMenuonefocus1");
						_ul.find("li:first-child").children("a").trigger("click");
					}else{
						var _rel = _this.attr("data-rel");
						_this.addClass("xMenuonefocus");
                        _this.addClass("xMenuonefocus1");
						_self.dom.dRightbox.load(_rel,function(){
							$(window).resize();
						});
					}
				}else if(_this.hasClass("xMenuTwo")){//二级栏目点击

					var _rel = _this.attr("data-rel"),_name = _this.text();
					_ulArr.find(".xMenuFocus").removeClass("xMenuFocus");
                    $(".xMenuonefocus").removeClass("xMenuonefocus");
                    $(".xMenuFocus1").removeClass("xMenuFocus1");
					_this.addClass("xMenuFocus");
                    _this.parent().parent().prev().addClass("xMenuFocus1");

					if(_name == "指标监管"){
						var s = '';
						s += '<iframe src="'+_rel+'" frameborder="0" width="100%" height="100%" ></iframe>';
						_self.dom.dRightbox.html(s);
					}else{
						_self.dom.dRightbox.load(_rel,function(){
							$(window).resize();
						});
					}

				}else{
					//
				}
				
			});
			$(".xMenuThree span").on("click",function(){
				var _rel = $(this).attr("data-rel"),_name = $(this).text();
				$(".xMenuThree .red").removeClass("red");
				$(this).addClass("red");
				if(_name == "高额住院费用"){
					var s = '';
					s += '<iframe src="'+_rel+'" frameborder="0" width="100%" height="100%" ></iframe>';
					_self.dom.dRightbox.html(s);
				}else{
					_self.dom.dRightbox.load(_rel,function(){
						$(window).resize();
					});
				}

			});

			$(".xMenu a:eq(0)").trigger("click");

		}
		
	};
	x.init();
})(window);