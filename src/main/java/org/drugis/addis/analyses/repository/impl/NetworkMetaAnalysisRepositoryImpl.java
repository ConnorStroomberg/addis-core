package org.drugis.addis.analyses.repository.impl;

import org.drugis.addis.analyses.AnalysisCommand;
import org.drugis.addis.analyses.InterventionInclusion;
import org.drugis.addis.analyses.NetworkMetaAnalysis;
import org.drugis.addis.analyses.repository.NetworkMetaAnalysisRepository;
import org.drugis.addis.exception.MethodNotAllowedException;
import org.drugis.addis.exception.ResourceDoesNotExistException;
import org.drugis.addis.interventions.model.AbstractIntervention;
import org.drugis.addis.interventions.repository.InterventionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Created by connor on 6-5-14.
 */
@Repository
public class NetworkMetaAnalysisRepositoryImpl implements NetworkMetaAnalysisRepository {
  @Qualifier("emAddisCore")
  @PersistenceContext(unitName = "addisCore")
  EntityManager em;

  @Inject
  InterventionRepository interventionRepository;

  @Override
  public NetworkMetaAnalysis create(AnalysisCommand analysisCommand) throws MethodNotAllowedException, ResourceDoesNotExistException {
    NetworkMetaAnalysis networkMetaAnalysis = new NetworkMetaAnalysis(analysisCommand.getProjectId(), analysisCommand.getTitle());
    em.persist(networkMetaAnalysis);

    Collection<AbstractIntervention> interventions = interventionRepository.query(analysisCommand.getProjectId());
    Set<InterventionInclusion> interventionInclusions = new HashSet<>(interventions.size());
    for (AbstractIntervention intervention : interventions) {
      interventionInclusions.add(new InterventionInclusion(networkMetaAnalysis.getId(), intervention.getId()));
    }
    networkMetaAnalysis.updateIncludedInterventions(interventionInclusions);
    return update(networkMetaAnalysis);
  }

  @Override
  public Collection<NetworkMetaAnalysis> query(Integer projectId) {
    TypedQuery<NetworkMetaAnalysis>  query = em.createQuery("FROM NetworkMetaAnalysis a WHERE a.projectId = :projectId", NetworkMetaAnalysis.class);
      query.setParameter("projectId", projectId);
    return query.getResultList();
  }

  @Override
  public List<NetworkMetaAnalysis> queryByOutcomes(Integer projectId, List<Integer> outcomeIds) {
    if(outcomeIds == null || outcomeIds.isEmpty()) {
      return Collections.emptyList();
    } else {
      TypedQuery<NetworkMetaAnalysis> query = em.createQuery("FROM NetworkMetaAnalysis a WHERE a.projectId = :projectId AND outcomeId IN :outcomeIds", NetworkMetaAnalysis.class);
      query.setParameter("projectId", projectId);
      query.setParameter("outcomeIds", outcomeIds);
      return query.getResultList();
    }
  }

  @Override
  public NetworkMetaAnalysis update(NetworkMetaAnalysis analysis) throws ResourceDoesNotExistException, MethodNotAllowedException {
    return em.merge(analysis);
  }

  @Override
  public void setPrimaryModel(Integer analysisId, Integer modelId) {
    TypedQuery<NetworkMetaAnalysis> query = em.createQuery("FROM NetworkMetaAnalysis a WHERE a.id = :analysisId", NetworkMetaAnalysis.class);
    query.setParameter("analysisId", analysisId);
    List<NetworkMetaAnalysis> resultList = query.getResultList();
    NetworkMetaAnalysis networkMetaAnalysis = resultList.get(0);
    networkMetaAnalysis.setPrimaryModel(modelId);
    em.merge(networkMetaAnalysis);
  }
}
