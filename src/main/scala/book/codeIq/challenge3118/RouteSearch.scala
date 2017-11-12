package book.codeIq.challenge3118

import java.util.Scanner;


object Main{
  def main(args: Array[String]) = {
    print(searchShortestDistance2(strListToIntArray(scanArg)))
  }

  def searchShortestDistance(routeInfo: Array[Array[Int]]): Int ={
    val distInfo = Array.ofDim[Int](routeInfo.length, routeInfo.length)
    distInfo(0)(0) = routeInfo(0)(0)
    distanceSearch(routeInfo, distInfo, (0, 0))(routeInfo.length - 1)(routeInfo.length - 1)
  }

  def searchShortestDistance2(routeInfo: Array[Array[Int]]): Int ={
    val distInfo = Array.ofDim[Int](routeInfo.length, routeInfo.length)
    distInfo(0)(0) = routeInfo(0)(0)
    distanceSearch2(routeInfo, distInfo)(routeInfo.length - 1)(routeInfo.length - 1)
  }

  def scanArg = {
    var cin=new Scanner(System.in);
    var strList = List[String]()
    while(cin.hasNext()){
      strList :+= cin.nextLine()
    }
    strList
  }

  implicit def strListToIntArray(strList: List[String]): Array[Array[Int]] = {
    strList.map( st =>
      st.toCharArray.map( ch => ch.toString.toInt)
    ).toArray
  }

  def distanceSearch(routeInfo: Array[Array[Int]], distanceInfo: Array[Array[Int]], currentPositon: (Int, Int)):Array[Array[Int]] = {
    //println(distanceInfo.deep)
    if(moveOnRight(routeInfo, distanceInfo, currentPositon)){
      distanceInfo(currentPositon._1 + 1)(currentPositon._2) = distanceInfo(currentPositon._1)(currentPositon._2) + routeInfo(currentPositon._1 + 1)(currentPositon._2)
      distanceSearch(routeInfo, distanceInfo, (currentPositon._1 + 1, currentPositon._2))
    }

    if(moveOnUnder(routeInfo, distanceInfo, currentPositon)){
      distanceInfo(currentPositon._1 )(currentPositon._2 + 1) = distanceInfo(currentPositon._1)(currentPositon._2) + routeInfo(currentPositon._1)(currentPositon._2 + 1)
      distanceSearch(routeInfo, distanceInfo, (currentPositon._1, currentPositon._2 + 1))
    }
    distanceInfo
  }

  def distanceSearch2(routeInfo: Array[Array[Int]], distanceInfo: Array[Array[Int]]):Array[Array[Int]] = {
    for (i <- 0 to routeInfo.length - 1; j <- 0 to routeInfo.length - 1){
      if(moveOnRight(routeInfo, distanceInfo, (i, j))){
        distanceInfo(i + 1)(j) = distanceInfo(i)(j) + routeInfo(i + 1)(j)
      }
      if(moveOnUnder(routeInfo, distanceInfo, (i,j))){
        distanceInfo(i )(j + 1) = distanceInfo(i)(j) + routeInfo(i)(j + 1)
      }
    }
    distanceInfo
  }

  def moveOnRight(routeInfo: Array[Array[Int]], distanceInfo: Array[Array[Int]], currentPositon: (Int, Int)):Boolean = {
    currentPositon._1 < routeInfo.length - 1 &&
    ((distanceInfo(currentPositon._1 + 1)(currentPositon._2) == 0 )||
    (distanceInfo(currentPositon._1)(currentPositon._2) + routeInfo(currentPositon._1 + 1)(currentPositon._2) < distanceInfo(currentPositon._1 + 1)(currentPositon._2)))
  }

  def moveOnUnder(routeInfo: Array[Array[Int]], distanceInfo: Array[Array[Int]], currentPositon: (Int, Int)):Boolean = {
    currentPositon._2 < routeInfo.length - 1 &&
      ((distanceInfo(currentPositon._1)(currentPositon._2 + 1) == 0 )||
        (distanceInfo(currentPositon._1)(currentPositon._2) + routeInfo(currentPositon._1)(currentPositon._2 + 1) < distanceInfo(currentPositon._1)(currentPositon._2 + 1)))
  }

  val dummy = true
}