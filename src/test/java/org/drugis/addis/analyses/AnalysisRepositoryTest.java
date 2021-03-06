package org.drugis.addis.analyses;

import org.drugis.addis.analyses.repository.AnalysisRepository;
import org.drugis.addis.config.JpaRepositoryTestConfig;
import org.drugis.addis.exception.ResourceDoesNotExistException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by daan on 7-5-14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {JpaRepositoryTestConfig.class})
public class AnalysisRepositoryTest {

  @Inject
  private AnalysisRepository analysisRepository;

  @PersistenceContext(unitName = "addisCore")
  EntityManager em;

  @Test
  public void testGetSingleStudyBenefitRiskAnalysis() throws ResourceDoesNotExistException {
    Integer analysisId = -4;
    AbstractAnalysis analysis = analysisRepository.get(analysisId);
    SingleStudyBenefitRiskAnalysis castAnalysis = (SingleStudyBenefitRiskAnalysis) analysis;
    SingleStudyBenefitRiskAnalysis singleStudyBenefitRiskAnalysis = em.find(SingleStudyBenefitRiskAnalysis.class, analysisId);
    assertEquals(2, singleStudyBenefitRiskAnalysis.getSelectedOutcomes().size());
    assertEquals(singleStudyBenefitRiskAnalysis, castAnalysis);
  }

  @Test
  public void testGetNetworkMetaAnalysis() throws ResourceDoesNotExistException {
    Integer analysisId = -5;
    AbstractAnalysis analysis = analysisRepository.get(analysisId);
    NetworkMetaAnalysis castAnalysis = (NetworkMetaAnalysis) analysis;
    NetworkMetaAnalysis networkMetaAnalysis = em.find(NetworkMetaAnalysis.class, analysisId);
    assertEquals(networkMetaAnalysis, castAnalysis);
  }

  @Test
  public void testGetMetaBenefitRiskAnalysis() throws ResourceDoesNotExistException {
    Integer analysisId = -10;
    AbstractAnalysis analysis = analysisRepository.get(analysisId);
    MetaBenefitRiskAnalysis castAnalysis = (MetaBenefitRiskAnalysis) analysis;
    MetaBenefitRiskAnalysis metaBenefitRiskAnalysis = em.find(MetaBenefitRiskAnalysis.class, analysisId);
    metaBenefitRiskAnalysis = em.find(MetaBenefitRiskAnalysis.class, metaBenefitRiskAnalysis.getId());
    assertEquals(1, metaBenefitRiskAnalysis.getInterventionInclusions().size());
    assertEquals(metaBenefitRiskAnalysis, castAnalysis);
  }

  @Test
  public void testGetNetworkMetaAnalysisWithExcludedArms() throws ResourceDoesNotExistException {
    Integer analysisId = -6;
    AbstractAnalysis analysis = analysisRepository.get(analysisId);
    NetworkMetaAnalysis castAnalysis = (NetworkMetaAnalysis) analysis;
    NetworkMetaAnalysis networkMetaAnalysis = em.find(NetworkMetaAnalysis.class, analysisId);
    assertEquals(networkMetaAnalysis, castAnalysis);
  }

  @Test(expected = ResourceDoesNotExistException.class)
  public void testGetNonexistentAnalysisFails() throws ResourceDoesNotExistException {
    analysisRepository.get(12345);
  }

  @Test
  public void testQuery() {
    Integer projectId = 1;
    List<AbstractAnalysis> analyses = analysisRepository.query(projectId);
    assertEquals(6, analyses.size());
    SingleStudyBenefitRiskAnalysis analysis = new SingleStudyBenefitRiskAnalysis(-1, 1, "analysis 1", Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    assertTrue(analyses.contains(analysis));
  }

}
