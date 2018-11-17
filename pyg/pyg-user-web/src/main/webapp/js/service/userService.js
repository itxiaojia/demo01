//服务层
app.service('userService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../user/findAll');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../user/findPage/'+page+'/'+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../user/findOne/'+id);
	}
	//增加 
	this.add=function(entity,code){
		return  $http.post('../user/add?code='+code,entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../user/update',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../user/delete/'+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../user/search/'+page+"/"+rows, searchEntity);
	}
	//获取验证码
	this.sendSms=function (phone) {
		return $http.get('../user/sendSms?phone='+phone);
    }
    //获取登陆名
	this.findLoginUser=function () {
		return $http.get('../user/findLoginUser');
    }
});
