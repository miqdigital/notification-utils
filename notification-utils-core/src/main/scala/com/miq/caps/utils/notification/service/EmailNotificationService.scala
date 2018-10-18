package com.miq.caps.utils.notification.service

import com.miq.caps.utils.notification.api.EmailNotificationApi
import com.miq.caps.utils.notification.context.EmailContext
import com.miq.caps.utils.notification.email.EmailMessage
import com.miq.caps.{ResponseStatus, ValidResponse}
import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.mail.{Email, SimpleEmail}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EmailNotificationService() extends EmailNotificationApi with LazyLogging {

  override def sendEmail(message: EmailMessage, simulate: Boolean = false): Future[ResponseStatus] = {
    if (simulate) simSendEmail(message)
    else {
      val email = createEmail(message)
      for {
        emailResponse <- Future(email.send)
      } yield ValidResponse
    }
  }

  private def simSendEmail(message: EmailMessage): Future[ResponseStatus] = {
    val email = createEmail(message)
    logger.debug("[email] {}", email.getSubject)
    Future(ValidResponse)
  }

  private def createEmail(message: EmailMessage): Email = {
    val email = new SimpleEmail()
    email.setHostName(EmailContext.host)
    email.setSmtpPort(EmailContext.port)
    email.setAuthentication(EmailContext.sender, EmailContext.token)
    email.setSSLOnConnect(true)
    email.setFrom(EmailContext.sender)
    email.addTo(message.recipient)
    email.setSubject(message.subject)
    email.setMsg(message.text)
  }

}