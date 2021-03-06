package org.drugis.addis.analyses.repository;

import org.drugis.addis.analyses.AnalysisCommand;
import org.drugis.addis.analyses.NetworkMetaAnalysis;
import org.drugis.addis.exception.MethodNotAllowedException;
import org.drugis.addis.exception.ResourceDoesNotExistException;

import java.util.Collection;
import java.util.List;

/**
 * Created by connor on 6-5-14.
 */
public interface NetworkMetaAnalysisRepository {
  NetworkMetaAnalysis create(AnalysisCommand newAnalysis) throws MethodNotAllowedException, ResourceDoesNotExistException;

  Collection<NetworkMetaAnalysis> query(Integer projectId);

  List<NetworkMetaAnalysis> queryByOutcomes(Integer projectId, List<Integer> outcomeIds);

  NetworkMetaAnalysis update(NetworkMetaAnalysis analysis) throws ResourceDoesNotExistException, MethodNotAllowedException;

  void setPrimaryModel(Integer analysisId, Integer modelId);
}
