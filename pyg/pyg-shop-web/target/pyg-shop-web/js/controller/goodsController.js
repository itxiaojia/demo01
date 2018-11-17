//控制层
app.controller('goodsController', function ($scope, $controller, uploadService, goodsService, itemCatService, typeTemplateService) {

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

    //定义一个数组存放商品上下架状态
    $scope.marketableList=["已下架","已上架"];

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

    //修改商品上下架状态
    $scope.updateMarketableStatus=function (status) {
        goodsService.updateMarketableStatus($scope.selectIds,status).success(function (data) {
            if (data.success){
                //修改成功 刷新页面
                $scope.reloadList();
                //清空数组
                $scope.selectIds=[];
            }else{
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

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

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


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
    //实现商品录入:三级联动
    //1,先查询顶级商品分类数据
    //2,根据选中的节点.查询此节点的子节点

    //1,先查询顶级节点
    $scope.findCatList = function () {
        itemCatService.findCatListByParentId(0).success(function (data) {
            $scope.cat1List = data;
        })
    };

    //2,使用$watch
    $scope.$watch('entity.goods.category1Id', function (newValue, oldValue) {
        itemCatService.findCatListByParentId(newValue).success(function (data) {
            $scope.cat2List = data;
        })
    });

    //3,查询第三季节点
    $scope.$watch('entity.goods.category2Id', function (newValue, oldValue) {
        itemCatService.findCatListByParentId(newValue).success(function (data) {
            $scope.cat3List = data;
        })
    });

    //4,监控模板id
    $scope.$watch('entity.goods.category3Id', function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(function (data) {
            $scope.entity.goods.typeTemplateId = data.typeId;
        })
    });

    //5,监控模板id的变化 查询品牌数据,和扩展属性数据
    $scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (data) {
            //获取模板对象
            $scope.typeTemplate = data;
            //获取品牌数据
            $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
            //获取扩展属性数据
            $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
        })
        //根据模板id查询获取规格数据
        typeTemplateService.findSpecListById(newValue).success(function (data) {
            $scope.specList = data;
        })
    });

    //上传文件的方法
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (data) {
            if (data.success) {
                //上传成功,取出url,设置文件地址
                $scope.image_entity.url = data.message;
            } else {
                //上传失败
                alert(data.message);
            }
        })
    };

    //定义页面的实体结构
    $scope.entity = {goods: {}, goodsDesc: {itemImages: [], specificationItems: []}};
    //定义保存的方法
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    };

    $scope.searchObjectByKey = function (list, key, value) {
        for (var i = 0; i < list.length; i++) {
            if (list[i][key] == value) {
                return list[i]
            }
        }
    };

    //点击规格选项选中时候执行的方法
    $scope.updateSpecAttribute = function ($event, attributeName, attributeValue) {
        //判断attributeName是否在数组中存在,如果存在返回这个数组中的对象,只push到attributeValue中
        var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, "attributeName", attributeName);

        if (object != null) {
            if ($event.target.checked) {
                object.attributeValue.push(attributeValue);
            } else {
                object.attributeValue.splice(object.attributeValue.indexOf(attributeValue), 1);
            }
        } else {
            $scope.entity.goodsDesc.specificationItems.push({
                "attributeName": attributeName,
                "attributeValue": [attributeValue]
            });
        }
    };

    //点击规格选项的时候执行的方法:创建sku列表
    $scope.createItemList = function () {
        //初始化itemList数组 初始化sku行
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];//初始
        //获取出你选择的规格集合数据
        var items = $scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i < items.length; i++) {
            //遍历集合 添加列/列
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
        }
    }
    //添加列值
    addColumn = function (list, columnName, conlumnValues) {
        var newList = [];//新的集合
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j < conlumnValues.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));//深克隆
                newRow.spec[columnName] = conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    };


});
