package com.miq.caps.exception

import com.miq.caps.BaseException

trait TestingException extends BaseException

case object ScalacheckGeneratorException extends TestingException {
  val statusCode: Int = 500
  val statusMessage: String = "Scalacheck failed to generate Test Case"
}
