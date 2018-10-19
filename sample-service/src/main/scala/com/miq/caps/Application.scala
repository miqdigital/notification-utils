package com.miq.caps

import com.miq.caps.utils.notification.verticle.SlackNotificationVerticle
import io.vertx.lang.scala.{ScalaVerticle, VertxExecutionContext}
import io.vertx.scala.core.Vertx

import scala.util.{Failure, Success}

object Application extends App {

  val vertx: Vertx = Vertx.vertx()
  implicit val ec: VertxExecutionContext = VertxExecutionContext(vertx.getOrCreateContext)

  val verticles = for {
    rest <- vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[RestVerticle])
    slack <- vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[SlackNotificationVerticle])
  } yield (rest, slack)

  verticles
    .onComplete {
      case Success((rest, slack)) =>
        println(s"verticle: rest - $rest")
        println(s"verticle: slack - $slack")
      case Failure(e) =>
        e.printStackTrace()
        sys.exit(1)
    }

  sys.addShutdownHook {
    vertx.close()
  }

}
