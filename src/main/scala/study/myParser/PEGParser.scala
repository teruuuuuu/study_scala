package study.myParser

trait PackratParser[+A] {
  val input: String
  abstract class Parser[+A] extends (Int => Option[(A, Int)]) {
    def /[B >: A](that: => Parser[B]): Parser[B] = parserOf{pos =>
      this(pos) orElse that(pos)
    }
    def ~[B](that: => Parser[B]): Parser[(A, B)] = parserOf{pos =>
      for((r1, rpos1) <- this(pos);
          (r2, rpos2) <- that(rpos1)) yield ((r1, r2), rpos2)
    }
    def ^^[B](f: A => B): Parser[B] = parserOf{pos =>
      for((r, rpos) <- this(pos)) yield (f(r), rpos)
    }
    def ? : Parser[Option[A]] = parserOf{pos =>
      this(pos).map{p => (Some(p._1), p._2)}.orElse(Some(None, pos))
    }
    def * : Parser[List[A]] = parserOf{pos =>
      def parse(pos: Int) : (List[A], Int) = {
        this(pos) match {
          case Some((r, rpos)) => val (r2, rpos2) = parse(rpos); (r::r2, rpos2)
          case None => (Nil, pos)
        }
      }
      Some(parse(pos))
    }
    def + : Parser[List[A]] = (this ~ this.*) ^^ {p => p._1::p._2}
    def unary_! : Parser[Unit] = parserOf{pos =>
      this(pos) match {
        case Some(_) => None
        case None => Some((), pos)
      }
    }
  }
  class Memoized[+A](val parser: Parser[A]) extends Parser[A] {
    import scala.collection.mutable._
    private[this] val cache = new HashMap[Int, Option[(A, Int)]]
    def apply(pos: Int): Option[(A, Int)] = {
      cache.get(pos).getOrElse {
        val result = parser(pos)
        cache(pos) = result
        result
      }
    }
  }
  def parserOf[A](f: Int => Option[(A, Int)]): Parser[A] =
    new Parser[A] {
      def apply(param: Int): Option[(A, Int)] = f(param)
    }
  def string(param: String): Parser[String] = parserOf{pos =>
    val in = input.substring(pos)
    if(in startsWith param) Some(param, pos + param.length)
    else None
  }
  def and[A](p: => Parser[A]): Parser[Unit] = !(!p)
  lazy val any: Parser[String] = parserOf{pos =>
    val in = input.substring(pos)
    if(in.length > 0) Some(in substring(0, 1), pos + 1) else None
  }
  implicit def stringToParser(param: String) = string(param)
  implicit def parserToMemoized[A](p: Parser[A]) = new Memoized(p)
  def parse: Option[(A, String)] = {
    for((r, pos) <- S(0)) yield (r, input.substring(pos))
  }
  val S: Parser[A]
}

class ParenParser(override val input: String) extends PackratParser[Any] {
  lazy val S: Parser[Any] = A ~ !any
  lazy val A: Parser[Any] = P ~ "+" ~ A / P ~ "-" ~ A / P
  lazy val P: Parser[Any] = "(" ~ A ~ ")" / "1"
}

//memoized
class MemoizedParenParser(override val input: String) extends PackratParser[Any] {
  lazy val S: Memoized[Any] = A ~ !any
  lazy val A: Memoized[Any] = P ~ "+" ~ A / P ~ "-" ~ A / P
  lazy val P: Memoized[Any] = "(" ~ A ~ ")" / "1"
}

object Exec{
  def main(args: Array[String]): Unit ={
    def time(any: => Any): Long = {
      val start = System.currentTimeMillis
      any
      System.currentTimeMillis - start
    }

    val input = "(((((((((((((1)))))))))))))"
    printf("not memoized: %dms%n", time{new ParenParser(input).parse})
    printf("memoized: %dms%n", time{new MemoizedParenParser(input).parse})
  }
}

