package com.miq.caps.utils.notification.api

import com.miq.caps.ResponseStatus
import io.vertx.core.json.JsonObject

import scala.concurrent.Future

trait SlackNotificationApi {

  def publishToWebhook(message: JsonObject, simulate: Boolean): Future[ResponseStatus]

}
