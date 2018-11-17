app.service("searchService",function ($http) {

    this.search=function (entity) {
        return $http.post('../itemSearch/search',entity);
    }
});