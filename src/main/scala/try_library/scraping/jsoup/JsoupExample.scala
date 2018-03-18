package try_library.scraping.jsoup

import java.io.File
import org.jsoup.Jsoup
import scala.collection.JavaConversions._

object JsoupExample {

  def printDoc(doc: org.jsoup.nodes.Document): Unit = {
    println("baseUri :" + doc.baseUri())
    println("title :" + doc.title)
    println("head :" + doc.head())
    println("body :" + doc.body)
  }

  def parseAndTravis = {
    val html = "<html><head><title>First parse</title></head>" +
      "<body><p>Parsed HTML into a doc.</p></body></html>"
    val doc = Jsoup.parse(html)
    printDoc(doc)
  }

  def parseBodyFragment = {
    val html = "<div><p>Lorem ipsum.</p>"
    val doc = Jsoup.parseBodyFragment(html)
    val body = doc.body
    println(body)
  }


  def showHtml(url: String) = {
    val doc = Jsoup.connect(url).get
    printDoc(doc)
  }

  def loadFromFile = {
    val input = new File("/tmp/input.html")
    Jsoup.parse(input, "UTF-8", "http://example.com/")
  }

  def showPostHtml() = {
    val doc = Jsoup.connect("http://example.com")
      .data("query", "Java")
      .userAgent("Mozilla")
      .cookie("auth", "token")
      .timeout(3000)
      .post()
  }

  def navigateADoc(url: String): Unit = {
    val doc = Jsoup.connect(url).get()
    //    doc.getAllElements.toArray.filter(_.isInstanceOf[Document]).map(_.asInstanceOf[Document]).
    //      filter (el => el.getElementsByTag("a").size() > 0 && !el.id.equals(""))
    //      .foreach{el =>
    //        val links = el.getElementsByTag("a")
    //        links.forEach{link =>
    //          println("href :" + link.attr("href"))
    //          println("text :" + link.text)
    //        }
    //      }

    val content = doc.getElementById("gbar")
    val links = content.getElementsByTag("a")
//
//    links.forEach{link =>
//      println("href: " + link)
//    }
//    links.forEach{link =>
//      println("href :" + link.attr("href"))
//      println("text :" + link.text)
//    }
  }

  def selectorSyntax(url: String) = {
    val doc = Jsoup.connect(url).get()
    val links = doc.select("a[href]") // a with href
    // val pngs = doc.select("img[src$=.png$]")
    val images = doc.select("img[src]")
    val masthead = doc.select("div.o-h").first // tag名.クラスめい
    val resultLinks = doc.select("div > a") // direct a after h3

  }

  def attributesTextHtml(url: String) = {
    val doc = Jsoup.connect(url).get
    val link = doc.select("a").first
    val text = doc.body.text // "An example link"
    val linkHref = link.attr("href") // "http://example.com/"
    val linkText = link.text // "example""

    val linkOuterH = link.outerHtml
    // "<a href="http://example.com"><b>example</b></a>"
    val linkInnerH = link.html // "<b>example</b>"
  }

  def listLinks(url: String) = {

    println("Fetching %s...", url)

    val doc = Jsoup.connect(url).get
    val links = doc.select("a[href]")
    val media = doc.select("[src]")
    val imports = doc.select("link[href]")

    println("\nMedia: (%d)".format(media.size))
//    media.forEach{src =>
//      if (src.tagName.equals("img")) println(" * %s: <%s> %sx%s (%s)".
//        format(src.tagName, trim(src.attr("abs:src")), src.attr("width"), src.attr("height"), trim(src.attr("alt"))))
//      else println(" * %s: <%s>".format(src.tagName, src.attr("abs:src")))
//    }

    println("\nImports: (%d)".format(imports.size))
    imports.foreach{link =>
      println(" * %s <%s> (%s)".format(link.tagName, trim(link.attr("abs:href")), trim(link.attr("rel"))))
    }

    println("\nLinks: (%d)".format(links.size))
    links.foreach{link =>
      println(" * a: <%s>  (%s)".format(link.attr("abs:href"), link.text))
    }
  }

  def trim(text: String):String = {
    text match {
      case x if x.length > 30 => x.substring(0, 15)
      case x:String => x
    }
  }

  def main(args: Array[String]) = {
    // parseAndTravis
    // parseBodyFragment
    // showHtml("http://www.google.co.jp")
    // navigateADoc("http://www.google.co.jp")
    // selectorSyntax("http://japanese.engadget.com/")
    // attributesTextHtml("http://www.google.co.jp")
    listLinks("http://japanese.engadget.com/")
  }
}
