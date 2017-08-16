package test.scala.endpoint.project

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
  * Created by user on 12-Feb-16.
  */
object WishlistTest {

  def run(headers:Map[String, String], contentType:String) = {
    val params = Map("a" -> 2, "b" -> 2)
    val additionalHeaders = Map("Content-Type" -> contentType, "Test-header" -> "test1")
//    val mergedHeaders = headers ++ additionalHeaders.map{ case (k, v) => k -> (v + headers.getOrElse(k, "")) }

    exec(http("Get Wishlist with Parameters")
      .post("/REST/5571f383ab6b8c9a859507a7/customer/201889/wishlist")
      .queryParamMap(params)
      .headers(additionalHeaders)
      .body(RawFileBody("GetWishlist.json")).asJSON
      .check(status.is(200),
        jsonPath("$.valid").is("ok"),
        jsonPath("$.mock").is("4"))
    )
  }
}
