package com.github.fortega

import com.github.fortega.service.SparkService._
import org.apache.spark.sql.functions.trim

object App {
  lazy val completions = createCompletionUdf(apiKey = sys.env("API_KEY"))

  def main(cmdArgs: Array[String]): Unit = usingSpark { spark =>
    import spark.implicits._

    val questions = Seq(
      "could scala app ask some questions to chatgpt?",
      "tell me something cool about scala",
      "why is better to use scala with spark than pyspark?"
    ).toDF("question")

    val output =
      questions.withColumn("completion", completions($"question"))
    output.printSchema
    output
      .select(
        $"question",
        trim($"completion.value.choices.text" (0)) as "completition"
      )
      .show(false)
  }
}
