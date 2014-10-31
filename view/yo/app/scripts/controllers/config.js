'use strict';

angular.module('staticApp')
  .controller('ConfigController', function ($scope, $element, $http) {

    $scope.config = {};

    $scope.master = {};

    $scope.captureTweets = function (newConfig) {

      $scope.master = angular.copy(newConfig);

      $http.post('/rest/capture', $element.serialize());

    };

  });
