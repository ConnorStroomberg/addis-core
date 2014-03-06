package org.drugis.addis.outcomes;

import org.drugis.addis.trialverse.model.SemanticOutcome;

import javax.persistence.*;

/**
 * Created by daan on 2/20/14.
 */
@Entity
public class Outcome {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String motivation;
  private String semanticOutcomeLabel;
  private String semanticOutcomeUrl;

  public Outcome() {
  }

  public Outcome(String name, String motivation, SemanticOutcome semanticOutcome) {
    this(null, name, motivation, semanticOutcome);
  }

  public Outcome(Integer id, String name, String motivation, SemanticOutcome semanticOutcome) {
    this.id = id;
    this.name = name;
    this.motivation = motivation;
    this.semanticOutcomeLabel = semanticOutcome.getLabel();
    this.semanticOutcomeUrl = semanticOutcome.getUri();
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getMotivation() {
    return motivation;
  }

  public String getSemanticOutcomeLabel() {
    return semanticOutcomeLabel;
  }

  public String getSemanticOutcomeUrl() {
    return semanticOutcomeUrl;
  }


}
