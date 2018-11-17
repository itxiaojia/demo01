 //控制层 
app.controller('userController' ,function($scope,$controller ,userService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		userService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		userService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		userService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){
		//判断两次输入的密码是否一致
		if ($scope.entity.password!=$scope.password){
			alert("输入的密码不一致");
			return;
		}

		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=userService.update( $scope.entity ); //修改  
		}else{
			serviceObject=userService.add( $scope.entity ,$scope.code );//增加
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//去登陆页面
					location.href="/home-index.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		userService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		userService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//发送验证码
    $scope.sendSms=function () {

	    //判断手机号是否为空
        if($scope.entity.phone==null || $scope.entity.phone==""){
            alert("请输入手机号");
            return;
        }
        userService.sendSms($scope.entity.phone).success(function (resopnse) {
            if (resopnse.success){
                alert("验证码发送成功")
            }else{
                alert("验证码发送失败")
            }
        })
    }

    //去登陆
    $scope.toCas=function () {
        location.href="/home-index.html";
    }
    //获取登陆名
	$scope.findLoginUser=function () {
		userService.findLoginUser().success(function (response) {
			$scope.loginUser=response;
        })
    }
});	
