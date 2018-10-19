package com.miq.caps.generators

import com.miq.caps.utils.notification.slack.{SlackBotMessage, SlackMessage}
import org.scalacheck.Gen

object SlackMessageGenerator {

  val genNonEmptyString: Gen[String] = Gen.alphaLowerStr.suchThat(_.length > 0)

  val genSlackMessage: Gen[SlackMessage] = for {
    text <- genNonEmptyString
  } yield SlackMessage(text)

  val genEmail: Gen[String] = for {
    username <- genNonEmptyString
    domain <- genNonEmptyString
  } yield s"$username@$domain.com"

  val genSlackUserBotMessage: Gen[SlackBotMessage] = for {
    id <- genEmail
    message <- genSlackMessage
  } yield SlackBotMessage(id, message)

  val genSlackIdBotMessage: Gen[SlackBotMessage] = for {
    id <- genNonEmptyString
    message <- genSlackMessage
  } yield SlackBotMessage(id, message)

}
