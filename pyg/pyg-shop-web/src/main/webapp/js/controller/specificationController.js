//在模块下定义控制器
app.controller("specificationController",function ($scope,$controller,specificationService) {

    //控制器继承
    //参数一:控制器继承名称
    //参数二:控制器作用域范围传递
    $controller("baseController",{$scope:$scope});

    //定义函数查询所有规格
    $scope.findAll=function () {
        specificationService.findAll().success(function (data) {
            $scope.specificationList=data;
        })
    };

    //查询规格分页数据
    $scope.findPage=function (page,rows) {
        specificationService.findPage(page,rows).success(function (data) {
            //将总记录数赋值给分页组件
            $scope.paginationConf.totalItems=data.total;
            //记录总记录,页面回想
            $scope.specificationList=data.rows;
        })
    };
    //添加规格的方法
    $scope.add=function () {
        //判断
        var objService=null;
        if ($scope.entity.tbSpecification.id!=null){
            objService=specificationService.update($scope.entity);
        }else{
            objService=specificationService.add($scope.entity);
        }
        objService.success(function (data) {
            if (data.success){
                $scope.reloadList();
            }else{
                alert(data.message)
            }
        })
    };
    //根据id查询规格数据
    $scope.findOne=function (id) {
        specificationService.findOne(id).success(function (data) {
            $scope.entity=data;
        })
    };

    //定义删除的方法
    $scope.dele=function () {
        specificationService.dele($scope.selectIds).success(function (data) {
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
        specificationService.searchResult(page,rows,$scope.query).success(function (data) {
            //将总记录数赋值给分页组件
            $scope.paginationConf.totalItems=data.total;
            //记录总记录,页面回想
            $scope.specificationList=data.rows;
        })
    };

    //定义规格选项集合
    $scope.entity={tbSpecification:{},specificationOptionList:[]};
    //定义方法添加规格选项行数
    $scope.addTableRow=function () {
        $scope.entity.specificationOptionList.push({});
    };
    //删除行
    $scope.delTableRow=function (index) {
        $scope.entity.specificationOptionList.splice(index,1);
    }
});