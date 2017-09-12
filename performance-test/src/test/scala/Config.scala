package test.scala

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * @author Dmitry Ryazanov
  */
object Config {

	// Use command line argument to load config file (application.conf by default)
	val conf = ConfigFactory.load(Option(System.getProperty("env")).getOrElse("application"))

	val baseUrl = conf.getString("app.baseUrl")
	val authKey = conf.getString("app.authKey")

	val httpProtocol = http.baseURL(baseUrl)
	val successfulRequestsGreaterThan = global.successfulRequests.percent.greaterThan(100)

	val contentTypeJsonUTF8 = "application/json;charset=UTF-8"
	val contentTypeXmlUTF8 = "application/xml;charset=UTF-8"
  val basicAuthorization = ("Basic " + authKey)
  val xFormUrlEncoded = """application/x-www-form-urlencoded"""

  val authorizationHeader = "Authorization"
  val contentTypeHeader = "Content-Type"
	val apiVersionHeader = "Api-version"
  val gwOrganizationIdHeader = "GW-Organization-ID"
  val gwCountryHeader = "GW-Country"

	val basicAuthHeaders = Map(authorizationHeader -> basicAuthorization, contentTypeHeader -> xFormUrlEncoded)
}
