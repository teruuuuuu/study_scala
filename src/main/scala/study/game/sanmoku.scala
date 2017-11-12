package study.game

/**
  * Created by arimuraterutoshi on 2016/11/19.
  */
class defaultString(str: String) {

  val show = str match{
    case null => " "
    case _ => str
  }
}

class Sanmoku {
  val row = 3
  val turn = Array("W", "B")
  //var turn_index = (scala.math.random * turn.length).toInt
  var turn_index = 0
  val array = new Array[String](this.row * this.row)

  implicit def string2lonelyString(s: String) = new defaultString(s)


  def place = {
    val unset_place = this.get_unset_palce(this.row, this.array)
    val set_place = (scala.math.random * unset_place.length).toInt
    this.array(unset_place(set_place)) = this.turn(this.turn_index)
    this.next_turn
    this.show
  }

  def place_input(col:Int, row: Int):Int = {
    if(this.array(col + this.row * row) == null){
      this.array(col + this.row * row) = this.turn(this.turn_index)
      this.next_turn
      this.show
      1
    }else{
      0
    }

  }

  def get_unset_palce(row: Int, array: Array[String]):List[Int] = {
    var result_list = List[Int]()
    for (i <- 0 to row * row - 1)
      if (array(i) == null) result_list = result_list :+ i
    result_list
  }

  def next_turn = {
    this.turn_index = (this.turn_index + 1) % this.turn.length
  }

  def show = {
    //println(this.array.deep)
    println("┌---┬---┬---┐  ")
    for(i <- 0 to this.array.length - 1){
      print(s"│ ${this.array(i).show} ")
      if(i % 3 == 2) {
        print("│ \n")
        if( i / 3 == 2){
          print("└---┴---┴---┘\n")
        }else{
          print("├---┼---┼---┤\n")
        }
      }
    }
  }

  def judge():String = {
    val b = new scala.util.control.Breaks
    for( i <- 0 to this.row -1)
      b.breakable {
        for (j <- 0 to this.row - 1) {
          if (this.array(3 * i + j) == null) b.break()
          else if (this.array(3 * i) != this.array(3 * i + j)) b.break()
          else if (j == this.row - 1) return this.array(3 * i)
        }
      }

    for( i <- 0 to this.row -1)
      b.breakable {
        for (j <- 0 to this.row - 1) {
          if (this.array(i + j * this.row) == null) b.break()
          else if (this.array(i) != this.array(i + j * this.row)) b.break()
          else if (j == this.row - 1) return this.array(i)
        }
      }

    if(this.array(0) != null && this.array(0) == this.array( 4) && this.array(0) ==  this.array( 8)) return this.array(0)
    if(this.array(2) != null && this.array(2) == this.array( 4) && this.array(2) == this.array( 6)) return this.array(2)
    "-1"
  }

  def end():Boolean = {
    if(this.get_unset_palce(this.row, this.array).length == 0) true
    else if(!this.judge().equals("-1")) true
    else false
  }

  def numInput(title: String): Int ={
    val scanner = new java.util.Scanner(System.in)
    print(title)
    while(scanner.hasNext){
      try {
        val num: Int = scanner.next.toInt
        if (num >= 0 && num < this.row){
          return num
        }else{
          print(title)
        }
      }catch {
        case e: Exception => print(title)
      }finally {
      }
    }
    return -1
  }

  def auto(): Unit ={
    while(!this.end){
      this.place
    }
    val result = this.judge()
    if(result.equals("-1")) println("引き分け")
    else println(s"${result}勝利")
  }

  def manual(): Unit ={
    val b = new scala.util.control.Breaks
    while(!this.end){
      if(this.turn_index == 0){
        b.breakable{
          while(true){
            val col:Int = this.numInput("行:")
            val row:Int = this.numInput("列:")
            if(this.place_input(col, row) != 0){
              b.break()
            }else{
              println("そこには置けません")
            }
          }
        }


      }else{
        this.place
      }
    }
    val result = this.judge()
    if(result.equals("-1")) println("引き分け")
    else println(s"${result}勝利")
  }

}

object exec extends App{
  val sanmoku = new Sanmoku()
  //sanmoku.auto()
  sanmoku.manual()
}