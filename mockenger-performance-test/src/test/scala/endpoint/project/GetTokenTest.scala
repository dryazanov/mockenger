package test.scala.endpoint.project

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by user on 12-Feb-16.
  */
object GetTokenTest {

  val url = "/oauth/token?grant_type=password"
  val username = "admin@email.com"
  val password = "123456"
  val authHeader = "Basic Y2xpZW50YXBwOjEyMzQ1Ng=="

  def run(headers:Map[String, String], contentType:String) = {
    exec(http("Get Token type password")
      .post(url)
        .formParam("username", username)
        .formParam("password", password)
        .headers(Map("Authorization" -> authHeader))
      .check(status.is(200))
      .check(headerRegex("content-type", contentType).ofType[String])
      .check(jsonPath("$..access_token").exists
        .saveAs("access_token"))
    )
  }
}
