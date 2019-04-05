package com.example.processor.sample

import java.util

import com.example.processor.sample.config.CardPrefixes
import com.streamsets.datatransformer.api.spark.{SingleInputSparkTransform, SparkSessionProvider}
import com.streamsets.pipeline.api.ConfigIssue
import com.streamsets.pipeline.spark.SparkData
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{broadcast, explode, split}

/** Sample processor
  *
  *  @constructor create a new processor
  */
class SampleProcessor(val inputFieldName: String, val outputFieldName: String, val ccTypes: java.util.List[CardPrefixes] ) extends SingleInputSparkTransform {
  private var ccTypeDF: DataFrame = null

  /**
    * Initializes the transformer, converting the a static list of credit card type/prefix mappings into a
    * [[org.apache.spark.sql.DataFrame]].
    *
    * @return a List of any [[com.streamsets.pipeline.api.ConfigIssue]]s found by the super class constructor
    */
  override def init(): util.List[ConfigIssue] = {
    val issues = super.init()

    if (issues.size() == 0) {
      val sparkSession = SparkSessionProvider.spark()
      import sparkSession.implicits._

      // Make a DataFrame suitable for joining with the input data
      ccTypeDF = sparkSession.createDataFrame(ccTypes, classOf[CardPrefixes])
          .withColumn("prefix", explode(split($"prefixes",",")))
          .drop("prefixes")
    }

    issues
  }

  /**
    * Looks up the credit card numbers in the input [[com.streamsets.pipeline.spark.SparkData]] and sets them in the
    * output.
    *
    * @param input [[com.streamsets.pipeline.spark.SparkData]] containing a [[org.apache.spark.sql.DataFrame]]
    * @return output [[com.streamsets.pipeline.spark.SparkData]] containing output data
    */
  override def transform(input: SparkData): SparkData = {
    var df = input.get()

    // Short circuit if no incoming data
    if (df.count() == 0) return input

    val sparkSession = SparkSessionProvider.spark()
    import sparkSession.implicits._

    // Join the input data with the credit card types DataFrame where the incoming credit card number starts with
    // a prefix
    new SparkData(
      df
          .join(broadcast(ccTypeDF), $"credit_card".startsWith($"prefix"),"left_outer")
          .drop("prefix")
          .withColumnRenamed("type", "credit_card_type")
    )
  }
}
