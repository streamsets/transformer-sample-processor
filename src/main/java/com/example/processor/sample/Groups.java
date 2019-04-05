package com.example.processor.sample;

import com.streamsets.pipeline.api.Label;

public enum Groups implements Label {
  SAMPLE ("Sample"),
  ;

  private String label;

  Groups(String label) {
    this.label = label;
  }

  @Override
  public String getLabel() {
    return label;
  }
}
