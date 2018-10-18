package com.miq.caps.utils.notification.api

import com.miq.caps.ResponseStatus
import com.miq.caps.utils.notification.email.EmailMessage

import scala.concurrent.Future

trait EmailNotificationApi {

  def sendEmail(message: EmailMessage, simulate: Boolean): Future[ResponseStatus]

}
