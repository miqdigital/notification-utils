package com.miq.caps

import com.miq.caps.generators.EmailMessageGenerator
import com.miq.caps.utils.JsonUtils
import com.miq.caps.utils.notification.context.EmailContext
import com.miq.caps.utils.notification.verticle.EmailNotificationVerticle
import org.scalacheck.Prop
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{Assertions, Matchers}

import scala.concurrent.Future

class EmailNotificationSpec extends VertxSpec[EmailNotificationVerticle] with Matchers with ScalaFutures {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(10, Seconds), interval = Span(1, Seconds))

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSize = 1, sizeRange = 10, minSuccessful = 5, workers = 1)

  "EmailNotificationVerticle" should "simulate receive messages and send Email" in {
    val test = Prop.forAll(EmailMessageGenerator.genEmailMessage(EmailContext.sender)) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](EmailNotificationVerticle.SIMULATE_EMAIL_STRING_MESSAGE, JsonUtils.encode(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(5))
    Future.successful(Assertions.succeed)
  }

  it should "receive messages and send Email" in {
    val test = Prop.forAll(EmailMessageGenerator.genEmailMessage(EmailContext.sender)) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](EmailNotificationVerticle.EMAIL_STRING_MESSAGE, JsonUtils.encode(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(1))
    Future.successful(Assertions.succeed)
  }

}
