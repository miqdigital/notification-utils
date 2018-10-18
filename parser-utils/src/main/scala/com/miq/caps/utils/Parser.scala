package com.miq.caps.utils

import io.circe._

trait Parser {
  def encode[T](v: T)(implicit ev: Encoder[T]): String
  def decode[T](v: String)(implicit ev: Decoder[T]): Either[Error, T]
}
