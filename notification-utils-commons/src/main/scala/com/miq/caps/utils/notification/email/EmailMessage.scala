package com.miq.caps.utils.notification.email

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class EmailMessage(subject: String, recipient: String, text: String)

object EmailMessage {
  implicit val encoder: Encoder[EmailMessage] = deriveEncoder
  implicit val decoder: Decoder[EmailMessage] = deriveDecoder
}