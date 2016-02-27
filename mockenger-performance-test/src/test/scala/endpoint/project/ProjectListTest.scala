package test.scala.endpoint.project

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by user on 12-Feb-16.
  */
object ProjectListTest {

  def run(headers:Map[String, String], contentType:String) = {
    exec(http("Get Project List")
      .get("/projects")
      .headers(headers)
      .check(status.is(200),
        headerRegex("content-type", contentType).ofType[String],
        jsonPath("$..id").count.is(3)
      )
    )
  }
}
