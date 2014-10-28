'use strict';

angular.module('staticApp')
  .controller('ConfigController', function ($scope, $http) {

    $scope.config = {};

    $scope.master = {};

    $scope.captureTweets = function (newConfig) {

      $scope.master = angular.copy(newConfig);

      $http.post('/view_war_exploded/rest/capture', {duration:10, users: 'timveil'});


    };

  });
