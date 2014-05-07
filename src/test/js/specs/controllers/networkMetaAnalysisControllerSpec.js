define(['angular', 'angular-mocks', 'controllers'], function() {
  describe('the network meta-analysis controller', function() {
    var scope,
      analysisDeferred,
      mockAnalysis = {},
      projectDeferred,
      mockProject = {
        id: 11
      },
      mockStateParams = {
        analysisId: 1,
        projectId: 11
      },
      mockOutcomes, outcomeResource;

    beforeEach(module('addis.controllers'));

    beforeEach(inject(function($rootScope, $controller, $q) {
      analysisDeferred = $q.defer();
      mockAnalysis.$promise = analysisDeferred.promise;

      projectDeferred = $q.defer();
      mockProject.$promise = projectDeferred.promise;

      scope = $rootScope;
      scope.$parent = {
        analysis: mockAnalysis,
        project: mockProject
      };
      outcomeResource = jasmine.createSpyObj('OutcomeResource', ['query']);

      $controller('NetworkMetaAnalysisController', {
        $scope: scope,
        $stateParams: mockStateParams,
        OutcomeResource: outcomeResource
      });
    }));

    describe('when first initialised', function() {
      it('should inherit the parent\'s analysis and project', function() {
        expect(scope.analysis).toEqual(scope.$parent.analysis);
        expect(scope.project).toEqual(scope.$parent.project);
      });

      it('should place the list of selectable outcomes on the scope', function() {
        expect(outcomeResource.query).toHaveBeenCalledWith({
          projectId: mockProject.id
        });
        expect(scope.outcomes).toEqual(mockOutcomes);
      });

    });

    describe('when the analysis and project are loaded', function() {

      beforeEach(inject(function($controller) {
        analysisDeferred.resolve(mockAnalysis);
        projectDeferred.resolve(mockProject);
        scope.$apply();
      }));

    });

  });
});