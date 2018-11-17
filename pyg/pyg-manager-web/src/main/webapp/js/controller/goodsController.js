//控制层
app.controller('goodsController', function ($scope, $controller,itemCatService, goodsService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };

    //初始化执行方法显示商家商品列表
    $scope.findGoodsBySellerId=function () {
        goodsService.findGoodsBySellerId().success(function (data) {
            $scope.goodsList=data;
        })
    };
    //定义一个数组存放商品状态
    $scope.status=["未审核","已审核","审核未通过","关闭"];

    //商品分类列表
    $scope.itemCatList=[];
    
    //根据id查询商品分类
    $scope.findItemCatList=function () {
        itemCatService.findAll().success(function (data) {
            for (var i=0;i<data.length;i++){
                $scope.itemCatList[data[i].id]=data[i].name;
            }
        })
    };

    //批量修改商品状态
    $scope.updateStatus=function (status) {
        goodsService.updateStatus($scope.selectIds,status).success(function (data) {
            if (data.success){
                //修改成功
                $scope.reloadList();//刷新列表
                $scope.selectIds=[];//清空数组
            }else{
                //修改失败
                alert(data.message);
            }
        })
    };

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.goods.sellerId != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            //获取富文本编辑器的值
            $scope.entity.goodsDesc.introduction = editor.html();
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    alert(response.message);
                } else {
                    alert(response.message);
                }
            }
        );
    }




});
