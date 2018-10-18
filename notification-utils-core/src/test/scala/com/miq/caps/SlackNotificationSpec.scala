package com.miq.caps

import com.miq.caps.generators.SlackMessageGenerator
import com.miq.caps.utils.JsonUtils
import com.miq.caps.utils.notification.verticle.SlackNotificationVerticle
import org.scalacheck.Prop
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{Assertions, Matchers}

import scala.concurrent.Future

class SlackNotificationSpec extends VertxSpec[SlackNotificationVerticle] with Matchers with ScalaFutures {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds), interval = Span(1, Seconds))

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSize = 1, sizeRange = 10, minSuccessful = 5, workers = 1)

  "SlackNotificationVerticle" should "simulate receive messages and publish to slack" in {
    val test = Prop.forAll(SlackMessageGenerator.genStringMessage) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.SIMULATE_PUBLISH_STRING_TO_WEBHOOK, message)
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(5))
    Future.successful(Assertions.succeed)
  }

  it should "receive messages and publish to slack" in {
    val test = Prop.forAll(SlackMessageGenerator.genStringMessage) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.PUBLISH_STRING_TO_WEBHOOK, message)
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(5))
    Future.successful(Assertions.succeed)
  }

}
