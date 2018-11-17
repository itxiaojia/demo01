//在模块下定义控制器
app.controller("indexController",function ($scope,loginService) {

    //定义函数展示用户名
    $scope.showLoginName = function () {
        loginService.showLoginName().success(function (data) {
            $scope.loginName = data.loginName;
        })
    };
});