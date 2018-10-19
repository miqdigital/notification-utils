package com.miq.caps.utils.notification.slack

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class SlackBotMessage(id: String, message: SlackMessage)

object SlackBotMessage {
  implicit val encoder: Encoder[SlackBotMessage] = deriveEncoder
  implicit val decoder: Decoder[SlackBotMessage] = deriveDecoder
}
