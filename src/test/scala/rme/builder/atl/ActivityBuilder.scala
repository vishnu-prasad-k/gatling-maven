package rme.builder.atl

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import rme.builder.CreateHttpRequest

object ActivityBuilder {

  def createRequestCifView(atlBU: String, atlCIF: String) = CreateHttpRequest.getRmeRequest(
    "Activity_CifView /path/Activity.json",
    "/path/Activity.json")
    .queryParam("bu", atlBU)
    .queryParam("cif", atlCIF)
    .check(status.is(200))

}