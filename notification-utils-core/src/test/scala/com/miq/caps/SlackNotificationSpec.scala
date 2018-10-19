package com.miq.caps

import com.miq.caps.generators.SlackMessageGenerator
import com.miq.caps.utils.JsonUtils
import com.miq.caps.utils.notification.verticle.SlackNotificationVerticle
import io.vertx.core.json.JsonObject
import org.scalacheck.Prop
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{Assertions, Matchers}

import scala.concurrent.Future

class SlackNotificationSpec extends VertxSpec[SlackNotificationVerticle] with Matchers with ScalaFutures {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(10, Seconds), interval = Span(1, Seconds))

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSize = 1, sizeRange = 10, minSuccessful = 5, workers = 1)

  private def generateJsonObjectMessage(text: String): JsonObject = {
    new JsonObject()
      .put("text", text)
  }

  "SlackNotificationVerticle" should "generate string messages and simulate publish to slack" in {
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

  ignore should "generate string messages and publish to slack" in {
    val test = Prop.forAll(SlackMessageGenerator.genStringMessage) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.PUBLISH_STRING_TO_WEBHOOK, message)
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(1))
    Future.successful(Assertions.succeed)
  }

  it should "simulate json object messages and simulate publish to slack" in {
    val test = Prop.forAll(SlackMessageGenerator.genStringMessage) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.SIMULATE_PUBLISH_JSON_OBJECT_TO_WEBHOOK, generateJsonObjectMessage(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(5))
    Future.successful(Assertions.succeed)
  }

  ignore should "generate json object messages and publish to slack" in {
    val test = Prop.forAll(SlackMessageGenerator.genStringMessage) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.PUBLISH_JSON_OBJECT_TO_WEBHOOK, generateJsonObjectMessage(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(1))
    Future.successful(Assertions.succeed)
  }

}
