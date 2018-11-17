//在模块下定义控制器
app.controller("brandController",function ($scope,$controller,brandService) {

    //控制器继承
    //参数一:控制器继承名称
    //参数二:控制器作用域范围传递
    $controller("baseController",{$scope:$scope});

    //定义函数查询所有品牌
    $scope.findAll=function () {
        brandService.findAll().success(function (data) {
            $scope.brandList=data;
        })
    };

    //查询品牌分页数据
    $scope.findPage=function (page,rows) {
        brandService.findPage(page,rows).success(function (data) {
            //将总记录数赋值给分页组件
            $scope.paginationConf.totalItems=data.total;
            //记录总记录,页面回想
            $scope.brandList=data.rows;
        })
    };
    //添加品牌的方法
    $scope.add=function () {
        //判断
        var objService=null;
        if ($scope.entity.id!=null){
            objService=brandService.update($scope.entity);
        }else{
            objService=brandService.add($scope.entity);
        }
        objService.success(function (data) {
            if (data.success){
                $scope.reloadList();
            }else{
                alert(data.message)
            }
        })
    };
    //根据id查询品牌数据
    $scope.findOne=function (id) {
        brandService.findOne(id).success(function (data) {
            $scope.entity=data;
        })
    };

    //定义删除的方法
    $scope.dele=function () {
        brandService.dele($scope.selectIds).success(function (data) {
            if (data.success){
                $scope.reloadList();
            }else{
                alert(data.message);
            }
        })
    };
    //定义搜索对象
    $scope.query={};
    //
    //定义搜索方法
    $scope.searchResult=function (page,rows) {
        brandService.searchResult(page,rows,$scope.query).success(function (data) {
            //将总记录数赋值给分页组件
            $scope.paginationConf.totalItems=data.total;
            //记录总记录,页面回想
            $scope.brandList=data.rows;
        })
    }
})