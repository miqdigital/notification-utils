package com.miq.caps.generators

import com.miq.caps.utils.notification.slack.SlackMessage
import org.scalacheck.Gen

object SlackMessageGenerator {

  val genStringMessage: Gen[String] =
    Gen.alphaLowerStr.suchThat(_.length > 0)

  val genSlackMessage: Gen[SlackMessage] = for {
    text <- genStringMessage
  } yield SlackMessage(text)

}
