package org.drugis.addis.trialverse.model.emun;

/**
 * Created by connor on 20-8-14.
 */
public enum StudyDataType {
  BASE_LINE_CHARACTERISTICS("PopulationCharacteristic"), ENDPOINTS("Endpoint"), ADVERSE_EVENTS("AdverseEvent");

  private String tripleStoreLabel;

  StudyDataType(String tripleStoreLabel) {
    this.tripleStoreLabel = tripleStoreLabel;
  }

  @Override
  public String toString() {
    return this.tripleStoreLabel;
  }
}
