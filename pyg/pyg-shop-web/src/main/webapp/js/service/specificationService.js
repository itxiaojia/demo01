//定义服务
app.service("specificationService",function ($http) {
    //查询所有规格
    this.findAll=function () {
        return $http.get("../specification/findAll");
    };
    //分页查询的方法
    this.findPage=function (page,rows) {
        return $http.get("../specification/findPage/"+page+"/"+rows);
    };
    //添加规格方法
    this.add=function (entity) {
        return $http.post("../specification/save",entity);
    };
    //更新规格的方法
    this.update=function (entity) {
        return $http.post("../specification/update",entity);
    };
    //删除规格方法
    this.dele=function (ids) {
        return $http.get("../specification/delete/"+ids);
    };
    //根据id查询规格数据
    this.findOne=function (id) {
        return $http.get("../specification/findOne/"+id);
    };
    //定义搜索方法
    this.searchResult=function (page, rows,query) {
        return $http.post("../specification/findQv?page="+page+"&rows="+rows,query);
    };
    //定义查询规格显示下拉款方法
    this.findSpecList=function () {
        return $http.get("../specification/findSpecList");
    }
});