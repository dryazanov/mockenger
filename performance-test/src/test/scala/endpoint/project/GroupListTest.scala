package test.scala.endpoint.project

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by user on 12-Feb-16.
  */
object GroupListTest {

  def run(headers:Map[String, String], contentType:String) = {
    exec(http("Get Group List By ProjectId")
      .get("/projects/${project_id}/groups")
      .headers(headers)
      .check(status.is(200))
      .check(headerRegex("content-type", contentType).ofType[String])
      .check(jsonPath("$[0].id").exists.saveAs("group_id"))
    )
  }
}
