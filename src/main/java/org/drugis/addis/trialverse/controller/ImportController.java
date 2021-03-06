package org.drugis.addis.trialverse.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.drugis.addis.base.AbstractAddisCoreController;
import org.drugis.addis.trialverse.service.ClinicalTrialsImportService;
import org.drugis.addis.trialverse.service.impl.ClinicalTrialsImportError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by connor on 12-5-16.
 */
@Controller
@RequestMapping("/import")
public class ImportController extends AbstractAddisCoreController {

  private final static Logger logger = LoggerFactory.getLogger(ImportController.class);

  @Inject
  private ClinicalTrialsImportService clinicalTrialsImportService;

  @RequestMapping(value = "/{nctId}", method = RequestMethod.GET)
  @ResponseBody
  public JsonNode fetchInfo(HttpServletResponse response, @PathVariable String nctId) throws ClinicalTrialsImportError {
    logger.debug("fetch study info from clinicalTrials Importer for NTCID: " + nctId);
    return clinicalTrialsImportService.fetchInfo(nctId);
  }
}
