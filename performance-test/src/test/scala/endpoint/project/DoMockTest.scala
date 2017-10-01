package endpoint.project

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import test.scala.Config._

/**
  * @author Dmitry Ryazanov
  */
object DoMockTest {

  val baseUrl = "/api/HTTP/${group_code}/"
  val headersJson = Map(contentTypeHeader -> "application/json", "performance-test-header" -> "true")
  val headersXml = Map(contentTypeHeader -> "application/xml", "performance-test-header" -> "true")
  val headersSoapXml = Map(contentTypeHeader -> "application/soap+xml", "soapaction" -> "create", "performance-test-header" -> "true")
  val headersJsonTransformed = Map(contentTypeHeader -> "application/json", "performance-test-header" -> "false")
  val params = Map("a" -> "1", "b" -> "2", "c" -> "3")
  val paramsTransformed = Map("a" -> "100", "b" -> "200", "c" -> "300")

  def runForGET = {
    exec(
      http("Run GET mock-request")
        .get(baseUrl + "mock/get")
        .queryParamMap(params)
        .headers(headersJson)
        .check(status.is(201))
    )
  }

  def runForHEAD = {
    exec(
      http("Run HEAD mock-request")
        .head(baseUrl + "mock/head")
        .queryParamMap(params)
        .headers(headersJson)
        .check(status.is(200))
    )
  }

  def runForDELETE = {
    exec(
      http("Run DELETE mock-request")
        .delete(baseUrl + "mock/delete")
        .queryParamMap(params)
        .headers(headersJson)
        .check(status.is(204))
    )
  }

  def runForOPTIONS = {
    exec(
      http("Run OPTIONS mock-request")
        .options(baseUrl + "mock/options")
        .queryParamMap(params)
        .headers(headersJson)
        .check(status.is(204))
    )
  }

  def runForPOSTWithJSONBody = {
    exec(
      http("Run POST mock-request with JSON body")
        .post(baseUrl + "mock/post/with/json")
        .body(RawFileBody("mock_request_json_body.json")).asJSON
        .headers(headersJson)
        .queryParamMap(params)
        .check(status.is(201))
    )
  }

  def runForPUTWithJSONBody = {
    exec(
      http("Run PUT mock-request with JSON body")
        .put(baseUrl + "mock/put/with/json")
        .body(RawFileBody("mock_request_json_body.json")).asJSON
        .headers(headersJson)
        .queryParamMap(params)
        .check(status.is(200))
    )
  }

  def runForPATCHWithJSONBody = {
    exec(
      http("Run PATCH mock-request with JSON body")
        .patch(baseUrl + "mock/patch/with/json")
        .body(RawFileBody("mock_request_json_body.json")).asJSON
        .headers(headersJson)
        .queryParamMap(params)
        .check(status.is(200))
    )
  }

  def runForPOSTWithJSONBodyAndTransformers = {
    exec(
      http("Run POST mock-request with JSON body and transformers")
        .post(baseUrl + "mock/post/with/json/and/transformers")
        .body(RawFileBody("mock_request_json_body.json")).asJSON
        .headers(headersJsonTransformed)
        .queryParamMap(paramsTransformed)
        .check(status.is(201))
    )
  }

  def runForPOSTWithSOAPXMLBody = {
    exec(
      http("Run POST mock-request with SOAP+XML body")
        .post(baseUrl + "mock/post/with/soap/xml")
        .body(RawFileBody("mock_request_soap_xml_body.xml")).asXML
        .headers(headersSoapXml)
        .check(status.is(201))
    )
  }

  def runForPOSTWithLargeXMLBody = {
    exec(
      http("Run POST mock-request with large XML body")
        .post(baseUrl + "mock/post/with/large/xml")
        .body(RawFileBody("mock_request_large_xml_body.xml")).asXML
        .headers(headersXml)
        .check(status.is(201))
    )
  }
}