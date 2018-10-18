package com.miq.caps.utils.notification.verticle

import com.miq.caps.utils.notification.email.EmailMessage
import com.miq.caps.utils.notification.service.EmailNotificationService
import com.miq.caps.utils.{JsonUtils, VertxUtils}
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

import scala.concurrent.Future

object EmailNotificationVerticle {

  val EMAIL_JSON_OBJECT = "emailJsonObject"
  val EMAIL_STRING_MESSAGE = "emailStringMessage"
  val SIMULATE_EMAIL_JSON_OBJECT = "simulateEmailJsonObject"
  val SIMULATE_EMAIL_STRING_MESSAGE = "simulateEmailStringMessage"

}

class EmailNotificationVerticle extends ScalaVerticle {

  private lazy val service: EmailNotificationService =
    new EmailNotificationService()

  private def emailJsonObjectHandler(r: Message[JsonObject]): Future[Message[String]] = {
    val pipeline = for {
      request <- JsonUtils.decodeFromJsonObjectAsFuture[EmailMessage](r.body)
      response <- service.sendEmail(request)
    } yield JsonUtils.encode(response)

    VertxUtils.handleCompletion[JsonObject](r, pipeline)
  }

  private def emailStringMessageHandler(r: Message[String]): Future[Message[String]] = {
    val pipeline = for {
      request <- Future.fromTry(JsonUtils.decodeAsTry[EmailMessage](r.body))
      response <- service.sendEmail(request)
    } yield JsonUtils.encode(response)

    VertxUtils.handleCompletion[String](r, pipeline)
  }

  private def simulateEmailJsonObjectHandler(r: Message[JsonObject]): Future[Message[String]] = {
    val pipeline = for {
      request <- JsonUtils.decodeFromJsonObjectAsFuture[EmailMessage](r.body)
      response <- service.sendEmail(request, simulate = true)
    } yield JsonUtils.encode(response)

    VertxUtils.handleCompletion[JsonObject](r, pipeline)
  }

  private def simulateEmailStringMessageHandler(r: Message[String]): Future[Message[String]] = {
    val pipeline = for {
      request <- Future.fromTry(JsonUtils.decodeAsTry[EmailMessage](r.body))
      response <- service.sendEmail(request, simulate = true)
    } yield JsonUtils.encode(response)

    VertxUtils.handleCompletion[String](r, pipeline)
  }

  override def startFuture(): Future[_] = {
    vertx.eventBus().consumer[JsonObject](EmailNotificationVerticle.EMAIL_JSON_OBJECT)
      .handler(emailJsonObjectHandler)
      .completionFuture()
    vertx.eventBus().consumer[String](EmailNotificationVerticle.EMAIL_STRING_MESSAGE)
      .handler(emailStringMessageHandler)
      .completionFuture()
    vertx.eventBus().consumer[JsonObject](EmailNotificationVerticle.SIMULATE_EMAIL_JSON_OBJECT)
      .handler(simulateEmailJsonObjectHandler)
      .completionFuture()
    vertx.eventBus().consumer[String](EmailNotificationVerticle.SIMULATE_EMAIL_STRING_MESSAGE)
      .handler(simulateEmailStringMessageHandler)
      .completionFuture()
  }

}
