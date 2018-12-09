package rme.builder.foundation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object LoginBuilder {

  def createRequest(rmeTxn: String, pid: String): HttpRequestBuilder = {
    http("Login /path/Login.json")
      .get("/path/Login.json")
      .header("RME_TXN_ID", rmeTxn)
      .header("wm_user", "SUPERUSER")
      .queryParam("PID", pid)
      .check(status.is(200))
      .check(jsonPath("$.RME_TOKEN").saveAs("Authorization"))
      .check(jsonPath("$.userData[0].region").saveAs("REGION"))
  }

}