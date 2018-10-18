package com.miq.caps

trait BaseException extends RuntimeException {

  def statusCode: Int
  def statusMessage: String

}

case object InvalidStringException extends BaseException {
  val statusCode: Int = 400
  val statusMessage: String = "String cannot be empty"
}

case object InvalidJsonObjectException extends BaseException {
  val statusCode: Int = 400
  val statusMessage: String = "Json Object cannot be deserialized"
}

case object InvalidResponseException extends BaseException {
  val statusCode: Int = 500
  val statusMessage: String = "Response cannot be empty"
}

case class DeserializationException(errors: String) extends BaseException {
  val statusCode: Int = 400
  val statusMessage: String = errors
}