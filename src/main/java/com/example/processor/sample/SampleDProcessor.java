package com.example.processor.sample;

import com.streamsets.datatransformer.api.operator.BaseTransformOperator;
import com.streamsets.datatransformer.api.operator.Operator;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.ExecutionMode;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.HideConfigs;
import com.streamsets.pipeline.api.StageDef;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

@StageDef(
    version = 1,
    label = "Sample Processor",
    description = "Simply passes its input to its output",
    icon="default.png",
    onlineHelpRefUrl = "",
    execution = {ExecutionMode.BATCH, ExecutionMode.STREAMING}
)
@ConfigGroups(Groups.class)
@HideConfigs(preconditions = true, onErrorRecord = true)
@GenerateResourceBundle
public class SampleDProcessor extends BaseTransformOperator<Dataset<Row>> {

  @Override
  public Operator<Dataset<Row>> getOperator() {
    return new SampleProcessor();
  }
}
