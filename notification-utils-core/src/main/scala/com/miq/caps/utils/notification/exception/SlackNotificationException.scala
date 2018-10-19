package com.miq.caps.utils.notification.exception

import com.miq.caps.BaseException

trait SlackNotificationException extends BaseException

case object InvalidWebhookException extends SlackNotificationException {
  val statusCode: Int = 400
  val statusMessage: String = "Slack Webhook is unreachable"
}

case object InvalidSlackResponseException extends SlackNotificationException {
  val statusCode: Int = 500
  val statusMessage: String = "Slack did not return a valid response"
}

case class SlackUserNotFoundException(email: String) extends SlackNotificationException {
  val statusCode: Int = 404
  val statusMessage: String = s"User $email not found on your workspace"
}