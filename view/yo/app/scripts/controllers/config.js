'use strict';

angular.module('staticApp')
  .controller('ConfigController', function ($scope, $element, $http) {

    $scope.config = {};

    $scope.master = {};

    $scope.captureTweets = function (newConfig) {

      $scope.master = angular.copy(newConfig);

      $http.post('/rest/capture', $scope.master).success(function (data) {
        if (data.status == 'success') {
          alert('all okay');
        } else {
          alert(data.msg)
        }
      });

    };

  });
