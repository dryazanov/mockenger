package test.scala.endpoint.project

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by user on 12-Feb-16.
  */
object GroupListTest {

  def run(headers:Map[String, String], contentType:String) = {
    exec(http("Get Group List By ProjectId")
      .get("/projects/557050015a672b9af6cfcb49/groups")
      .headers(headers)
      .check(status.is(200),
        headerRegex("content-type", contentType).ofType[String],
        jsonPath("$..id").count.is(4)
      )
    )
  }
}
