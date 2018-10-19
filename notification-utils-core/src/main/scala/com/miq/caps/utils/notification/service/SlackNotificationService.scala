package com.miq.caps.utils.notification.service

import com.miq.caps.utils.ValidationUtils
import com.miq.caps.utils.notification.api.SlackNotificationApi
import com.miq.caps.utils.notification.context.SlackContext
import com.miq.caps.utils.notification.slack.SlackBotMessage
import com.miq.caps.{ResponseStatus, ValidResponse}
import com.typesafe.scalalogging.LazyLogging
import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.scala.ext.web.client.WebClient

import scala.concurrent.Future

class SlackNotificationService(client: WebClient)(implicit ec: VertxExecutionContext) extends SlackNotificationApi with LazyLogging {

  override def publishToWebhook(message: JsonObject, simulate: Boolean = false): Future[ResponseStatus] = {
    if (simulate) simPublishToWebhook(message)
    else {
      for {
        clientResponse <- client.postAbs(SlackContext.webhook)
          .sendJsonObjectFuture(message)
        responseString <- ValidationUtils.validateOptional(clientResponse.bodyAsString)
        slackResponse <- ValidationUtils.validateResponseAsFuture(responseString, "ok")
      } yield slackResponse
    }
  }

  private def simPublishToWebhook(message: JsonObject): Future[ResponseStatus] = {
    logger.debug("[message] {}", message.encode)
    Future(ValidResponse)
  }

  private def generateSlackMessage(message: SlackBotMessage, id: String): JsonObject = {
    new JsonObject()
      .put("channel", id)
      .put("text", message.message.text)
  }

  private def getSlackIdByEmail(email: String): Future[String] = {
    for {
      clientResponse <- client.getAbs(SlackContext.emailLookup)
        .addQueryParam("email", email)
        .putHeader("Authorization", s"Bearer ${SlackContext.botToken}")
        .sendFuture()
      responseObject <- ValidationUtils.validateOptional(clientResponse.bodyAsJsonObject)
    } yield responseObject.getJsonObject("user").getString("id")
  }

  override def publishToSlackUser(message: SlackBotMessage, simulate: Boolean = false): Future[ResponseStatus] = {
    if (simulate) simPublishToSlackUser(message)
    else {
      for {
        slackId <- getSlackIdByEmail(message.id)
        clientResponse <- client.postAbs(SlackContext.postMessage)
          .putHeader("Authorization", s"Bearer ${SlackContext.botToken}")
          .sendJsonObjectFuture(generateSlackMessage(message, slackId))
        responseObject <- ValidationUtils.validateOptional(clientResponse.bodyAsJsonObject)
        slackResponse <- ValidationUtils.validateResponseAsFuture(responseObject.getBoolean("ok"), true)
      } yield slackResponse
    }
  }

  private def simPublishToSlackUser(message: SlackBotMessage): Future[ResponseStatus] = {
    logger.debug("[message:{}] {}", message.id, message.message.text)
    Future(ValidResponse)
  }

  override def publishToSlackId(message: SlackBotMessage, simulate: Boolean = false): Future[ResponseStatus] = {
    if (simulate) simPublishToSlackId(message)
    else {
      for {
        clientResponse <- client.postAbs(SlackContext.postMessage)
          .putHeader("Authorization", s"Bearer ${SlackContext.botToken}")
          .sendJsonObjectFuture(generateSlackMessage(message, message.id))
        responseObject <- ValidationUtils.validateOptional(clientResponse.bodyAsJsonObject)
        slackResponse <- ValidationUtils.validateResponseAsFuture(responseObject.getBoolean("ok"), true)
      } yield slackResponse
    }
  }

  private def simPublishToSlackId(message: SlackBotMessage): Future[ResponseStatus] = {
    logger.debug("[message:{}] {}", message.id, message.message.text)
    Future(ValidResponse)
  }

}
