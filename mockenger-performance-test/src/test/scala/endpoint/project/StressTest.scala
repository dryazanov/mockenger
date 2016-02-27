package test.scala.endpoint.project

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class StressTest extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:8080")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())

	val contentType = "application/json;charset=UTF-8"
	val headers = Map("Content-Type" -> contentType)

	val scn = scenario("RecordedSimulation")
		  .randomSwitch(
				20.0 -> ProjectListTest.run(headers, contentType),
				30.0 -> GroupListTest.run(headers, contentType),
				50.0 -> WishlistTest.run(contentType)
			)

	setUp(scn.inject(atOnceUsers(100)))
//	setUp(scn.inject(atOnceUsers(10), constantUsersPerSec(10).during(5)))
//	setUp(scn.inject(splitUsers(100).into(rampUsers(10).over(10)).separatedBy(7)))
		.protocols(httpProtocol)
}