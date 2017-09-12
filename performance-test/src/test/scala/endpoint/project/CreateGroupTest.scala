package endpoint.project

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import test.scala.Config._

/**
  * @author Dmitry Ryazanov
  */
object CreateGroupTest {

  val requestName = "Create group"
  val groupCode = "GRPFRPRFRMNCTSTS"
  val url = "/api/projects/${project_code}/groups"
  val headers = Map(contentTypeHeader -> contentTypeJsonUTF8)

  def apply:ChainBuilder = {
    exec(session => session.set("group_code", groupCode))
      .exec(
        http(requestName)
      .post(url)
      .body(ElFileBody("create_group.json")).asJSON
      .headers(headers)
      .check(status.is(201))
      .check(headerRegex(contentTypeHeader, contentTypeJsonUTF8).ofType[String])
      .check(jsonPath("$.id").exists.saveAs("group_id"))
      .check(jsonPath("$.code").exists.saveAs("group_code"))
    )
  }
}