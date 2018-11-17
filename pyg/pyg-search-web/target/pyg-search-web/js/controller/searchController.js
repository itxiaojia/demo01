app.controller("searchController",function ($scope,$location,searchService) {

    // 定义封装搜索参数对象，封装所有参与搜索参数
    // 1,主查询条件 （关键词搜索条件）
    // 2,分类查询参数
    // 3，品牌参数
    // 4,规格属性参数
    // 5,价格参数
    // 6,排序
    // 7,分页
    $scope.searchMap = {
        "keywords" : "",
        "category" : "",
        "brand" : "",
        "spec" : {},
        "price" : "",
        "sort" : "ASC",
        "sortField" : "price",
        "page" : 1,
        "pageSize" : 30
    };

    //定义方法接受首页传递过来的搜索关键字
    $scope.searchKeywords=function () {
        //$location.search() 获取首页传递的所有数据
        var keywords=$location.search()["keywords"];

        //将数据传递
        $scope.searchMap.keywords=keywords;

        //执行搜索
        $scope.searchList();
    };

    //搜索的方法
    $scope.searchList=function () {
        searchService.search($scope.searchMap).success(function (reponse) {
            $scope.searchResult=reponse;

            //分页
            buildPageLabel();
        })
    };

    //定义方法:点击过滤条件执行的方法
    $scope.addFilterCondition=function (key,value) {
        //判断 如果key是分类
        if (key=="category"||key=="brand"||key=="price"){
            $scope.searchMap[key]=value;
        }else{
            $scope.searchMap.spec[key]=value;
        }
        //再执行搜索
        $scope.searchList();
    }

    //定义方法 删除面包屑
    $scope.removeSearchItem=function (key) {
        //判断 如果key是分类
        if (key=="category"||key=="brand"||key=="price"){
            $scope.searchMap[key]="";
        }else{
            delete $scope.searchMap.spec[key];
        }
        //再执行搜索
        $scope.searchList();
    }

    //排序的方法
    $scope.sortSearch=function (key,value) {
        $scope.searchMap.sortField=key;
        $scope.searchMap.sort=value;
        //再搜索
        $scope.searchList();
    }

    //构建分页标签(totalPages为总页数)
    buildPageLabel=function(){
        $scope.pageLabel=[];//新增分页栏属性
        var maxPageNo= $scope.searchResult.totalPages;//得到最后页码
        var firstPage=1;//开始页码
        var lastPage=maxPageNo;//截止页码

        $scope.firstDot=true;//前面有点
        $scope.lastDot=true;//后边有点


        if($scope.searchResult.totalPages> 5){  //如果总页数大于5页,显示部分页码
            if($scope.searchResult.pageNo<=3){//如果当前页小于等于3
                lastPage=5; //前5页
                $scope.firstDot=false;
            }else if( $scope.searchResult.pageNo>=lastPage-2  ){//如果当前页大于等于最大页码-2
                firstPage= maxPageNo-4;
                $scope.lastDot=false;//后5页
            }else{ //显示当前页为中心的5页
                firstPage=$scope.searchResult.pageNo-2;
                lastPage=$scope.searchResult.pageNo+2;
                $scope.firstDot=true;//前面有点
                $scope.lastDot=true;//后边有点
            }
        }
        //循环产生页码标签
        for(var i=firstPage;i<=lastPage;i++){
            $scope.pageLabel.push(i);
        }
    }

    //查询当前页
    $scope.pageSearch=function (page) {
        if (page<1){
            page=1;
        }
        if (page>$scope.searchResult.totalPages){
            page=$scope.searchResult.totalPages;
        }
        $scope.searchMap.page=page;
        //在执行查询
        //再搜索
        $scope.searchList();
    }
});