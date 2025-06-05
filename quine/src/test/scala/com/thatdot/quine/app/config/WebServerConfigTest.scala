package com.thatdot.quine.app.config


import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should

import com.thatdot.quine.util.{Host, Port}

class WebServerConfigTest extends AnyFunSuite with should.Matchers {
  
  test("WebServerBindConfig should use baseUrl when provided") {
    val configWithBaseUrl = WebServerBindConfig(
      baseUrl = Some("https://quine.example.com/webapp")
    )
    
    val url = configWithBaseUrl.guessResolvableUrl
    url.toString shouldEqual "https://quine.example.com/webapp"
  }
  
  test("WebServerBindConfig should construct URL from host/port when baseUrl is not provided") {
    val configWithoutBaseUrl = WebServerBindConfig(
      address = Host("127.0.0.1"),
      port = Port(8080),
      baseUrl = None
    )
    
    val url = configWithoutBaseUrl.guessResolvableUrl
    url.toString shouldEqual "http://127.0.0.1:8080"
  }
  
  test("WebServerBindConfig should use localhost for wildcard bindings") {
    val wildcardConfig = WebServerBindConfig(
      address = Host("0.0.0.0"),
      port = Port(8080),
      baseUrl = None
    )
    
    val url = wildcardConfig.guessResolvableUrl
    url.toString should startWith("http://127.0.0.1:8080")
  }
  
  test("baseUrl with subpath should be respected") {
    val configWithSubpath = WebServerBindConfig(
      baseUrl = Some("https://example.org/quine/webapp")
    )
    
    val url = configWithSubpath.guessResolvableUrl
    url.toString shouldEqual "https://example.org/quine/webapp"
  }
  
  test("WebserverAdvertiseConfig should use baseUrl when provided") {
    val configWithBaseUrl = WebserverAdvertiseConfig(
      address = Host("example.com"),
      port = Port(8080),
      baseUrl = Some("https://quine.example.com/webapp")
    )
    
    val url = configWithBaseUrl.url("http")
    url.toString shouldEqual "https://quine.example.com/webapp"
  }
  
  test("WebserverAdvertiseConfig should construct URL from host/port when baseUrl is not provided") {
    val configWithoutBaseUrl = WebserverAdvertiseConfig(
      address = Host("example.com"),
      port = Port(8080),
      baseUrl = None
    )
    
    val url = configWithoutBaseUrl.url("https")
    url.toString shouldEqual "https://example.com:8080"
  }
  
  test("WebserverAdvertiseConfig should respect the protocol parameter when baseUrl is not provided") {
    val config = WebserverAdvertiseConfig(
      address = Host("example.com"),
      port = Port(8080)
    )
    
    val httpUrl = config.url("http")
    httpUrl.toString shouldEqual "http://example.com:8080"
    
    val httpsUrl = config.url("https")
    httpsUrl.toString shouldEqual "https://example.com:8080"
  }
}