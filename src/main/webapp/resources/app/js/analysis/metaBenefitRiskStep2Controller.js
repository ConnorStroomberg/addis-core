'use strict';
define(['lodash'], function(_) {
  var dependencies = ['$scope', '$q', '$stateParams', '$state', '$modal',
    'AnalysisResource', 'InterventionResource', 'OutcomeResource', 'MetaBenefitRiskService', 'ModelResource'
  ];
  var MetBenefitRiskStep2Controller = function($scope, $q, $stateParams, $state, $modal,
    AnalysisResource, InterventionResource, OutcomeResource, MetaBenefitRiskService, ModelResource) {

    $scope.goToStep1 = goToStep1;
    $scope.openDistributionModal = openDistributionModal;

    $scope.analysis = AnalysisResource.get($stateParams);
    $scope.alternatives = InterventionResource.query($stateParams);
    $scope.outcomes = OutcomeResource.query($stateParams);
    $scope.models = ModelResource.getConsistencyModels($stateParams);

    var promises = [$scope.analysis.$promise, $scope.alternatives.$promise, $scope.outcomes.$promise, $scope.models.$promise];

    $q.all(promises).then(function(result) {
      var analysis = result[0];
      var alternatives = result[1];
      var outcomes = result[2];
      var models = result[3];
      var outcomeIds = outcomes.map(function(outcome) {
        return outcome.id;
      });

      AnalysisResource.query({
        projectId: $stateParams.projectId,
        outcomeIds: outcomeIds
      }).$promise.then(function(networkMetaAnalyses) {
        
        networkMetaAnalyses = networkMetaAnalyses
          .map(_.partial(MetaBenefitRiskService.joinModelsWithAnalysis, models))
          .map(MetaBenefitRiskService.addModelsGroup);

        $scope.outcomesWithAnalyses = outcomes
          .map(_.partial(MetaBenefitRiskService.buildOutcomesWithAnalyses, analysis, networkMetaAnalyses, models))
          .map(function(owa) {
            owa.networkMetaAnalyses = owa.networkMetaAnalyses.sort(MetaBenefitRiskService.compareAnalysesByModels);
            return owa;
          })
          .filter(function(owa) {
            return owa.outcome.isIncluded;
          })
          .map(function(owa) {
            owa.baselineDistribution = $scope.analysis.mbrOutcomeInclusions.find(function(inclusion) {
              return inclusion.outcomeId === owa.outcome.id;
            }).baseline;
            return owa;
          });

      });

      $scope.alternatives = alternatives.map(function(alternative) {
        var isAlternativeInInclusions = analysis.includedAlternatives.find(function(includedAlternative) {
          return includedAlternative.id === alternative.id;
        });
        if (isAlternativeInInclusions) {
          alternative.isIncluded = true;
        }
        return alternative;
      });

      $scope.outcomes = outcomes.map(function(outcome) {
        var isOutcomeInInclusions = analysis.mbrOutcomeInclusions.find(function(mbrOutcomeInclusion) {
          return mbrOutcomeInclusion.outcomeId === outcome.id;
        });
        if (isOutcomeInInclusions) {
          outcome.isIncluded = true;
        }
        return outcome;
      });
    });


    function goToStep1() {
      $state.go('MetaBenefitRiskCreationStep-1', $stateParams);
    }

    function openDistributionModal(owa) {
      $modal.open({
        templateUrl: './app/js/analysis/setBaselineDistribution.html',
        controller: 'SetBaselineDistributionController',
        windowClass: 'small',
        resolve: {
          outcomeWithAnalysis: function() {
            return owa;
          },
          includedAlternatives: function() {
            return $scope.analysis.includedAlternatives;
          },
          setBaselineDistribution: function() {
            return function(baseline) {
              $scope.analysis.mbrOutcomeInclusions.map(function(mbrOutcomeInclusion){
                if(mbrOutcomeInclusion.outcomeId === owa.outcome.id) {
                  return _.extend(mbrOutcomeInclusion, {baseline: baseline});
                } else {
                  return mbrOutcomeInclusion;
                }
              });
              $scope.analysis.$save();
            };
          }
        }
      });
    }

  };
  return dependencies.concat(MetBenefitRiskStep2Controller);
});