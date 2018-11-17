app.controller("addressController",function ($scope,addressService,orderService,cartService) {

    //根据登陆用户 初始化查询收货地址
    $scope.findAddressList=function () {
        addressService.findAddressList().success(function (response) {
            $scope.addressList=response;
            //遍历
            for (var i=0;i<$scope.addressList.length;i++){
                if($scope.addressList[i].isDefault==1){
                    $scope.address=$scope.addressList[i];
                }
            }
        })
    }

    //点击选中 将address对象放入变量中
    $scope.selectAddress=function (address) {
        $scope.address=address;
    }

    //判断是否是选中的
    $scope.isSelected=function (address) {
        if ($scope.address==address){
            return true;
        }else{
            return false;
        }
    }

    //定义一个订单对象
    $scope.order={PaymentType:'1'};
    //点击选择支付方式
    $scope.selectPaymentType=function (type) {
        $scope.order.PaymentType=type;
    }

    //
    $scope.orderSave=function () {

        //封装数据
        $scope.order.receiverMobile=$scope.address.mobile;
        $scope.order.receiver=$scope.address.contact;
        $scope.order.receiverAreaName=$scope.address.address;

        orderService.save($scope.order).success(function (response) {
            if (response.success){
                //跳转到支付页面
                location.href="/pay.html";
            }else{
                alert(response.message);
            }
        })
    }

    //初始化获取商品清单
    $scope.findCartList=function () {
        cartService.findCartList().success(function (response) {
            $scope.cartList=response;

            //调用求合计
            $scope.totalValue=cartService.sum($scope.cartList);
        })
    }
});