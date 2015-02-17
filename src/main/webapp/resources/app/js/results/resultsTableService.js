'use strict';
define([],
  function() {
    var dependencies = [];
    var ResultsTableService = function() {

      var CONTINUOUS_TYPE = 'http://trials.drugis.org/ontology#continuous',
        DICHOTOMOUS_TYPE = 'http://trials.drugis.org/ontology#dichotomous';

      function createInputColumns(measurementType) {
        if (measurementType === CONTINUOUS_TYPE) {
          return [{valueName: 'mean'}, {valueName: 'standard_deviation'}, {valueName: 'sample_size'}];
        } else if (measurementType === DICHOTOMOUS_TYPE) {
          return [{valueName: 'count'}, {valueName: 'sample_size'}];
        }
      }

      function createHeaders(measurementType) {
        if (measurementType === CONTINUOUS_TYPE) {
          return ['Mean', '± sd', 'N'];
        } else if (measurementType === DICHOTOMOUS_TYPE) {
          return ['Count', 'N'];
        }
      }

      function createRow(variable, arm, arms, measuredAtMoment, measurementMoments) {
        var row = {
          variable: variable,
          arm: arm,
          measurementMoment: _.find(measurementMoments, function(measurementMoment) {
            return measurementMoment.uri === measuredAtMoment.uri;
          }),
          nArms: arms.length,
          inputColumns: createInputColumns(variable.measurementType)
        };
        return row;
      }

      function createInputRows(variable, arms, measurementMoments, resultValues) {
        var rows = [];
        _.forEach(variable.measuredAtMoments, function(measuredAtMoments) {
          _.forEach(arms, function(arm) {
            rows = rows.concat(createRow(variable, arm, arms, measuredAtMoments, measurementMoments));
          });
        });
        return rows;
      }

      return {
        createInputRows: createInputRows,
        createHeaders: createHeaders,
        CONTINUOUS_TYPE: CONTINUOUS_TYPE,
        DICHOTOMOUS_TYPE: DICHOTOMOUS_TYPE
      };
    };
    return dependencies.concat(ResultsTableService);
  });
