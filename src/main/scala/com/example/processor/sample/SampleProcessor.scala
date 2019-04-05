package com.example.processor.sample

import java.util

import com.streamsets.datatransformer.api.spark.{SingleInputSparkTransform, SparkSessionProvider}
import com.streamsets.pipeline.api.ConfigIssue
import com.streamsets.pipeline.spark.SparkData
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{broadcast, explode, split}

/** Sample processor
  *
  *  @constructor create a new processor
  */
class SampleProcessor extends SingleInputSparkTransform {
  private val ccTypes = List(
    ("Visa", "4"),
    ("Mastercard", "51,52,53,54"),
    ("AMEX", "34,37"),
    ("Diners Club", "300,301,302,303,304,305,36,38"),
    ("Discover", "6011,65"),
    ("JCB", "2131,1800,35")
  )
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
      ccTypeDF = ccTypes.toDF("type", "prefixes")
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
