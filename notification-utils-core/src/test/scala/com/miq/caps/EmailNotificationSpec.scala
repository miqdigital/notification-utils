package com.miq.caps

import com.miq.caps.generators.EmailMessageGenerator
import com.miq.caps.utils.JsonUtils
import com.miq.caps.utils.notification.context.EmailContext
import com.miq.caps.utils.notification.email.EmailMessage
import com.miq.caps.utils.notification.verticle.EmailNotificationVerticle
import io.vertx.core.json.JsonObject
import org.scalacheck.Prop
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{Assertions, Matchers}

import scala.concurrent.Future

class EmailNotificationSpec extends VertxSpec[EmailNotificationVerticle] with Matchers with ScalaFutures {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(10, Seconds), interval = Span(5, Seconds))

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSize = 1, sizeRange = 10, minSuccessful = 5, workers = 1)

  private def generateJsonObjectMessage(message: EmailMessage): JsonObject = {
    new JsonObject()
      .put("subject", message.subject)
      .put("recipient", message.recipient)
      .put("text", message.text)
  }

  "EmailNotificationVerticle" should "generate string messages and simulate send Email" in {
    val test = Prop.forAll(EmailMessageGenerator.genEmailMessage(EmailContext.sender)) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](EmailNotificationVerticle.SIMULATE_EMAIL_STRING_MESSAGE, JsonUtils.encode(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(3))
    Future.successful(Assertions.succeed)
  }

  it should "generate string messages and send Email" in {
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

  it should "generate json object messages and send Email" in {
    val test = Prop.forAll(EmailMessageGenerator.genEmailMessage(EmailContext.sender)) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](EmailNotificationVerticle.EMAIL_JSON_OBJECT, generateJsonObjectMessage(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(1))
    Future.successful(Assertions.succeed)
  }

  it should "generate json object messages and simulate send Email" in {
    val test = Prop.forAll(EmailMessageGenerator.genEmailMessage(EmailContext.sender)) { message =>
      val sender = for {
        response <- vertx.eventBus()
          .sendFuture[String](EmailNotificationVerticle.SIMULATE_EMAIL_JSON_OBJECT, generateJsonObjectMessage(message))
        decoded <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](response.body))
      } yield decoded

      if (sender.futureValue == ValidResponse) Prop.passed
      else Prop.falsified
    }
    test.check(_.withMinSuccessfulTests(3))
    Future.successful(Assertions.succeed)
  }



}
