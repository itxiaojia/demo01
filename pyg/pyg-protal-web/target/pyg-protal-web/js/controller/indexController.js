app.controller('indexController',function ($scope,indexService) {

    //定义一个数组
    $scope.contentList=[];
    //根据分类id查询广告详情数据
    $scope.findContentCategoryById=function (categoryId) {
        indexService.findContentCategoryById(categoryId).success(function (data) {
            $scope.contentList[categoryId]=data;
        })
    };

    //首页跳转到搜索服务
    $scope.search=function () {
        location.href="http://localhost:8086/search.html#?keywords="+$scope.keywords;
    }
});