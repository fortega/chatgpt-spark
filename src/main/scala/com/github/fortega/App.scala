package com.github.fortega

import com.github.fortega.service.SparkService._

object App {

  private def main(cmdArgs: Array[String]): Unit = usingSpark { spark =>
    import spark.implicits._

    val completitions = createCompletitionsUdf(apiKey = sys.env("API_KEY"))
    val input = List("scala", "java", "python")
      .map(lang => s"the cool stuff about $lang language is")
      .toDF("input")
    input.withColumn("response", completitions($"input")).show(false)
  }

}
