package com.github.fortega

import com.github.fortega.service.SparkService._
import org.apache.spark.sql.functions.element_at

object App {
  def run(
      inputPath: String,
      outputPath: String
  ) = usingSpark { spark =>
    import spark.implicits._

    val completionUdf = createCompletionUdf(apiKey = sys.env("API_KEY"))

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
      .option("sep", ":\n")
      .csv(outputPath)
    spark.stop
    sys.exit
  }

  def main(
      cmdArgs: Array[String]
  ): Unit = cmdArgs match {
    case Array(in, out) => run(in, out)
  }
}
