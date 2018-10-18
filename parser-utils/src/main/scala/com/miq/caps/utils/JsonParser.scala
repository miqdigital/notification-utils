package com.miq.caps.utils

import io.circe.parser.{decode => parse}
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Error, Printer}

class JsonParser extends Parser {
  private val printer: Printer = Printer.spaces2.copy(dropNullValues = true)

  override def encode[T](v: T)(implicit ev: Encoder[T]): String = v.asJson.pretty(printer)
  override def decode[T](v: String)(implicit ev: Decoder[T]): Either[Error, T] = parse[T](v)
}
