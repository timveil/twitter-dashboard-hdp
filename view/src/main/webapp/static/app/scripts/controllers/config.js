'use strict';

angular.module('staticApp')
  .controller('ConfigController', ['$scope', function ($scope) {

    $scope.config = {};

    $scope.master = {};

    $scope.captureTweets = function (newConfig) {
      $scope.master = angular.copy(newConfig);
    }

  }]);
