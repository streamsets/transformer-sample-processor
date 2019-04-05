package com.example.processor.sample.config;

import com.streamsets.pipeline.api.ConfigDef;

import java.io.Serializable;

public class CardPrefixes implements Serializable {
  public CardPrefixes() {
  }

  public CardPrefixes(String type, String prefixes) {
    this.type = type;
    this.prefixes = prefixes;
  }

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "Credit Card Type",
      description = "For example: Mastercard",
      group = "SAMPLE",
      displayPosition = 10
  )
  public String type;

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "List of Prefixes",
      description = "Comma-separated list of prefixes. For example: 51,52,53",
      group = "SAMPLE",
      displayPosition = 20
  )
  public String prefixes;

  public String getPrefixes() { return prefixes; }
  public void setPrefixes(String prefixes) { this.prefixes = prefixes; }
}
