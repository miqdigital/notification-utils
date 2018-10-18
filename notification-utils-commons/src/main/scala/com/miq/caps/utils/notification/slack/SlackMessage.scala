package com.miq.caps.utils.notification.slack

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class SlackMessage(text: String)

object SlackMessage {
  implicit val encoder: Encoder[SlackMessage] = deriveEncoder
  implicit val decoder: Decoder[SlackMessage] = deriveDecoder
}