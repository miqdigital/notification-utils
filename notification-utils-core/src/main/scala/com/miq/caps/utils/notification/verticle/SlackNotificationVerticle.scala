package com.miq.caps.utils.notification.verticle

import com.miq.caps.BaseException
import com.miq.caps.utils.{JsonUtils, ValidationUtils, VertxUtils}
import com.miq.caps.utils.notification.service.SlackNotificationService
import com.miq.caps.utils.notification.slack.SlackMessage
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message
import io.vertx.scala.ext.web.client.{WebClient, WebClientOptions}

import scala.concurrent.Future

object SlackNotificationVerticle {

  val PUBLISH_JSON_OBJECT_TO_WEBHOOK = "publishJsonObjectToWebhook"
  val PUBLISH_STRING_TO_WEBHOOK = "publishStringToWebhook"
  val SIMULATE_PUBLISH_JSON_OBJECT_TO_WEBHOOK = "simulatePublishJsonObjectToWebhook"
  val SIMULATE_PUBLISH_STRING_TO_WEBHOOK = "simulatePublishStringToWebhook"

}

class SlackNotificationVerticle extends ScalaVerticle {

  private lazy val service: SlackNotificationService = {
    val clientOptions: WebClientOptions = WebClientOptions()
    val client: WebClient = WebClient.create(vertx, clientOptions)
    new SlackNotificationService(client)
  }

  private def publishJsonObjectToWebhookHandler(r: Message[JsonObject]): Future[Message[String]] = {
    val pipeline = for {
      request <- ValidationUtils.validateJsonObjectRequest[SlackMessage](r.body)
      response <- service.publishToWebhook(request)
    } yield JsonUtils.encode(response)

    VertxUtils.handleCompletion[JsonObject](r, pipeline)
  }

  private def publishStringToWebhookHandler(r: Message[String]): Future[Message[String]] = {
    val pipeline = for {
      messageString <- ValidationUtils.validateNonEmptyString(r.body)
      message = SlackMessage(messageString)
      request = JsonUtils.encodeAsJsonObject(message)
      response <- service.publishToWebhook(request)
    } yield JsonUtils.encode(response)

    VertxUtils.handleCompletion[String](r, pipeline)
  }

  private def simulatePublishJsonObjectToWebhookHandler(r: Message[JsonObject]): Future[Message[String]] = {
    val pipeline = for {
      request <- ValidationUtils.validateJsonObjectRequest[SlackMessage](r.body)
      response <- service.publishToWebhook(request, simulate = true)
    } yield JsonUtils.encode(response)

    VertxUtils.handleCompletion[JsonObject](r, pipeline)
  }

  private def simulatePublishStringToWebhookHandler(r: Message[String]): Future[Message[String]] = {
    val pipeline = for {
      messageString <- ValidationUtils.validateNonEmptyString(r.body)
      message = SlackMessage(messageString)
      request = JsonUtils.encodeAsJsonObject(message)
      response <- service.publishToWebhook(request, simulate = true)
    } yield JsonUtils.encode(response)

    VertxUtils.handleCompletion[String](r, pipeline)
  }

  override def startFuture(): Future[_] = {
    vertx.eventBus().consumer[JsonObject](SlackNotificationVerticle.PUBLISH_JSON_OBJECT_TO_WEBHOOK)
      .handler(publishJsonObjectToWebhookHandler)
      .completionFuture()
    vertx.eventBus().consumer[String](SlackNotificationVerticle.PUBLISH_STRING_TO_WEBHOOK)
      .handler(publishStringToWebhookHandler)
      .completionFuture()
    vertx.eventBus().consumer[JsonObject](SlackNotificationVerticle.SIMULATE_PUBLISH_JSON_OBJECT_TO_WEBHOOK)
      .handler(simulatePublishJsonObjectToWebhookHandler)
      .completionFuture()
    vertx.eventBus().consumer[String](SlackNotificationVerticle.SIMULATE_PUBLISH_STRING_TO_WEBHOOK)
      .handler(simulatePublishStringToWebhookHandler)
      .completionFuture()
  }

}
