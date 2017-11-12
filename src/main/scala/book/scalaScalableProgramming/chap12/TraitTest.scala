package book.scalaScalableProgramming.chap12


trait Philosophical {
  def philosophize(): Unit ={
    println("i consume memory, therfore i am!")
  }
}

class Frog extends Philosophical{
  override def toString = "green"
}
class TraitTest {
}

object TraitTest {

  def main(args: Array[String]): Unit ={
    val frog = new Frog
    frog.philosophize()
    println(frog)
  }
}
