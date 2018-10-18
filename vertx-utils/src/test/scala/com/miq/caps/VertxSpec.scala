package com.miq.caps

import io.vertx.lang.scala.{ScalaVerticle, VertxExecutionContext}
import io.vertx.scala.core.Vertx
import org.scalatest.prop.PropertyChecks
import org.scalatest.{AsyncFlatSpec, BeforeAndAfter}

import scala.reflect.runtime.universe._
import scala.util.{Failure, Success}

abstract class VertxSpec[T <: ScalaVerticle : TypeTag] extends AsyncFlatSpec with PropertyChecks with BeforeAndAfter {

  val vertx: Vertx = Vertx.vertx()
  implicit val vertxExecutionContext: VertxExecutionContext = VertxExecutionContext(vertx.getOrCreateContext())

  private var deploymentId: String = _

  before {
    vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[T])
      .andThen {
        case Success(id) =>
          deploymentId = id
          info(s"${ScalaVerticle.nameForVerticle[T]} has been deployed")
        case Failure(e) =>
          throw new RuntimeException(e)
      }
  }

  after {
    vertx.undeployFuture(deploymentId)
      .andThen {
        case Success(id) =>
          info(s"${ScalaVerticle.nameForVerticle[T]} has been undeployed")
        case Failure(e) =>
          throw new RuntimeException(e)
      }
  }

}