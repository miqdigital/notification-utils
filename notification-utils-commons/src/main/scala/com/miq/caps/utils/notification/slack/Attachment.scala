package com.miq.caps.utils.notification.slack

import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class Field(title: String,
                 value: String,
                 short: Boolean)

case class Confirm(title: String,
                   text: String,
                   okTest: String,
                   dismissText: String)

case class Action(name: String,
                  text: String,
                  `type`: String,
                  value: String,
                  style: Option[String] = None,
                  confirm: Option[Confirm] = None)

case class Attachment(fallback: Option[String] = None,
                      color: Option[String] = None,
                      attachmentType: Option[String] = None,
                      pretext: Option[String] = None,
                      authorName: Option[String] = None,
                      authorLink: Option[String] = None,
                      authorIcon: Option[String] = None,
                      title: Option[String] = None,
                      titleLink: Option[String] = None,
                      text: Option[String] = None,
                      actions: Seq[Action] = Seq.empty[Action],
                      fields: Seq[Field] = Seq.empty[Field],
                      imageUrl: Option[String] = None,
                      thumbUrl: Option[String] = None,
                      footer: Option[String] = None,
                      footerIcon: Option[String] = None,
                      ts: Option[Long] = None)

object Attachment {
  implicit val configuration: Configuration = Configuration.default
    .withDefaults
    .withSnakeCaseMemberNames

  implicit val fieldEncoder: Encoder[Field] = deriveEncoder
  implicit val fieldDecoder: Decoder[Field] = deriveDecoder

  implicit val confirmEncoder: Encoder[Confirm] = deriveEncoder
  implicit val confirmDecoder: Decoder[Confirm] = deriveDecoder

  implicit val actionEncoder: Encoder[Action] = deriveEncoder
  implicit val actionDecoder: Decoder[Action] = deriveDecoder

  implicit val encoder: Encoder[Attachment] = deriveEncoder
  implicit val decoder: Decoder[Attachment] = deriveDecoder
}