'use strict';
define(['lodash'],
  function(_) {
    var dependencies = ['$scope', '$state', '$modalInstance', 'itemService', 'callback', 'item'];
    var EditArmController = function($scope, $state, $modalInstance, itemService, callback, item) {

      $scope.isEditing = false;
      $scope.item = item;
      itemService.queryItems().then(function(arms) {
        $scope.otherArms = _.filter(arms, function(arm) {
          return arm.armURI !== $scope.item.armURI;
        });
      });
      $scope.showMergeWarning = false;

      $scope.editItem = function() {
        $scope.isEditing = true;
        itemService.editItem(item).then(function() {
            callback();
            $modalInstance.close();
          },
          function() {
            $modalInstance.dismiss('cancel');
          });
      };

      $scope.reclassifyAsGroup = function() {
        $scope.isEditing = true;
        itemService.reclassifyAsGroup(item).then(function() {
            callback();
            $modalInstance.close();
          },
          function() {
            $modalInstance.dismiss('cancel');
          });
      };

      $scope.merge = function(targetArm) {
        $scope.isEditing = true;
        itemService.merge(item, targetArm).then(function() {
            callback();
            $modalInstance.close();
          },
          function() {
            $modalInstance.dismiss('cancel');
          });
      };

      $scope.updateMergeWarning = function(targetArm) {
        itemService.hasOverlap(item, targetArm).then(function(result) {
          $scope.showMergeWarning = result;
        });
      };

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };
    };
    return dependencies.concat(EditArmController);
  });
