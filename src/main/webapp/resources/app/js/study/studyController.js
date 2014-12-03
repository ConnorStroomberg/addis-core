'use strict';
define(['angular', 'rdfstore'],
  function(angular, rdfstore) {
    var dependencies = ['$scope', '$stateParams', 'StudyResource', '$location', '$anchorScroll', '$modal', 'RdfstoreService'];
    var StudyController = function($scope, $stateParams, StudyResource, $location, $anchorScroll, $modal, RdfstoreService) {

      $scope.study = {};
      $scope.arms = {};
      var store;
      var studyQuery =
        'prefix ontology: <http://trials.drugis.org/ontology#>' +
        'prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>' +
        'prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>' +
        'prefix study: <http://trials.drugis.org/studies/>' +
        'prefix instance: <http://trials.drugis.org/instances/>' +
        'select' +
        ' ?label ?comment' +
        ' where {' +
        '    ?studyUid' +
        '      rdf:type ontology:Study ;' +
        '      rdfs:label ?label ; ' +
        '      rdfs:comment ?comment . ' +
        '}';

      var armsQuery =
        'prefix ontology: <http://trials.drugis.org/ontology#>' +
        'prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>' +
        'prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>' +
        'prefix study: <http://trials.drugis.org/studies/>' +
        'prefix instance: <http://trials.drugis.org/instances/>' +
        'select' +
        ' ?label' +
        ' where {' +
        '    ?armUid' +
        '      rdf:type ontology:Arm ;' +
        '      rdfs:label ?label . ' +
        '}';

      // $http(req).success(function(data, status, headers, config) {
      StudyResource.get($stateParams, function(responce, status) {
        // var arms;
        // var study;

        // if (result['@graph']) {

        //   study = _.find(result['@graph'], function(item) {
        //     return item['@type'] === 'http://trials.drugis.org/ontology#Study';
        //   });

        //   arms = _.filter(result['@graph'], function(item) {
        //     return item['@type'] === 'http://trials.drugis.org/ontology#Arm';
        //   });
        // } else {
        //   study = result;
        // }

        // study.arms = arms;
        // $scope.study = study;

        rdfstore.create(function(store) {
          store.load('text/n3', responce.n3Data, function(success, results) {

            store.execute(armsQuery, function(success, results) {
              if (success) {
                console.log(results);
                $scope.arms = results;
                $scope.$apply(); // rdf store does not trigger apply
              } else {
                console.error('armsQuery failed!');
              }
            });

            store.execute(studyQuery, function(success, results) {
              if (success) {
                console.log(results);
                $scope.study = results.length === 1 ? results[0] : console.error('single result expexted');
                $scope.$apply(); // rdf store does not trigger apply
              } else {
                console.error('armsQuery failed!');
              }
            });

          });
        });

      });

      $scope.sideNavClick = function(anchor) {
        var newHash = anchor;
        if ($location.hash() !== newHash) {
          $location.hash(anchor);
        } else {
          $anchorScroll();
        }
      };

      $scope.showArmDialog = function() {
        $modal.open({
          templateUrl: 'app/js/study/view/arm.html',
          scope: $scope
        });
      };
    };

    return dependencies.concat(StudyController);
  });