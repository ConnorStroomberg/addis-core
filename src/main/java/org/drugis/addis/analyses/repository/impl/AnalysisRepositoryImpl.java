package org.drugis.addis.analyses.repository.impl;

import org.drugis.addis.analyses.AbstractAnalysis;
import org.drugis.addis.analyses.repository.AnalysisRepository;
import org.drugis.addis.exception.ResourceDoesNotExistException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by daan on 7-5-14.
 */
@Repository
public class AnalysisRepositoryImpl implements AnalysisRepository {
  @Qualifier("emAddisCore")
  @PersistenceContext(unitName = "addisCore")
  EntityManager em;

  @Override
  public AbstractAnalysis get(Integer analysisId) throws ResourceDoesNotExistException {
    AbstractAnalysis analysis = em.find(AbstractAnalysis.class, analysisId);
    if (analysis == null) {
      throw new ResourceDoesNotExistException();
    }
    return analysis;

  }

  @Override
  public List<AbstractAnalysis> query(Integer projectId) {
    TypedQuery<AbstractAnalysis> query = em.createQuery("FROM AbstractAnalysis " +
            "a WHERE a.projectId = :projectId", AbstractAnalysis.class);
    query.setParameter("projectId", projectId);
    return query.getResultList();
  }

}
