package com.miq.caps.utils.notification.context

import com.typesafe.config.{Config, ConfigFactory}

object EmailContext {

  private val config: Config = ConfigFactory.load("email.conf")

  lazy val host: String = config.getString("email.smtp.host")
  lazy val port: Int = config.getInt("email.smtp.port")
  lazy val sender: String = config.getString("email.sender")
  lazy val token: String = config.getString("email.token")

}
