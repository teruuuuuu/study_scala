package book.scala_text

class basic{

  def cal(a: Double, b: Double, n: Int):Double = {
    n match {
      case 1 => a * b
      case _ => cal(a * b, b, n - 1)
    }
  }
}


object exec{

  def main(args: Array[String]): Unit ={
    val basic = new basic()
    println(basic.cal(3950000, 1.023, 8).toInt)

    println(1 - (((26400 / 0.016) - 26400) / 1980000))

    val TEIKA = 1980000
    val GENKA = (26400 / 0.016)
    val NEBIKIGO = GENKA - 26400
    val NEBIKI = TEIKA - NEBIKIGO
    val NEBIKI_RATE = NEBIKI / TEIKA

    println("原価: " + GENKA)
    println("値引き後: " + NEBIKIGO)
    println("値引き率: " + NEBIKI_RATE)
    NEBIKI
  }
}
