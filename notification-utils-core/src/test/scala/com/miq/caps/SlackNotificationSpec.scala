package com.miq.caps

import com.miq.caps.generators.SlackMessageGenerator
import com.miq.caps.utils.JsonUtils
import com.miq.caps.utils.notification.context.SlackContext
import com.miq.caps.utils.notification.slack.SlackBotMessage
import com.miq.caps.utils.notification.verticle.SlackNotificationVerticle
import org.scalacheck.Prop
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{Assertions, Matchers}

import scala.concurrent.Future

class SlackNotificationSpec extends VertxSpec[SlackNotificationVerticle] with Matchers with ScalaFutures {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(10, Seconds), interval = Span(5, Seconds))

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSize = 1, sizeRange = 10, minSuccessful = 5, workers = 1)

  "SlackNotificationVerticle" should "generate string messages and simulate publish to slack" in {
    val test = Prop.forAll(SlackMessageGenerator.genNonEmptyString) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.SIMULATE_PUBLISH_STRING_TO_WEBHOOK, message)
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(3))
    Future.successful(Assertions.succeed)
  }

  it should "generate string messages and publish to slack" in {
    val test = Prop.forAll(SlackMessageGenerator.genNonEmptyString) { message =>
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
    val test = Prop.forAll(SlackMessageGenerator.genSlackMessage) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.SIMULATE_PUBLISH_JSON_OBJECT_TO_WEBHOOK, JsonUtils.encodeAsJsonObject(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(3))
    Future.successful(Assertions.succeed)
  }

  it should "generate json object messages and publish to slack" in {
    val test = Prop.forAll(SlackMessageGenerator.genSlackMessage) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.PUBLISH_JSON_OBJECT_TO_WEBHOOK, JsonUtils.encodeAsJsonObject(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(1))
    Future.successful(Assertions.succeed)
  }

  it should "generate bot json object messages and simulate publish to slack user" in {
    val test = Prop.forAll(SlackMessageGenerator.genSlackUserBotMessage) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.SIMULATE_PUBLISH_JSON_OBJECT_TO_SLACK_USER, JsonUtils.encodeAsJsonObject(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(3))
    Future.successful(Assertions.succeed)
  }

  it should "generate bot json object messages and publish to slack user" in {
    val test = Prop.forAll(SlackMessageGenerator.genSlackMessage) { message =>
      val slackMessage: SlackBotMessage = SlackBotMessage(SlackContext.email, message)
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.PUBLISH_JSON_OBJECT_TO_SLACK_USER, JsonUtils.encodeAsJsonObject(slackMessage))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(1))
    Future.successful(Assertions.succeed)
  }

  it should "generate bot json object messages and simulate publish to slack id" in {
    val test = Prop.forAll(SlackMessageGenerator.genSlackIdBotMessage) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.SIMULATE_PUBLISH_JSON_OBJECT_TO_SLACK_ID, JsonUtils.encodeAsJsonObject(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(3))
    Future.successful(Assertions.succeed)
  }

  it should "generate bot json object messages and publish to slack id" in {
    val test = Prop.forAll(SlackMessageGenerator.genSlackMessage) { message =>
      val slackMessage: SlackBotMessage = SlackBotMessage(SlackContext.channel, message)
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](SlackNotificationVerticle.PUBLISH_JSON_OBJECT_TO_SLACK_ID, JsonUtils.encodeAsJsonObject(slackMessage))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(1))
    Future.successful(Assertions.succeed)
  }

}
