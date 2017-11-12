package book.quiz.misc

/**
  * [[BTree]]に格納される要素。
  */
sealed trait Node {

  /**
    * ノードが持つ値。
    */
  val value: Int

  /**
    * ノード数。
    */
  val size: Int

  /**
    * ノードが保持するすべての値の合計値。
    */
  val sum: Int

  /**
    * ノードが保持するすべての値の平均値。
    */
  val avg: Double

  /**
    * ノードが保持する最大値。
    */
  val max: Int

  /**
    * ノードが保持する最小値。
    */
  val min: Int

  /**
    * 指定した値を保持するノードを検索する。
    *
    * @param value 値
    * @return ノード
    */
  def find(value: Int): Option[Node]

}

/**
  * 枝を表す[[Node]]。
  *
  * @param left　左の[[Node]]
  * @param value 値
  * @param right 右の[[Node]]
  */
case class Branch(left: Node, value: Int, right: Node) extends Node {

  val size: Int = left.size + 1 + right.size

  val sum: Int = left.sum + value + right.sum

  val avg: Double = sum / size

  val max: Int = right.max

  val min: Int = left.min

  def find(value: Int): Option[Node] =
    if( value == this.value ) Option(Branch(this.left, this.value, this.right))
    else if (value > this.value) this.right.find(value )
    else this.left.find(value)

}

/**
  * 葉を表す[[Node]]。
  *
  * @param value 値
  */
case class Leaf(value: Int) extends Node {

  val size: Int = 1

  val sum: Int = value

  val avg: Double = value

  val max: Int = value

  val min: Int = value

  def find(value: Int): Option[Node] =
    if(value == size) Option(Leaf(this.value))
    else None

}

/**
  * 二分木データ構造。
  *
  * @param node 頂点のノード
  */
case class BTree(node: Node) {

  lazy val size: Int = node.size

  lazy val sum: Int = node.sum

  lazy val avg: Double = node.avg

  lazy val max: Int = node.max

  lazy val min: Int = node.min

  def find(value: Int): Option[Node] = node.find(value)

}

/**
  * [[BTree]]のコンパニオンオブジェクト。
  */
object BTree {

  /**
    * ファクトリメソッド。
    *
    * @param values ノードに格納する値の集合
    * @return [[BTree]]
    */
  def apply(values: List[Int]): BTree = ???
}


object BTreeTest{

  def main(args:Array[String]): Unit ={
    val bTree1 = BTree(Leaf(1))
    val bTree2 = BTree(Branch(Leaf(1), 2, Leaf(3)))
    val bTree3 = BTree(Branch(Branch(Leaf(1), 2, Leaf(3)), 4, Branch(Leaf(5), 6, Leaf(7))))

    bTree1.size
    bTree1.sum
    bTree1.avg
    bTree1.max
    bTree1.min

    bTree2.size
    bTree2.sum
    bTree2.avg
    bTree2.max
    bTree2.min

    bTree3.size
    bTree3.sum
    bTree3.avg
    bTree3.max
    bTree3.min

    bTree3.find(12)
  }
}
