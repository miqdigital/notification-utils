package com.miq.caps.utils.notification.context

import com.typesafe.config.{Config, ConfigFactory}

object SlackContext {

  private val config: Config = ConfigFactory.load("slack.conf")

  lazy val webhook: String = config.getString("slack.webhook")

  lazy val emailLookup: String = config.getString("slack.emailLookup")
  lazy val postMessage: String = config.getString("slack.postMessage")
  lazy val botToken: String = config.getString("slack.bot.token")
  lazy val email: String = config.getString("slack.test.email")
  lazy val channel: String = config.getString("slack.test.channel")

}
