var auctionApp = angular.module('auctionApp', ['ngRoute'])

.config(function($routeProvider, $locationProvider) {
    $routeProvider.when('/admin', {
        templateUrl: 'assets/admin.html',
        controller: 'AdminCtrl'
    });

    $routeProvider.when('/auctions', {
        templateUrl: 'assets/auctions.html',
        controller: 'AuctionListCtrl'
    });

    $routeProvider.when('/about', {
        templateUrl: 'assets/about.html'
    });

    $routeProvider.otherwise({redirectTo: '/auctions'});


});



auctionApp.controller('AdminCtrl', function($scope) {

});

auctionApp.controller('MainCtrl', function($scope, $location) {
    $scope.isActive = function (viewLocation) {
        var active = (viewLocation === $location.path());
        return active;
    };
});

auctionApp.controller('AuctionListCtrl', function($scope, $http) {
    $http.get('/auctions').success(function(data) {
        $scope.auctions = data
    });

});