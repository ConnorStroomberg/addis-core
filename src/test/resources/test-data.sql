INSERT INTO public.account (id, username, firstname, lastname, email) VALUES (1, '1000123', 'Connor', 'Bonnor', 'connor@test.com');
INSERT INTO public.account (id, username, firstname, lastname, email) VALUES (2, '2000123', 'Daan', 'Baan', 'foo@bar.com');

INSERT INTO public.ApplicationKey (id, secretKey, accountId, applicationName, creationDate, revocationDate) VALUES (1, 'supersecretkey', 1, 'Test Application', '2015-09-01', '2080-12-31');

INSERT INTO public.project (id, owner, name, description, namespaceUid, datasetversion) VALUES (1, 1, 'testname 1', 'testdescription 1', 'namespaceUid-1', 'version-1');
INSERT INTO public.project (id, owner, name, description, namespaceUid, datasetversion) VALUES (2, 2, 'testname 2', 'testdescription 2', 'namespaceUid-1', 'version-1');
INSERT INTO public.project (id, owner, name, description, namespaceUid, datasetversion) VALUES (3, 1, 'testname 3', 'testdescription 3', 'namespaceUid-2', 'version-1');

INSERT INTO public.outcome (id, project, name, motivation, semanticOutcomeLabel, semanticOutcomeUri) VALUES (1, 1, 'outcome 1', 'outcome description 1', 'outcome1', 'org.drugis.addis.outcome://outcome1');
INSERT INTO public.outcome (id, project, name, motivation, semanticOutcomeLabel, semanticOutcomeUri) VALUES (2, 1, 'outcome 2', 'outcome description 2', 'outcome2', 'org.drugis.addis.outcome://outcome2');
INSERT INTO public.outcome (id, project, name, motivation, semanticOutcomeLabel, semanticOutcomeUri) VALUES (3, 2, 'outcome 3', 'outcome description 3', 'outcome3', 'org.drugis.addis.outcome://outcome3');

INSERT INTO public.abstractIntervention (id, project, name, motivation) VALUES (-1, 1, 'intervention 1', 'intervention description 1');
INSERT INTO public.abstractIntervention (id, project, name, motivation) VALUES (-2, 1, 'intervention 2', 'intervention description 2');
INSERT INTO public.abstractIntervention (id, project, name, motivation) VALUES (-3, 2, 'intervention 3', 'intervention description 3');
INSERT INTO public.abstractIntervention (id, project, name, motivation) VALUES (-4, 2, 'intervention 4', 'intervention description 4');
INSERT INTO public.abstractIntervention (id, project, name, motivation) VALUES (-5, 2, 'intervention 5', 'intervention description 5');
INSERT INTO public.abstractIntervention (id, project, name, motivation) VALUES (-6, 2, 'intervention 6', 'intervention description 6');

INSERT INTO public.singleIntervention (singleInterventionId, semanticInterventionLabel, semanticInterventionUri) VALUES (-1, 'intervention1', 'http://trials.drugis.org/namespaces/1/interventions/1');
INSERT INTO public.singleIntervention (singleInterventionId, semanticInterventionLabel, semanticInterventionUri) VALUES (-2, 'intervention2', 'http://trials.drugis.org/namespaces/1/interventions/2');
INSERT INTO public.singleIntervention (singleInterventionId, semanticInterventionLabel, semanticInterventionUri) VALUES (-3, 'intervention3', 'http://trials.drugis.org/namespaces/1/interventions/3');
INSERT INTO public.singleIntervention (singleInterventionId, semanticInterventionLabel, semanticInterventionUri) VALUES (-4, 'intervention4', 'http://trials.drugis.org/namespaces/1/interventions/4');
INSERT INTO public.singleIntervention (singleInterventionId, semanticInterventionLabel, semanticInterventionUri) VALUES (-5, 'intervention5', 'http://trials.drugis.org/namespaces/1/interventions/5');

INSERT INTO public.simpleIntervention (simpleInterventionId) VALUES (-1);
INSERT INTO public.simpleIntervention (simpleInterventionId) VALUES (-2);
INSERT INTO public.simpleIntervention (simpleInterventionId) VALUES (-3);

INSERT INTO public.fixedDoseIntervention (fixedInterventionId,
lowerBoundType, lowerBoundValue, lowerBoundUnitName, lowerBoundUnitPeriod,
upperBoundType, upperBoundValue, upperBoundUnitName, upperBoundUnitPeriod)
VALUES (-4,
'AT_LEAST', 3.0, 'mg', 'P1D',
'AT_MOST', 2.5, 'mg', 'P1D');

INSERT INTO public.titratedDoseIntervention (titratedInterventionId,
minLowerBoundType, minLowerBoundUnitName, minLowerBoundUnitPeriod, minLowerBoundValue, minLowerboundunitconcept,
minUpperBoundType, minUpperBoundUnitName, minUpperBoundUnitPeriod, minUpperBoundValue, minUpperboundunitconcept,
maxLowerBoundType, maxLowerBoundUnitName, maxLowerBoundUnitPeriod, maxLowerBoundValue, maxLowerboundunitconcept,
maxUpperBoundType, maxUpperBoundUnitName, maxUpperBoundUnitPeriod, maxUpperBoundValue, maxUpperboundunitconcept)
VALUES (-5,
'AT_LEAST', 'mg', 'P1D', 3.0, 'concept',
'AT_MOST',  'mg', 'P1D', 2.5, 'concept',
'AT_LEAST', 'mg', 'P1D', 4.0, 'concept',
'AT_MOST',  'mg', 'P1D', 3.5, 'concept');

INSERT INTO public.combinationIntervention (combinationInterventionId) VALUES(-6);
INSERT INTO public.InterventionCombination (combinationInterventionId,singleInterventionId ) VALUES (-6, -5);

INSERT INTO public.AbstractAnalysis(id, projectId, title) VALUES (-1, 1, 'analysis 1');
INSERT INTO public.AbstractAnalysis(id, projectId, title) VALUES (-2, 1, 'analysis 3');
INSERT INTO public.AbstractAnalysis(id, projectId, title) VALUES (-3, 1, 'analysis 2');
INSERT INTO public.AbstractAnalysis(id, projectId, title) VALUES (-4, 2, 'analysis 4');
INSERT INTO public.AbstractAnalysis(id, projectId, title) VALUES (-5, 1, 'nma');
INSERT INTO public.AbstractAnalysis(id, projectId, title) VALUES (-6, 1, 'nma 2');
INSERT INTO public.AbstractAnalysis(id, projectId, title) VALUES (-7, 2, 'nma task test');
INSERT INTO public.AbstractAnalysis(id, projectId, title) VALUES (-10, 1, 'metabr 1');

INSERT INTO public.SingleStudyBenefitRiskAnalysis (id) VALUES (-1);
INSERT INTO public.SingleStudyBenefitRiskAnalysis (id) VALUES (-2);
INSERT INTO public.SingleStudyBenefitRiskAnalysis (id) VALUES (-3);
INSERT INTO public.SingleStudyBenefitRiskAnalysis (id, problem) VALUES (-4, 'singlestudy problem');

INSERT INTO public.SingleStudyBenefitRiskAnalysis_outcome (analysisId, outcomeId) VALUES (-3, 1);
INSERT INTO public.SingleStudyBenefitRiskAnalysis_outcome (analysisId, outcomeId) VALUES (-3, 2);

INSERT INTO public.SingleStudyBenefitRiskAnalysis_outcome (analysisId, outcomeId) VALUES (-4, 1);
INSERT INTO public.SingleStudyBenefitRiskAnalysis_outcome (analysisId, outcomeId) VALUES (-4, 2);

INSERT INTO public.interventioninclusion (analysisId, interventionId) VALUES (-3, -1);
INSERT INTO public.interventioninclusion (analysisId, interventionId) VALUES (-3, -2);

INSERT INTO public.interventioninclusion (analysisId, interventionId) VALUES (-4, -1);
INSERT INTO public.interventioninclusion (analysisId, interventionId) VALUES (-4, -2);

INSERT INTO public.NetworkMetaAnalysis(id, outcomeId) VALUES (-5, 1);
INSERT INTO public.NetworkMetaAnalysis(id, outcomeId) VALUES (-6, 1);
INSERT INTO public.NetworkMetaAnalysis(id) VALUES (-7);

INSERT INTO public.model(id, analysisId, title, linearModel, modelType, heterogeneityPrior, burnInIterations, inferenceIterations, thinningFactor, likelihood, link) VALUES (1, -5, 'model title', 'fixed', '{"type": "network"}', '{"type": "automatic"}', 5000, 20000, 10, 'binom', 'logit');
INSERT INTO public.model(id, analysisId, title, linearModel, modelType, heterogeneityPrior, burnInIterations, inferenceIterations, thinningFactor, likelihood, link, outcomeScale) VALUES (2, -5, 'model title', 'fixed', '{"type": "pairwise", "details": {"to": {id: -1, "name" : "study1"}, "from": {"id": -2, "name": "study2"}}}', '{"type": "variance", "values": {"mean": 2.3, "stdDev": 0.3} }', 5000, 20000, 10,  'binom', 'logit', 2.2);

INSERT INTO public.model(id, analysisId, taskUrl, title, linearModel, modelType, burnInIterations, inferenceIterations, thinningFactor, likelihood, link) VALUES (3, -7, 'http://patavi-test.drugis.org/1', 'model title', 'fixed', '{"type": "network"}', 50, 20, 1, 'binom', 'logit');
INSERT INTO public.model(id, analysisId, taskUrl, title, linearModel, modelType, burnInIterations, inferenceIterations, thinningFactor, likelihood, link) VALUES (4, -7, 'http://patavi-test.drugis.org/2', 'model title', 'fixed', '{"type": "network"}', 50, 20, 1, 'binom', 'logit');

UPDATE public.NetworkMetaAnalysis SET primaryModel = 1 WHERE id = -7;

INSERT INTO public.MetaBenefitRiskAnalysis(id, finalized) VALUES (-10, FALSE);
INSERT INTO public.interventionInclusion(analysisId, interventionId) VALUES (-10, -1);
INSERT INTO public.MbrOutcomeInclusion(metaBenefitRiskAnalysisId, outcomeId, networkMetaAnalysisId, modelId) VALUES (-10, 1, -5, 1);

INSERT INTO public.scenario (id, workspace, title, state) VALUES (1, -1, 'Default', 'problem state');
INSERT INTO public.scenario (id, workspace, title, state) VALUES (2, -1, 'Scenario title', 'problem state modified');
INSERT INTO public.scenario (id, workspace, title, state) VALUES (3, -2, 'Default for different analysis', 'problem state modified');
INSERT INTO public.scenario (id, workspace, title, state) VALUES (4, -10, 'Default', 'problem state');


INSERT INTO public.armExclusion (id, trialverseUid, analysisId) VALUES (-1, '-101', -6);
INSERT INTO public.armExclusion (id, trialverseUid, analysisId) VALUES (-2, '-102', -6);

INSERT INTO public.interventionInclusion (interventionId, analysisId) VALUES (-2, -6);

INSERT INTO public.remarks(analysisId, remarks) VALUES(-1, 'yo yo yo !');

INSERT INTO public.covariate(id, project, name, motivation, definitionkey, type) VALUES (1, 1, 'covariate 1 name', 'my motivation', 'ALLOCATION_RANDOMIZED', 'STUDY_CHARACTERISTIC');
INSERT INTO public.covariate(id, project, name, motivation, definitionkey, type) VALUES (2, 1, 'covariate 2 name', 'my motivation', 'BLINDING_AT_LEAST_SINGLE_BLIND', 'STUDY_CHARACTERISTIC');
INSERT INTO public.covariate(id, project, name, motivation, definitionkey, type) VALUES (3, 2, 'covariate 3 name', 'my motivation', 'ALLOCATION_RANDOMIZED', 'STUDY_CHARACTERISTIC');

INSERT INTO public.VersionMapping (id, versionedDatasetUrl, ownerUuid, trialverseDatasetUrl) VALUES(1, 'http://datastoreserver/aaa-111', 'connor@test.com', 'http://trials.drugis.org/datasets/e2ab9670-d3c7-402c-81ad-60abbb46ca4c');
INSERT INTO public.VersionMapping (id, versionedDatasetUrl, ownerUuid, trialverseDatasetUrl) VALUES(2, 'http://datastoreserver/bbb-222', 'connor@test.com', 'http://trials.drugis.org/datasets/dbdf84e9-8bdb-4233-9bf6-c553cd023638');

INSERT INTO public.FeaturedDataset(dataseturl) VALUES('http://trials.drugis.org/datasets/e2ab9670-d3c7-402c-81ad-60abbb46ca4c');
INSERT INTO public.FeaturedDataset(dataseturl) VALUES('http://trials.drugis.org/datasets/dbdf84e9-8bdb-4233-9bf6-c553cd023638');

INSERT INTO public.funnelPlot(id, modelId) VALUES (-1, 1);

INSERT INTO public.funnelPlotComparison(plotId, t1, t2, biasDirection) VALUES (-1, 2, 3, 0);
INSERT INTO public.funnelPlotComparison(plotId, t1, t2, biasDirection) VALUES (-1, 3, 4, 1);