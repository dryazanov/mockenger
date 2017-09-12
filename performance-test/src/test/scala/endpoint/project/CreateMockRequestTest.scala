package endpoint.project

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import test.scala.Config._

/**
  * @author Dmitry Ryazanov
  */
object CreateMockRequestTest {

  val url = "/api/projects/${project_code}/groups/${group_code}/requests"
  val headers = Map(contentTypeHeader -> contentTypeJsonUTF8)

  def createGETRequest = createRequest("GET", "", "create_get_mock.json")
  def createHEADRequest = createRequest("HEAD", "", "create_head_mock.json")
  def createDELETERequest = createRequest("DELETE", "", "create_delete_mock.json")
  def createOPTIONSRequest = createRequest("OPTIONS", "", "create_options_mock.json")
  def createPOSTRequestWithJSONBody = createRequest("POST", " with JSON body", "create_post_mock_with_json_body.json")
  def createPUTRequestWithJSONBody = createRequest("PUT", " with JSON body", "create_put_mock_with_json_body.json")
  def createPATCHRequestWithJSONBody = createRequest("PATCH", " with JSON body", "create_patch_mock_with_json_body.json")
  def createPOSTRequestWithSOAPXMLBody = createRequest("POST", " with SOAP+XML body", "create_post_mock_with_soap_xml_body.json")
  def createPOSTRequestWithLargeXMLBody = createRequest("POST", " with large XML body", "create_post_mock_with_large_xml_body.json")
  def createPOSTRequestWithJSONBodyAndTransformers = createRequest("POST", " with JSON body and transformers",
    "create_post_mock_with_json_body_and_transformers.json")

  private def createRequest(requestType:String, extraInfo:String, fileName:String) = {
    exec(
      http("Create " + requestType + " mock-request" + extraInfo)
        .post(url)
        .body(ElFileBody(fileName)).asJSON
        .headers(headers)
        .check(status.is(201))
        .check(headerRegex(contentTypeHeader, contentTypeJsonUTF8).ofType[String])
    )
  }
}