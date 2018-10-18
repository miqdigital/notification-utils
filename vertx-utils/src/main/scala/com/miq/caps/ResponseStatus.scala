package com.miq.caps

import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.semiauto._

sealed trait ResponseStatus

object ResponseStatus {
  implicit val encoder: Encoder[ResponseStatus] = deriveEnumerationEncoder
  implicit val decoder: Decoder[ResponseStatus] = deriveEnumerationDecoder
}

case object ValidResponse extends ResponseStatus
case object InvalidResponse extends ResponseStatus
