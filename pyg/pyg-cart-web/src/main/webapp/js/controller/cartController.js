app.controller('cartController',function ($scope,cartService) {

    $scope.findCartList=function () {
        cartService.findCartList().success(function (response) {
            $scope.cartList=response;

            //调用求合计
            $scope.totalValue=cartService.sum($scope.cartList);
        })
    }

    //点击+/-改变购物车商品数量
    $scope.addGoodsToCartList=function (itemId,num) {
        cartService.addGoodsToCartList(itemId,num).success(function (response) {
            if (response.success){
                //刷新页面
                $scope.findCartList();
            }else{
                alert(response.message);
            }
        })
    }
});