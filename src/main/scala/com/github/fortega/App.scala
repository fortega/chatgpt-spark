package com.github.fortega

import com.github.fortega.service.SparkService._
import org.apache.spark.sql.functions.element_at

object App {
  def run(
      apiKey: String,
      inputPath: String,
      outputPath: String
  ) = usingSpark { spark =>
    import spark.implicits._

    val completionUdf = createCompletionUdf(apiKey)

    val input = spark.read
      .csv(inputPath)
      .toDF("question")

    val output = input
      .withColumn("completion", completionUdf($"question"))

    output
      .withColumn(
        "completion",
        element_at($"completion.value.choices.text", 1)
      )
      .write
      .csv(outputPath)
    spark.stop
    sys.exit
  }

  def main(
      cmdArgs: Array[String]
  ): Unit = cmdArgs match {
    case Array(apiKey, in, out) => run(apiKey, in, out)
  }
}
