package com.miq.caps.utils.notification.api

import com.miq.caps.ResponseStatus
import com.miq.caps.utils.notification.slack.SlackBotMessage
import io.vertx.core.json.JsonObject

import scala.concurrent.Future

trait SlackNotificationApi {

  def publishToWebhook(message: JsonObject, simulate: Boolean): Future[ResponseStatus]

  def publishToSlackUser(message: SlackBotMessage, simulate: Boolean): Future[ResponseStatus]

  def publishToSlackId(message: SlackBotMessage, simulate: Boolean): Future[ResponseStatus]

}
