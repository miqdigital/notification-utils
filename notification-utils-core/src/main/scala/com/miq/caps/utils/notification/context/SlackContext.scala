package com.miq.caps.utils.notification.context

import com.typesafe.config.{Config, ConfigFactory}

object SlackContext {

  private val config: Config = ConfigFactory.load("slack.conf")

  lazy val webhook: String = config.getString("slack.webhook")

}
