'use strict';

angular.module('staticApp')
  .controller('ConfigController', ['$scope', '$element', '$http', '$log', function ($scope, $element, $http, $log) {

    $scope.config = {};

    $scope.ingestStatus = {};

    $scope.startIngest = function () {

      $http.post('/rest/ingest/start', $scope.config).success(function (data, status, headers, config) {
        $log.debug(data);

        $scope.ingestStatus = data;

      });

    };

    $scope.stopIngest = function () {

      $http.post('/rest/ingest/stop', $scope.config).success(function (data, status, headers, config) {
        $log.debug(data);

        $scope.ingestStatus = data;

      });

    };

    var init = function () {
      $http.get('/rest/ingest/status').success(function (data, status, headers, config) {
        $log.debug(data);

        $scope.ingestStatus = data;

      });
    };
// and fire it after definition
    init();

  }]);
