package com.miq.caps.utils.notification.service

import com.miq.caps.{ResponseStatus, ValidResponse}
import com.miq.caps.utils.ValidationUtils
import com.miq.caps.utils.notification.api.SlackNotificationApi
import com.miq.caps.utils.notification.context.SlackContext
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
        responseString <- ValidationUtils.validateOptionalString(clientResponse.bodyAsString)
        slackResponse <- ValidationUtils.validateResponseAsFuture(responseString, "ok")
      } yield slackResponse
    }
  }

  def simPublishToWebhook(message: JsonObject): Future[ResponseStatus] = {
    logger.debug("[message] {}", message.encode)
    Future(ValidResponse)
  }

}
