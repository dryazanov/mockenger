package test.scala.endpoint.project

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class IntegrationTest extends Simulation {

	val conf = ConfigFactory.load()
	val baseUrl = conf.getString("app.baseUrl")

	val httpProtocol = http
		.baseURL(baseUrl)
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())

	val contentType = "application/json;charset=UTF-8"

	val scn = scenario("RecordedSimulation")
		  .exec(GetTokenTest.run(contentType))
			.exec({
				val headers = Map("Content-Type" -> contentType, "Authorization" -> "Bearer ${access_token}")
				ProjectListTest.run(headers, contentType)
			})

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}