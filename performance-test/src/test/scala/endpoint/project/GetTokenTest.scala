package test.scala.endpoint.project

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by user on 12-Feb-16.
  */
object GetTokenTest {

  val url = "/oauth/token"
  val grantType = "password"
  val username = "admin@email.com"
  val password = "123456"
  val headers = Map("Authorization" -> "Basic Y2xpZW50YXBwOjEyMzQ1Ng==", "Content-Type" -> """application/x-www-form-urlencoded""")

  def run(contentType:String) = {
    exec(http("Get Token - grant type password")
      .post(url)
        .formParam("grant_type", grantType)
        .formParam("username", username)
        .formParam("password", password)
        .headers(headers)
      .check(status.is(200))
      .check(headerRegex("content-type", contentType).ofType[String])
      .check(jsonPath("$..access_token").exists.saveAs("access_token"))
    )
  }
}
