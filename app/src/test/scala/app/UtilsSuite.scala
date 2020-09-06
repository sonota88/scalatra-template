package app

import org.scalatest.FunSuite
// import org.scalatest.BeforeAndAfter

// class UtilsSuite extends FunSuite with BeforeAndAfter {
class UtilsSuite extends FunSuite {

  // before { ... }
  // after { ... }

  test("getExt") {
    assert(Utils.getExt("fdsa.txt") == ".txt")
    assert(Utils.getExt("foo/fdsa.txt") == ".txt")
    assert(Utils.getExt("foo/var/fdsa.txt") == ".txt")
    assert(Utils.getExt("fdsa.FDSA.txt") == ".txt")
  }

  test("getExt - no ext") {
    assert(Utils.getExt("Makefile") == "")
  }

}
