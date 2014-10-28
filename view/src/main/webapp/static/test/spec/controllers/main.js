'use strict';

describe('Controller: ConfigController', function () {

  // load the controller's module
  beforeEach(module('staticApp'));

  var MainCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MainCtrl = $controller('ConfigController', {
      $scope: scope
    });
  }));


});
