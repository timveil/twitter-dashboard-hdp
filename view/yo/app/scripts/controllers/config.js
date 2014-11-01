'use strict';

angular.module('staticApp')
  .controller('ConfigController', function ($scope, $element, $http) {

    $scope.config = {};

    $scope.master = {};

    $scope.startIngest = function (newConfig) {

      $scope.master = angular.copy(newConfig);

      $http.post('/rest/ingest/start', $scope.master).success(function (data, status, headers, config) {
      });

    };

    $scope.stopIngest = function () {

      $http.post('/rest/ingest/stop', $scope.master).success(function (data, status, headers, config) {
      });

    };

  });
