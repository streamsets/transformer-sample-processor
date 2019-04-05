package com.example.processor.sample.config;

import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ListBeanModel;

import java.util.ArrayList;
import java.util.List;

public class SampleConfigBean {
  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "Input Field Name",
      description = "Field with the credit card number",
      displayPosition = 10,
      group = "SAMPLE"
  )
  @ListBeanModel
  public String inputFieldName = "";

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "Output Field Name",
      description = "Field for the credit card type",
      displayPosition = 20,
      group = "SAMPLE"
  )
  @ListBeanModel
  public String outputFieldName = "";

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      label = "Card Prefixes",
      displayPosition = 30,
      group = "SAMPLE"
  )
  @ListBeanModel
  public List<CardPrefixes> cardTypes = new ArrayList<>();
}