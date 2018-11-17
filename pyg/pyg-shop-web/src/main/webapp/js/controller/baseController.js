//在模块下定义控制器
app.controller("baseController",function ($scope) {

    //定义reloadList方法
    $scope.reloadList=function () {
        //调用分页查询的方法
        // if ($scope.query.name==null){
            $scope.findPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        // }else{
        //     //调用搜索方法
        //     $scope.searchResult($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        // }

    };

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            //重新加载,此方法将会被自动加载
            //1,页面刷新
            //2,分页控件中数据发生变化，reloadList也会自动调用
            $scope.reloadList();
        }
    };

    //定义数组变量封装ids
    $scope.selectIds=[];
    //定义方法(组装待删除的id)
    $scope.updateSeSelection=function ($event,id) {
        //判断是否是鼠标点击事件
        if ($event.target.checked){
            //选中事件
            $scope.selectIds.push(id);
        }else{
            //取消事件
            $scope.selectIds.splice($scope.selectIds.indexOf(id),1);
        }
    };

});