package com.example.processor.sample

import java.util

import com.streamsets.datatransformer.api.spark.SingleInputSparkTransform
import com.streamsets.pipeline.api.ConfigIssue
import com.streamsets.pipeline.spark.SparkData

/** Sample processor
  *
  *  @constructor create a new processor
  */
class SampleProcessor extends SingleInputSparkTransform {
  /**
    * Initializes the processor.
    *
    * @return a List of any [[com.streamsets.pipeline.api.ConfigIssue]]s found by the super class constructor
    */
  override def init(): util.List[ConfigIssue] = {
    val issues = super.init()

    if (issues.size() == 0) {
      // Perform any initialization
    }

    issues
  }

  /**
    * Transforms the input [[com.streamsets.pipeline.spark.SparkData]] into the
    * output.
    *
    * @param input [[com.streamsets.pipeline.spark.SparkData]] containing a [[org.apache.spark.sql.DataFrame]]
    * @return output [[com.streamsets.pipeline.spark.SparkData]] containing output data
    */
  override def transform(input: SparkData): SparkData = {
    var df = input.get()

    // Short circuit if no incoming data
    if (df.count() == 0) return input

    // Apply required operations on the DataFrame before returning it in a
    // new SparkData object
    new SparkData(
      df
    )
  }
}
