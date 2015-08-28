package org.drugis.trialverse.graph.controller;

import org.apache.http.Header;

import org.apache.jena.riot.RDFLanguages;
import org.drugis.trialverse.dataset.exception.RevisionNotFoundException;
import org.drugis.trialverse.dataset.repository.DatasetReadRepository;
import org.drugis.trialverse.exception.MethodNotAllowedException;
import org.drugis.trialverse.graph.exception.ReadGraphException;
import org.drugis.trialverse.graph.exception.UpdateGraphException;
import org.drugis.trialverse.graph.repository.GraphReadRepository;
import org.drugis.trialverse.graph.repository.GraphWriteRepository;
import org.drugis.trialverse.graph.service.GraphService;
import org.drugis.trialverse.security.repository.AccountRepository;
import org.drugis.trialverse.util.Namespaces;
import org.drugis.trialverse.util.WebConstants;
import org.drugis.trialverse.util.controller.AbstractTrialverseController;
import org.drugis.trialverse.util.service.TrialverseIOUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daan on 19-11-14.
 */
@Controller
@RequestMapping(value = "/users/{userUid}/datasets/{datasetUuid}")
public class GraphController extends AbstractTrialverseController {

  @Inject
  private GraphService graphService;

  @Inject
  private WebConstants webConstants;

  @Inject
  private GraphReadRepository graphReadRepository;

  @Inject
  private GraphWriteRepository graphWriteRepository;

  @Inject
  private DatasetReadRepository datasetReadRepository;

  @Inject
  private AccountRepository accountRepository;

  @Inject
  private TrialverseIOUtilsService trialverseIOUtilsService;

  Logger logger = LoggerFactory.getLogger(getClass());

  @RequestMapping(value = "/versions/{versionUuid}/graphs/{graphUuid}", method = RequestMethod.GET)
  @ResponseBody
  public void getGraph(HttpServletResponse httpServletResponse, @PathVariable String datasetUuid,
                       @PathVariable String versionUuid, @PathVariable String graphUuid) throws URISyntaxException, IOException, ReadGraphException {
    logger.trace("get graph");
    byte[] responseContent = graphReadRepository.getGraph(new URI(Namespaces.DATASET_NAMESPACE + datasetUuid), versionUuid, graphUuid);
    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    httpServletResponse.setHeader("Content-Type", RDFLanguages.TURTLE.getContentType().getContentType());
    trialverseIOUtilsService.writeContentToServletResponse(responseContent, httpServletResponse);
  }

  @RequestMapping(value = "/graphs/{graphUuid}", method = RequestMethod.PUT, params = {WebConstants.COMMIT_TITLE_PARAM})
  public void setGraph(HttpServletRequest request, HttpServletResponse trialversResponse, Principal currentUser,
                       @RequestParam(WebConstants.COMMIT_TITLE_PARAM) String commitTitle, // here because it's required
                       @PathVariable String datasetUuid, @PathVariable String graphUuid)
          throws IOException, MethodNotAllowedException, URISyntaxException, UpdateGraphException {
    logger.trace("set graph");
    URI trialverseDatasetUri = new URI(Namespaces.DATASET_NAMESPACE + datasetUuid);
    if (datasetReadRepository.isOwner(trialverseDatasetUri, currentUser)) {
      Header versionHeader = graphWriteRepository.updateGraph(new URI(Namespaces.DATASET_NAMESPACE + datasetUuid), graphUuid, request);
      trialversResponse.setHeader(WebConstants.X_EVENT_SOURCE_VERSION, versionHeader.getValue());
      trialversResponse.setStatus(HttpStatus.OK.value());
    } else {
      throw new MethodNotAllowedException();
    }
  }

  @RequestMapping(value = "/graphs/{graphUuid}", method = RequestMethod.PUT, params = {WebConstants.COPY_OF_QUERY_PARAM})
  public void copyGraph(HttpServletRequest request, HttpServletResponse trialversResponse, Principal currentUser,
                        @RequestParam(WebConstants.COPY_OF_QUERY_PARAM) String copyOfUri,
                        @PathVariable String datasetUuid, @PathVariable String graphUuid)
          throws IOException, MethodNotAllowedException, URISyntaxException, UpdateGraphException, RevisionNotFoundException {
    logger.trace("copy graph");
    URI trialverseDatasetUri = new URI(Namespaces.DATASET_NAMESPACE + datasetUuid);
    if (datasetReadRepository.isOwner(trialverseDatasetUri, currentUser)) {
      String sourceVersionUuid = graphService.extractVersionUuid(copyOfUri);
      String sourceDatasetUuid = graphService.extractDatasetUuid(copyOfUri);
      String sourceGraphUuid = graphService.extractGraphUuid(copyOfUri);
      URI targetGraphUri = new URI(Namespaces.DATASET_NAMESPACE + datasetUuid + "/graphs/" + graphUuid);
      URI sourceDatasetUri = URI.create(Namespaces.DATASET_NAMESPACE + sourceDatasetUuid);
      URI sourceVersionUri = new URI(webConstants.getTriplestoreBaseUri() + sourceVersionUuid);
      URI sourceGraphUri = new URI(webConstants.getTriplestoreBaseUri() + sourceGraphUuid);
      URI newVersion = graphService.copy(trialverseDatasetUri, targetGraphUri, sourceDatasetUri, sourceVersionUri, sourceGraphUri);
      trialversResponse.setHeader(WebConstants.X_EVENT_SOURCE_VERSION, newVersion.toString());
      trialversResponse.setStatus(HttpStatus.OK.value());
    } else {
      throw new MethodNotAllowedException();
    }
  }
}
