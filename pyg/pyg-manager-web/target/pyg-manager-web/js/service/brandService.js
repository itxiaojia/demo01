//定义服务
app.service("brandService",function ($http) {
    //查询所有品牌
    this.findAll=function () {
        return $http.get("../brand/findAll");
    };
    //分页查询的方法
    this.findPage=function (page,rows) {
        return $http.get("../brand/findPage/"+page+"/"+rows);
    };
    //添加品牌方法
    this.add=function (entity) {
        return $http.post("../brand/save",entity);
    };
    //更新品牌的方法
    this.update=function (entity) {
        return $http.post("../brand/update",entity);
    };
    //删除品牌方法
    this.dele=function (ids) {
        return $http.get("../brand/delete/"+ids);
    };
    //根据id查询品牌数据
    this.findOne=function (id) {
        return $http.get("../brand/findOne/"+id);
    };
    //定义搜索方法
    this.searchResult=function (page, rows,query) {
        return $http.post("../brand/findQv?page="+page+"&rows="+rows,query);
    };
    //查询品牌数据 下拉款
    this.findBrandList=function () {
        return $http.get("../brand/findBrandList");
    }
});