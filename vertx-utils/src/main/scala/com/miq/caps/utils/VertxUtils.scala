package com.miq.caps.utils

import com.miq.caps.BaseException
import io.vertx.lang.scala.VertxExecutionContext
import io.vertx.scala.core.eventbus.Message

import scala.concurrent.Future

object VertxUtils {

  def handleCompletion[T](r: Message[T], pipeline: Future[String])(implicit ec: VertxExecutionContext): Future[Message[String]] = {
    val result = pipeline.recover {
      case e: BaseException =>
        e.printStackTrace()
        e.statusMessage
    }

    result.flatMap(response => r.replyFuture[String](response))
  }

}
