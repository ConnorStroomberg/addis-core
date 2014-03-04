'use strict';
define([], function() {
  var dependencies = ['$scope', '$stateParams', 'ProjectsService', 'TrialverseService', 'SemanticOutcomeService'];
  var ProjectsController = function($scope, $stateParams, ProjectsService, TrialverseService, SemanticOutcomeService) {

    $scope.loading = {loaded : false};
    $scope.project = ProjectsService.get($stateParams);

    $scope.project.$promise.then(function() {
      $scope.trialverse = TrialverseService.get({id: $scope.project.trialverseId});
      $scope.semanticOutcomes = SemanticOutcomeService.query({id: $scope.project.trialverseId});
      $scope.loading.loaded = true;
    });
    $scope.addOutcome = function(outcome) {
      $scope.project.outcomes.push(outcome);
      ProjectsService.save($scope.project);
    };
  };
  return dependencies.concat(ProjectsController);
});