package endpoint.project

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by user on 12-Feb-16.
  */
object RequestListTest {

  def run(headers:Map[String, String], contentType:String) = {
    exec(http("Get Request List By GroupId")
      .get("/projects/${project_id}/groups/${group_id}/requests")
      .headers(headers)
      .check(status.is(200))
      .check(headerRegex("content-type", contentType).ofType[String])
      .check(jsonPath("$..id").count.greaterThan(0))
    )
  }
}
