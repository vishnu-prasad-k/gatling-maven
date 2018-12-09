package rme

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import rme.builder.atl.ActivityBuilder
import rme.builder.foundation.LoginBuilder

class RMEIfapi extends Simulation {

  val atlDataSheet: RecordSeqFeederBuilder[String] = csv("RME_ATL_Datasheet.csv").random
  val rmeDataSheet: RecordSeqFeederBuilder[String] = csv("RME_ATL_Datasheet.csv").random

  val rampUserCount: Int = Integer.getInteger("userCount", 10).toInt
  val rampUserTime: Int = Integer.getInteger("rampUpTime", 10).toInt
  val numberSvcCalls: Int = Integer.getInteger("numberSvcCalls", rampUserCount).toInt
  val serviceNFR: Int = Integer.getInteger("nfr", 2000).toInt
  val baseURL: String = ConfigFactory.load().getString("gatling.baseUrl")

  val numberLoginBlocks: Int = 4
  val loopCount: Int = math.ceil(numberSvcCalls/rampUserCount).toInt

  val httpConf = http.baseURL(baseURL)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip,deflate,br")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .userAgentHeader("Mozilla/5/0 (Windows NT 6.1: WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36")
    .disableCaching
    .disableFollowRedirect


  val scn = scenario("RMEIfapi")
    .group("RMEIfapi") {
      feed(rmeDataSheet)
        .exec(LoginBuilder.createRequest("${RME_TXN_ID}", "${PID}"))
        .repeat(loopCount) {
          group("ATL")
          feed(atlDataSheet)
            .exec(ActivityBuilder.createRequestCifView("${ATLBU}", "${ATLCIF}"))
        }
    }

  setUp(
    scn.inject(rampUsers(rampUserCount) over rampUserTime
    ).protocols(httpConf))
    .assertions(
      forAll.responseTime.percentile3.lessThan(serviceNFR),
      global.failedRequests.percent.lessThan(5)
    )

}