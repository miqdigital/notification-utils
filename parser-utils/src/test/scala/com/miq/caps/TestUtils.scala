package com.miq.caps

import com.miq.caps.utils.JsonUtils
import io.circe.Encoder

object TestUtils {

  def display[T: Encoder](t: T, response: String, verbose: Boolean = false): Unit = {
    if (verbose) {
      println("--- REQUEST ---")
      println(JsonUtils.encode(t))
      println("---RESPONSE")
      println(response)
    }
  }

}
