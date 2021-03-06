package net.hearthstats.config

import scala.io.Source
import java.io._
import grizzled.slf4j.Logging

/**
 * Represents information about the HearthStats Companion application, such a version and location.
 */
object Application extends Logging {

  def version: String = {
    val source = Source.fromURL(getClass.getResource("/version"))
    source.mkString.trim
  }

  def copyFileFromJarTo(jarPath: String, outPath: String) {
    val stream: InputStream = getClass.getResourceAsStream(jarPath)
    if (stream == null) {
      error(s"Exception: Unable to load file from JAR: $jarPath")
      System.exit(1)
    } else {
      var resStreamOut: OutputStream = null
      var readBytes: Int = 0
      val buffer: Array[Byte] = new Array[Byte](4096)
      try {
        resStreamOut = new FileOutputStream(new File(outPath))
        while ({
          readBytes = stream.read(buffer)
          readBytes
        } > 0) {
          resStreamOut.write(buffer, 0, readBytes)
        }
      } catch {
        case e: IOException =>
          error(s"Error writing file $outPath", e)
      } finally {
        try {
          stream.close
          resStreamOut.close
        } catch {
          case e: IOException =>
            error(s"Error closing stream for $jarPath", e)
        }
      }
    }
  }

}