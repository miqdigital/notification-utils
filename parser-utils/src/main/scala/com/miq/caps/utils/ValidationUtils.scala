package com.miq.caps.utils

import com.miq.caps._
import io.circe.Decoder
import io.vertx.core.json.JsonObject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ValidationUtils {

  def validateResponse[T](response: T, expected: T): ResponseStatus = {
    if (response.equals(expected)) ValidResponse
    else InvalidResponse
  }

  def validateResponseAsFuture[T](response: T, expected: T): Future[ResponseStatus] =
    Future(validateResponse(response, expected))

  def validateJsonObjectRequest[T: Decoder](request: JsonObject): Future[JsonObject] = for {
    decoded <- JsonUtils.decodeFromJsonObjectAsFuture[T](request)
  } yield request

  def validateNonEmptyString(s: String): Future[String] = {
    if (s.isEmpty) Future.failed(InvalidStringException)
    else Future.successful(s)
  }

  def validateOptional[T](o: Option[T]): Future[T] = o match {
    case None => Future.failed(InvalidResponseException)
    case Some(s) => Future.successful(s)
  }

}
