'use strict';
define(['lodash', 'angular'], function(_, angular) {
  var dependencies = ['$scope', '$q', '$stateParams', '$state', 'AnalysisResource', 'InterventionResource',
    'OutcomeResource', 'MetaBenefitRiskService', 'ModelResource'
  ];
  var MetBenefitRiskStep1Controller = function($scope, $q, $stateParams, $state, AnalysisResource, InterventionResource,
    OutcomeResource, MetaBenefitRiskService, ModelResource) {
    $scope.updateAlternatives = updateAlternatives;
    $scope.updateMbrOutcomeInclusions = updateMbrOutcomeInclusions;
    $scope.updateAnalysesInclusions = updateAnalysesInclusions;
    $scope.isOutcomeDisabled = isOutcomeDisabled;
    $scope.updateModelSelection = updateModelSelection;
    $scope.goToStep2 = goToStep2;

    $scope.analysis = AnalysisResource.get($stateParams);
    $scope.alternatives = InterventionResource.query($stateParams);
    $scope.outcomes = OutcomeResource.query($stateParams);
    $scope.models = ModelResource.getConsistencyModels($stateParams);


    function goToStep2() {
      $state.go('MetaBenefitRiskCreationStep-2', $stateParams);
    }

    /*
     ** (1) two or more interventions have been selected,
     ** (2) two or more outcomes have been selected,
     ** (3) for each outcome, an analysis and model have been selected,
     **     and the selected model includes all selected interventions and has results
     */
    function checkStep1Validity() {
      $scope.step1AlertMessages = [];
      //(1)
      var numberOfSelectedInterventions = MetaBenefitRiskService.numberOfSelectedInterventions($scope.alternatives);
      if (numberOfSelectedInterventions < 2) {
        $scope.step1AlertMessages.push('At least two alternatives must be selected.');
      }
      //(2)
      var numberOfSelectedOutcomes = MetaBenefitRiskService.numberOfSelectedOutcomes($scope.outcomesWithAnalyses);
      if (numberOfSelectedOutcomes < 2) {
        $scope.step1AlertMessages.push('At least two outcomes must be selected.');
      }
      //(3)
      var isModelWithMissingAlternatives = MetaBenefitRiskService.isModelWithMissingAlternatives($scope.outcomesWithAnalyses);
      if (isModelWithMissingAlternatives) {
        $scope.step1AlertMessages.push('A model with missing alternatives is selected');
      }
      //(3)
      var isModelWithoutResults = MetaBenefitRiskService.isModelWithoutResults($scope.outcomesWithAnalyses);
      if (isModelWithoutResults) {
        $scope.step1AlertMessages.push('A model that has not yet run is selected');
      }
    }

    function updateAnalysesInclusions(changedOutcome) {
      changeModelSelection(changedOutcome);
      if (changedOutcome.selectedModel) {
        updateMissingAlternatives(changedOutcome);
      }
      buildInclusions();
    }

    function updateMbrOutcomeInclusions(changedOutcome) {
      changeAnalysisSelection(changedOutcome);
      updateAnalysesInclusions(changedOutcome);
    }

    function updateModelSelection(owa) {
      if (owa.selectedModel) {
        updateMissingAlternatives(owa);
      }
      buildInclusions();
    }

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
          });
        updateMissingAlternativesForAllOutcomes();

        // when view setup is completed
        checkStep1Validity();
      });

      $scope.alternatives = alternatives.map(function(alternative) {
        var isAlternativeInInclusions = analysis.interventionInclusions.find(function(includedAlternative) {
          return includedAlternative.interventionId === alternative.id;
        });
        if (isAlternativeInInclusions) {
          alternative.isIncluded = true;
        }
        return alternative;
      });
      setIncludedAlternatives();

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

    function isOutcomeDisabled(outcomeWithAnalyses) {
      return !outcomeWithAnalyses.networkMetaAnalyses.length ||
        !hasSelectableAnalysis(outcomeWithAnalyses);
    }

    function setIncludedAlternatives() {
      $scope.analysis.interventionInclusions = $scope.alternatives.filter(function(alternative) {
        return alternative.isIncluded;
      });
    }

    function updateMissingAlternativesForAllOutcomes() {
      $scope.outcomesWithAnalyses.filter(function(outcome) {
        return outcome.selectedModel;
      }).forEach(function(outcome) {
        updateMissingAlternatives(outcome);
      });
    }

    function updateAlternatives() {
      setIncludedAlternatives();
      updateMissingAlternativesForAllOutcomes();
      buildAnalysisCommand($scope.analysis).$save();
      checkStep1Validity();
    }

    function changeAnalysisSelection(outcomeWithAnalyses) {
      var analysis;
      if (hasSelectableAnalysis(outcomeWithAnalyses) && outcomeWithAnalyses.outcome.isIncluded) {
        analysis = outcomeWithAnalyses.networkMetaAnalyses[0];
        outcomeWithAnalyses.selectedAnalysis = analysis;
      } else {
        outcomeWithAnalyses.selectedAnalysis = undefined;
      }
      return analysis;
    }

    function hasSelectableAnalysis(outcomeWithAnalyses) {
      var firstAnalysis = outcomeWithAnalyses.networkMetaAnalyses[0];
      return firstAnalysis && firstAnalysis.models.length;
    }

    function changeModelSelection(changedOutcome) {
      var selectedNma = changedOutcome.selectedAnalysis;
      if (selectedNma !== undefined) {
        var primaryModel = selectedNma.models.find(function(model) {
          return model.id === selectedNma.primaryModel;
        });
        if (primaryModel) {
          changedOutcome.selectedModel = primaryModel;
        } else {
          changedOutcome.selectedModel = selectedNma.models[0];
        }
      } else {
        changedOutcome.selectedModel = undefined;
      }
    }

    function updateMissingAlternatives(owa) {
      owa.selectedModel.missingAlternatives = MetaBenefitRiskService.findMissingAlternatives($scope.analysis.interventionInclusions, owa);
      owa.selectedModel.missingAlternativesNames = _.map(owa.selectedModel.missingAlternatives, 'name');
    }

    function buildAnalysisCommand(analysis){
      var analysisCommand = angular.copy(analysis);
      analysisCommand.interventionInclusions = analysis.interventionInclusions.map(function(includedIntervention) {
        return {
          analysisId: $scope.analysis.id,
          interventionId: includedIntervention.id
        };
      });
      return analysisCommand;
    }

    function buildInclusions() {
      $scope.analysis.mbrOutcomeInclusions = $scope.outcomesWithAnalyses.filter(function(outcomeWithAnalyses) {
        return outcomeWithAnalyses.outcome.isIncluded;
      }).map(function(outcomeWithAnalyses) {
        return {
          metaBenefitRiskAnalysisId: $scope.analysis.id,
          outcomeId: outcomeWithAnalyses.outcome.id,
          networkMetaAnalysisId: outcomeWithAnalyses.selectedAnalysis.id,
          modelId: outcomeWithAnalyses.selectedModel.id
        };
      });
      checkStep1Validity();
      buildAnalysisCommand($scope.analysis).$save();
    }

  };
  return dependencies.concat(MetBenefitRiskStep1Controller);
});
