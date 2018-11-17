app.controller("payController",function ($scope,$location,payService) {

   $scope.createNative=function () {
       payService.createNative().success(function (response) {
           //将结果赋值给变量
           $scope.out_trade_no=response.out_trade_no;
           $scope.total_fee=(response.total_fee/100).toFixed(2);

           //生成二维码
           //生成二维码
           var qr = new QRious({
               element:document.getElementById('qrious'),
               size:250,
               level:'H',
               value:response.code_url
           });

           queryPayStatus();
       });
   }
    queryPayStatus=function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(function (response) {
            if (response.success){
                //跳转到支付成功页面
                location.href="/paysuccess.html#?money="+$scope.total_fee;
            }else{
                if("二维码超时"==response.message){
                    $scope.createNative();
                }else{
                    location.href="/payfail.html";
                }
            }
        });
    }
    $scope.getMoney=function () {
        return $location.search()['money'];
    }
});