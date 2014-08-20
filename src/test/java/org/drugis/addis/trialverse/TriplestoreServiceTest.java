package org.drugis.addis.trialverse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.drugis.addis.TestUtils;
import org.drugis.addis.exception.ResourceDoesNotExistException;
import org.drugis.addis.trialverse.factory.RestOperationsFactory;
import org.drugis.addis.trialverse.model.SemanticIntervention;
import org.drugis.addis.trialverse.model.SemanticOutcome;
import org.drugis.addis.trialverse.model.StudyWithDetails;
import org.drugis.addis.trialverse.model.TreatmentActivity;
import org.drugis.addis.trialverse.model.emun.StudyDataType;
import org.drugis.addis.trialverse.service.TriplestoreService;
import org.drugis.addis.trialverse.service.impl.TriplestoreServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by connor on 2/28/14.
 */

public class TriplestoreServiceTest {

  @Mock
  RestOperationsFactory restOperationsFactory;

  @InjectMocks
  TriplestoreService triplestoreService;

  @Before
  public void setUp() {
    triplestoreService = new TriplestoreServiceImpl();
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetOutcomes() {
    String mockResult = TestUtils.loadResource(this.getClass(), "/triplestoreService/exampleOutcomeResult.json");
    createMockTrialverseService(mockResult);
    List<SemanticOutcome> result = triplestoreService.getOutcomes("abc");
    SemanticOutcome result1 = new SemanticOutcome("fdszgs-adsfd-1", "DBP 24-hour mean");
    assertEquals(result.get(0), result1);
  }

  @Test
  public void testGetInterventions() {
    String mockResult = TestUtils.loadResource(this.getClass(), "/triplestoreService/exampleInterventionResult.json");
    createMockTrialverseService(mockResult);

    List<SemanticIntervention> result = triplestoreService.getInterventions("abc");
    SemanticIntervention intervention = result.get(0);
    SemanticIntervention expectedSemanticIntervention = new SemanticIntervention("fdhdfgh-saddsgfsdf-123-a", "Azilsartan");
    assertEquals(expectedSemanticIntervention, intervention);
  }


  @Test
  public void testQueryStudydetails() {
    String mockResult = TestUtils.loadResource(this.getClass(), "/triplestoreService/exampleQueryStudyDetailsResult.json");
    createMockTrialverseService(mockResult);

    String namespaceUid = "namespaceUid";
    List<StudyWithDetails> studyWithDetailsList = triplestoreService.queryStudydetails(namespaceUid);

    assertNotNull(studyWithDetailsList);
    assertEquals(2, studyWithDetailsList.size());
    StudyWithDetails studyWithDetailsNoStartOrEndDate = studyWithDetailsList.get(0);

    assertNotNull(studyWithDetailsNoStartOrEndDate.getStudyUid());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getName());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getTitle());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getAllocation());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getBlinding());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getInclusionCriteria());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getNumberOfStudyCenters());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getPubmedUrls());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getStatus());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getIndication());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getObjectives());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getInvestigationalDrugNames());
    assertNotNull(studyWithDetailsNoStartOrEndDate.getNumberOfArms());

    assertNull(studyWithDetailsNoStartOrEndDate.getStartDate());
    assertNull(studyWithDetailsNoStartOrEndDate.getEndDate());

    StudyWithDetails studyWithDetailsWithStartAndEndDate = studyWithDetailsList.get(1);
    assertNotNull(studyWithDetailsWithStartAndEndDate.getStartDate());
    assertNotNull(studyWithDetailsWithStartAndEndDate.getEndDate());
  }

  @Test
  public void testGetStudyDetails() throws ResourceDoesNotExistException {
    String namespaceUid = "namespaceUid";
    String studyUid = "studyUid";

    String mockResult = TestUtils.loadResource(this.getClass(), "/triplestoreService/exampleGetStudyDetailsResult.json");
    createMockTrialverseService(mockResult);

    StudyWithDetails studyWithDetails = triplestoreService.getStudydetails(namespaceUid, studyUid);

    assertNotNull(studyWithDetails.getStudyUid());
    assertNotNull(studyWithDetails.getName());
    assertNotNull(studyWithDetails.getTitle());
  }

  @Test
  public void testGetStudyArms() throws ResourceDoesNotExistException {
    String namespaceUid = "namespaceUid";
    String studyUid = "studyUid";

    String mockResult = TestUtils.loadResource(this.getClass(), "/triplestoreService/exampleStudyArmsResult.json");
    createMockTrialverseService(mockResult);

    JSONArray arms = triplestoreService.getStudyArms(namespaceUid, studyUid);

    assertEquals(3, arms.size());
    JSONObject jsonObject = (JSONObject) arms.get(0);
    assertTrue(jsonObject.containsKey("arm"));
    assertTrue(jsonObject.containsKey("armLabel"));
    assertTrue(jsonObject.containsKey("numberOfParticipantsStarting"));

    assertTrue(jsonObject.containsValue("http://trials.drugis.org/instances/5959fd08-9c5b-4016-8118-d195cdb80c70"));
    assertTrue(jsonObject.containsValue("Olmesartan medoxomil 20-40mg/hydrochlorothiazide 12.5-25mg QD"));
    assertTrue(jsonObject.containsValue("356"));
  }

  @Test
  public void testGetTreatmentActivities() {
    String namespaceUid = "namespaceUid";
    String studyUid = "studyUid";

    String mockResult = TestUtils.loadResource(this.getClass(), "/triplestoreService/exampleStudyTreatmentActivitiesResult.json");
    createMockTrialverseService(mockResult);

    List<TreatmentActivity> treatmentActivities = triplestoreService.getStudyTreatmentActivities(namespaceUid, studyUid);
    assertEquals(4, treatmentActivities.size());
  }

  @Test
  public void testGetStudyDataForBaseLineCharacteristics() {
    String namespaceUid = "namespaceUid";
    String studyUid = "studyUid";
    StudyDataType studyDataType = StudyDataType.BASE_LINE_CHARACTERISTICS;

    String mockResult = TestUtils.loadResource(this.getClass(), "/triplestoreService/exampleBaseLineCharacteristicsResult.json");
    createMockTrialverseService(mockResult);

    JSONArray result = triplestoreService.getStudyData(namespaceUid, studyUid, studyDataType);
    assertEquals(20, result.size());
  }

  @Test
  public void testRegEx() {
    String studyOptionsString = "1|2";
    String uri1 = "foo/study/1/whatevr";
    String uri10 = "foo/study/10/whatevr";
    String uri12 = "foo/study/12/whatevr";
    String reg = "/study/(" + studyOptionsString + ")/";
    Pattern pattern = Pattern.compile(reg);
    Matcher matcher1 = pattern.matcher(uri1);
    Matcher matcher10 = pattern.matcher(uri10);
    Matcher matcher12 = pattern.matcher(uri12);
    assertTrue(matcher1.find());
    assertFalse(matcher10.find());
    assertFalse(matcher12.find());
  }

  private void createMockTrialverseService(String result) {
    RestOperations restTemplate = mock(RestTemplate.class);
    when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(Class.class), Mockito.anyMap())).thenReturn(result);
    when(restOperationsFactory.build()).thenReturn(restTemplate);

  }

}
