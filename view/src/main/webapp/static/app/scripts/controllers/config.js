'use strict';

angular.module('staticApp')
  .controller('ConfigController', ['$scope', function ($scope) {
    $scope.config = {};

    $scope.master = {};

    $scope.capture = function (config) {
      $scope.master = angular.copy(config);
    }

  }]);
