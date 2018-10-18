package com.miq.caps.generators

import com.miq.caps.utils.notification.email.EmailMessage
import org.scalacheck.Gen

object EmailMessageGenerator {

  val genStringMessage: Gen[String] =
    Gen.alphaLowerStr.suchThat(_.length > 0)

  def genEmailMessage(recipient: String): Gen[EmailMessage] = for {
    subject <- genStringMessage
    text <- genStringMessage
  } yield EmailMessage(subject, recipient, text)

}
