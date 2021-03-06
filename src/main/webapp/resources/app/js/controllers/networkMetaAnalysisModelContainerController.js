'use strict';
define([], function() {
  var dependencies = ['$scope', '$state', 'ProjectResource', 'AnalysisResource', 'UserService'];

  var NetworkMetaAnalysisModelContainerController = function($scope, $state, ProjectResource, AnalysisResource, UserService) {
    $scope.project = ProjectResource.get({
      projectId: $state.params.projectId
    });
    $scope.analysis = AnalysisResource.get($state.params);
    $scope.userId = $state.params.userUid;
    $scope.userUid = $state.params.userUid;

    var isUserOwner = false;

    if (UserService.hasLoggedInUser()) {
      $scope.loginUserId = (UserService.getLoginUser()).id;
      isUserOwner = UserService.isLoginUserId($scope.project.owner.id);
    }

    $scope.editMode = {
      isUserOwner: isUserOwner,
      disableEditing: !isUserOwner
    };
  };

  return dependencies.concat(NetworkMetaAnalysisModelContainerController);
});
