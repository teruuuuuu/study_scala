package book.scalaScalableProgramming.chap23

object ForQuery {
  case class Book(title: String, authors: String*)

  val books: List[Book] =
    List(
      Book(
        "Structure and InterPretation of Computer Programs",
        "Abelson, Harrold", "Sussman, Gerald J. "
      ),
      Book(
        "Principles of Compiler Design",
        "Aho. Alfred", "Ullman, Jeffrey"
      ),
      Book(
        "Programming in Modula-2",
        "Wirth, Niklaus"
      ),
      Book(
        "Elements of ML Programming",
        "Ullman, Jeffrey"
      ),
      Book(
        "The Java Language Specification",
        "Gosling, James",
        "Joy, Bill","Steele, Guy", "Bracha, Gilad"
      )
    )

  def main(args:Array[String]): Unit ={

    val filterList = for(
      b <- books;
      a <- b.authors
      if a startsWith("Ullman, Jeffrey")
    ) yield b.title
    println(filterList)

    val likeList = for(
      b <- books if (b.title indexOf "Program") >= 0
    ) yield b.title
    println(likeList)

    val multiAuthor = for(
      b1 <- books;
      b2 <- books if b1 != b2;
      a1 <- b1.authors;
      a2 <- b2.authors if a1 == a2
    ) yield a1
    println(multiAuthor)

    def removeDuplicates[A](xs: List[A]): List[A] = {
      if (xs.isEmpty) xs
      else
        xs.head :: removeDuplicates(
          xs.tail filter (x => x != xs.head)
        )
    }
    println(removeDuplicates(multiAuthor))


    val fmapList = books flatMap( b1 =>
      books withFilter( b2 => b1 != b2) flatMap (b2 =>
        b1.authors flatMap( a1 =>
          b2.authors withFilter( a2 => a1 == a2) map (a2 =>
            a1))
        ))
    println(removeDuplicates(fmapList))

    var buf:List[String] = List()
    println(
      books flatMap( b1 =>
        books withFilter( b2 => b1 != b2) flatMap (b2 =>
          b1.authors flatMap( a1 =>
            b2.authors withFilter( a2 => a1 == a2)
              withFilter ( a2 => ! buf.contains(a2)) map (a2 => {
                buf = buf :+ a1
                a1
              })
            )
          ))
    )

  }


}
