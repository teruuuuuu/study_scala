package study.network

import java.io.{BufferedOutputStream, FileOutputStream}
import java.net.URL

/**
  * Created by arimuraterutoshi on 2016/11/23.
  */
class FileDownload {

}

object Main {
  def main(args: Array[String]) = {
    val image_url = "http://www.yahoo.co.jp/"
    val file_name = "yahoo.html"
    download(image_url, file_name)
  }

  def download(url: String, file_name: String) = {
    val stream = new URL(url).openStream
    val buf = Stream.continually(stream.read).takeWhile( -1 != ).map(_.byteValue).toArray
    val bw = new BufferedOutputStream(new FileOutputStream(file_name))
    bw.write(buf)
    bw.close
  }
}