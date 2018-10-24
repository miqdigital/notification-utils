package com.miq.caps

import com.miq.caps.utils.{JsonUtils, ValidationUtils}
import com.miq.caps.utils.notification.slack.{Action, Attachment, SlackBotMessage, SlackMessage}
import com.miq.caps.utils.notification.verticle.SlackNotificationVerticle
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.http.HttpServer
import io.vertx.scala.ext.web.{Router, RoutingContext}
import io.vertx.scala.ext.web.handler.BodyHandler

import scala.concurrent.Future
import scala.util.{Failure, Success}

object RestVerticle {

  val PING_MESSAGE: String = "test-service says hi"

}

class RestVerticle extends ScalaVerticle {

  private def handlePing(r: RoutingContext) = {
    val pipeline = for {
      email <- ValidationUtils.validateOptional(r.queryParams().get("email"))
      message = SlackBotMessage(email, SlackMessage(RestVerticle.PING_MESSAGE, Seq(Attachment(text = Some("dummy"), attachmentType = Some("default"), actions = Seq(Action("terminate", "dum", "button", "yes"))))))
      sendSlackMessage <- vertx.eventBus().sendFuture[String](SlackNotificationVerticle.PUBLISH_JSON_OBJECT_TO_SLACK_USER, JsonUtils.encodeAsJsonObject(message))
      responseStatus <- Future.fromTry(JsonUtils.decodeAsTry[ResponseStatus](sendSlackMessage.body))
    } yield JsonUtils.encode(responseStatus)

    pipeline.onComplete {
      case Failure(e: BaseException) =>
        r.response().setStatusCode(e.statusCode).end(e.statusMessage)
      case Failure(e) =>
        e.printStackTrace()
        r.response().setStatusCode(500).end(e.getMessage)
      case Success(status) =>
        r.response().setStatusCode(200).end(status)
    }
  }

  override def startFuture(): Future[_] = {
    val server: HttpServer = vertx.createHttpServer()
    val router: Router = Router.router(vertx)

    router.get("/ping").handler(handlePing)

    server.requestHandler(router accept _)
      .listenFuture(8000)
  }

}
