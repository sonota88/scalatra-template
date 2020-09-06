package myapp

import org.scalatra.test.scalatest._

class MainServletTests extends ScalatraFunSuite {

  addServlet(classOf[MainServlet], "/*")

  // test("GET / on MyScalatraServlet should return status 200") {
  //   get("/") {
  //     status should equal (200)
  //   }
  // }

}
