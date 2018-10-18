package com.miq.caps.utils

import cats.syntax.either._
import com.miq.caps.{DeserializationException, InvalidJsonObjectException}
import io.circe.CursorOp.DownField
import io.circe.{CursorOp, Decoder, DecodingFailure, Encoder}
import io.vertx.core.json.JsonObject

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object JsonUtils {

  private val parser = new JsonParser

  def encode[T: Encoder](t: T): String =
    parser.encode(t)

  def decode[T: Decoder](jsonString: String): Either[String, T] =
    parser.decode[T](jsonString).leftMap {
      case e: DecodingFailure =>
        e.history.map {
          case c: DownField => s"${c.k}: is either missing or is invalid"
          case c: CursorOp => c.toString
        }.mkString("\n")
      case e: io.circe.Error =>
        e.getMessage
    }

  def decodeAsOption[T: Decoder](jsonString: String): Option[T] = {
    parser.decode[T](jsonString).toOption
  }

  def decodeAsTry[T: Decoder](jsonString: String): Try[T] = decode[T](jsonString) match {
    case Left(errors) => Failure(DeserializationException(errors))
    case Right(t) => Success(t)
  }

  def encodeAsJsonObject[T: Encoder](t: T): JsonObject = {
    val jsonString = encode(t)
    new JsonObject(jsonString)
  }

  def decodeFromJsonObject[T: Decoder](jsonObject: JsonObject): Option[T] = {
    decodeAsOption[T](jsonObject.encode)
  }

  def decodeFromJsonObjectAsFuture[T: Decoder](jsonObject: JsonObject): Future[T] = {
    decodeFromJsonObject[T](jsonObject) match {
      case None => Future.failed(InvalidJsonObjectException)
      case Some(t) => Future.successful(t)
    }
  }

}
