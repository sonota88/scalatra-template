package myapp

import org.scalatra._
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.StringReader
import java.io.BufferedReader
import java.io.Writer
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.regex.Pattern
import java.util.regex.Matcher

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

class MainServlet extends ScalatraServlet with MethodOverride {

  def readStaticFile(path:String):String = {
    val myAppDir = sys.env("MY_APP_DIR")
    Utils.readFile(
      Paths.get(myAppDir, "public", path).toString()
    )
  }

  def parseRequestBody(body: String): Map[String, String] = {
    var lines =
      Utils.withReader(
        new StringReader(body),
        (r)=>{
          Utils.readAllLines(r)
        }
      ).asInstanceOf[List[String]]

    // --------------------------------

    var blocks: List[List[String]] = List()

    val boundary = lines.head.stripLineEnd
    lines = lines.tail

    var buf: List[String] = List()
    for (line <- lines) {
      if (
           line.stripLineEnd.equals(boundary)
        || line.stripLineEnd.equals(boundary + "--")
      ) {
        blocks = buf.reverse :: blocks
        buf = List()
      } else {
        buf = line :: buf
      }
    }
    blocks = blocks.reverse

    // --------------------------------

    val mutMap = scala.collection.mutable.Map[String, String]()

    for (block <- blocks) {
      val pattern = Pattern.compile("Content-Disposition: form-data; name=\"(.+)\"")
      val m: Matcher = pattern.matcher(block(0))
      m.find()
      val k: String = m.group(1)

      val rest = block.tail.tail
      var v = ""
      for (line <- rest) {
        v = v + line
      }

      mutMap.put(k, v)
    }

    // println(mutMap)
    mutMap.toMap
  }

  def appendToFile(path: String, text: String) = {
    Utils.withOutputStream(
      new FileOutputStream(new File(path), true),
      (os: OutputStream) => {
        os.write(
          text.getBytes(StandardCharsets.UTF_8)
        )
      }
    )
  }

  /*
{ "fooBar": 123, "b": {"c":456}, "d": "fdsa", "e": [1,"a"], "f": null
  , "g": [true,false], "h": 123.456, "i": new Date() }
=> JObject(
     List(
       (fooBar,JInt(123))
     , (b,JObject(List((c,JInt(456)))))
     , (d,JString(fdsa))
     , (e,JArray(List(JInt(1), JString(a))))
     , (f,JNull)
     , (g,JArray(List(JBool(true), JBool(false))))
     , (h,JDouble(123.456))
     , (i,JString(2019-11-28T20:33:39.461Z))))
   */
  def parseJsonObject(json: String): Map[String, Any] = {
    val data = org.json4s.jackson.JsonMethods.parse(json)
    println(data)

    data match {
      case jo:JObject => println("JObject")
      case ja:JArray => println("JArray")
      case _ => println("other type")
    }

    data.values.asInstanceOf[Map[String, Any]]
  }

  get("/api/sample") {
    println(multiParams)
    println(params)
    println(request.contentType)
    println(request.getClass().getName())
    // println(request.body)

    val formParams = parseRequestBody(request.body)
    println("_params (" + formParams("_params") + ")")

    appendToFile("/tmp/sample.txt", "foo\n");

    contentType = "application/json"

    val json =
      ("result" ->
        ("id" -> 123) ~
        ("a" ->
          ("b" -> "foo") ~
          ("c" -> List(1, 2, 3))
        )
      ) ~
      ("errors" ->
        List(
          // Map("trace" -> "{trace}", "msg" -> "{message}")
        )
      )

    println(compact(render(json)))
    compact(render(json))
  }

  post("/api/sample") {
    println(multiParams)
    println(params)
    println(params.get("_params"))
    // println(request.body)

    val formParams = parseRequestBody(request.body)
    println("_params (" + formParams("_params") + ")")

    val _params =
      Utils.jsonType(formParams("_params")) match {
        case "object" => parseJsonObject(formParams("_params"))
        case _ => throw new RuntimeException("invalid JSON type")
      }
    println("_params =>", _params)

    contentType = "application/json"

    val json =
      ("result" ->
        ("id" -> 456) ~
        ("a" ->
          ("b" -> "foo") ~
          ("c" -> List(1, 2, 3))
        )
      ) ~
      ("errors" ->
        List(
          Map("trace" -> "{trace}", "msg" -> "{message}")
        )
      )

    println(compact(render(json)))
    compact(render(json))
  }

  get("/") {

    val myAppDir = sys.env("MY_APP_DIR")

    contentType = "text/html"
    Utils.readFile(myAppDir + "/views/index.html")
  }

  notFound {
    contentType = null

    if (requestPath.endsWith(".js")) {
      contentType = "text/javascript"
      readStaticFile(requestPath)
    } else {
      "not found"
    }
  }
}
