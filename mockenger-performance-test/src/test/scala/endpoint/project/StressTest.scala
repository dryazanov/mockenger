package test.scala.endpoint.project

import endpoint.project.RequestListTest
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class StressTest extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:8080")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())

	val contentType = "application/json;charset=UTF-8"
	val headers = Map("Content-Type" -> contentType)
  val headersWithToken = Map("Content-Type" -> contentType, "Authorization" -> "Bearer ${access_token}")

	val scn = scenario("RecordedSimulation")
		.exec(GetTokenTest.run(contentType))
    .randomSwitch(
      10.0 -> exec(
        ProjectListTest.run(headersWithToken, contentType),
        GroupListTest.run(headersWithToken, contentType),
        RequestListTest.run(headersWithToken, contentType)
      ),
      40.0 -> WishlistTest.run(headersWithToken, contentType)
    )

	setUp(scn.inject(atOnceUsers(100)))
//	setUp(scn.inject(atOnceUsers(10), constantUsersPerSec(10).during(5)))
//	setUp(scn.inject(splitUsers(100).into(rampUsers(10).over(10)).separatedBy(7)))
		.protocols(httpProtocol)
}