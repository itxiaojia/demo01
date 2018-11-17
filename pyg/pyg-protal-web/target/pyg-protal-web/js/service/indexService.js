app.service('indexService',function ($http) {

    this.findContentCategoryById=function (categoryId) {
        return $http.get('../content/findContentCategoryById?categoryId='+categoryId);
    }
});