package rme.builder

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object CreateHttpRequest {


  def getRmeRequest(name: String, url: String) = http(name)
    .get(url)
    .header("Authorization", session => session("Authorization").as[String])
    .header("RME_TXN_ID", session => session("RME_TXN_ID").as[String])
    .header("REGION", session => session("REGION").as[String])
    .header("wm_user", session => session("wm_user").as[String])


  def postRmeRequest(name: String, url: String) = http(name)
    .post(url)
    .header("Authorization", session => session("Authorization").as[String])
    .header("RME_TXN_ID", session => session("RME_TXN_ID").as[String])
    .header("REGION", session => session("REGION").as[String])
    .header("wm_user", session => session("wm_user").as[String])


}