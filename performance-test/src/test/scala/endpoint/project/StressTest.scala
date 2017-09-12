package test.scala.endpoint.project

import endpoint.project.CreateGroupTest.groupCode
import endpoint.project.{CreateGroupTest, CreateMockRequestTest, DoMockTest}
import test.scala.Config._
import io.gatling.core.Predef.{exec, _}


/**
  * @author Dmitry Ryazanov
  */
class StressTest extends Simulation {
  val scenarios = List(
    scenario("Prepare test data")
      .exec(CreateProjectTest.apply)
      .exec(CreateGroupTest.apply)
      .exec(CreateMockRequestTest.createGETRequest)
      .exec(CreateMockRequestTest.createHEADRequest)
      .exec(CreateMockRequestTest.createDELETERequest)
      .exec(CreateMockRequestTest.createOPTIONSRequest)
      .exec(CreateMockRequestTest.createPOSTRequestWithJSONBody)
      .exec(CreateMockRequestTest.createPUTRequestWithJSONBody)
      .exec(CreateMockRequestTest.createPATCHRequestWithJSONBody)
      .exec(CreateMockRequestTest.createPOSTRequestWithJSONBodyAndTransformers)
      .exec(CreateMockRequestTest.createPOSTRequestWithSOAPXMLBody)
      .exec(CreateMockRequestTest.createPOSTRequestWithLargeXMLBody)
      .inject(atOnceUsers(1)),

    scenario("Run stress tests")
      .exec(session => session.set("group_code", groupCode))
      .pause(5)
      .randomSwitch(
        10.0 -> exec(
          DoMockTest.runForGET
        ),
        10.0 -> exec(
          DoMockTest.runForHEAD
        ),
        10.0 -> exec(
          DoMockTest.runForDELETE
        ),
        10.0 -> exec(
          DoMockTest.runForOPTIONS
        ),
        10.0 -> exec(
          DoMockTest.runForPOSTWithJSONBody
        ),
        10.0 -> exec(
          DoMockTest.runForPUTWithJSONBody
        ),
        10.0 -> exec(
          DoMockTest.runForPATCHWithJSONBody
        ),
        10.0 -> exec(
          DoMockTest.runForPOSTWithJSONBodyAndTransformers
        ),
        10.0 -> exec(
          DoMockTest.runForPOSTWithSOAPXMLBody
        ),
        10.0 -> exec(
          DoMockTest.runForPOSTWithLargeXMLBody
        )
      )
      .inject(
        constantUsersPerSec(50) during(2*60*60), //2 hours
        rampUsersPerSec(5) to (500) during(60*60), // 1 hour
        constantUsersPerSec(100) during(2*60*60), //2 hours
      )
  )

  setUp(scenarios).protocols(httpProtocol)
}