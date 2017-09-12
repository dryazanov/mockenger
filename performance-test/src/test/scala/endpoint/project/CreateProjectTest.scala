package test.scala.endpoint.project


import test.scala.Config._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

/**
  * @author Dmitry Ryazanov
  */
object CreateProjectTest {

  val payload = StringBody("{\"name\":\"Project for performance tests\",\"code\":\"PRJCTFRPRFRMNCTSTS\",\"type\":\"HTTP\"}")

  def apply:ChainBuilder = {
    exec(
      http("Create project")
        .post("/api/projects")
        .body(payload)
        .headers(Map(contentTypeHeader -> contentTypeJsonUTF8))
        .check(status.is(201))
        .check(headerRegex(contentTypeHeader, contentTypeJsonUTF8).ofType[String])
        .check(jsonPath("$.id").exists.saveAs("project_id"))
        .check(jsonPath("$.code").exists.saveAs("project_code"))
    )
  }
}