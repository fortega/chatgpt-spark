package com.github.fortega.model

import scala.util.Try
import scala.util.Failure
import scala.util.Success

case class UdfTry[+A](
    value: Option[A],
    error: Option[String]
)

object UdfTry {
  def apply[A](value: => A): UdfTry[A] =
    try UdfTry(value = Some(value), error = None)
    catch {
      case exception: Throwable =>
        val message = s"${exception.getClass.getName}: ${exception.getMessage}"
        UdfTry(value = None, error = Some(message))
    }
}
