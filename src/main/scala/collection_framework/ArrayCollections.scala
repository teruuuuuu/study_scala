package collection_framework

import org.joda.time.DateTime


case class Element(id: Int, time: java.sql.Timestamp)

object ArrayCollections {
  def main(args:Array[String]): Unit ={


    val elementSeq = Array(
      Element(1, new java.sql.Timestamp(new DateTime(2017, 8, 10, 16, 13).getMillis))
      , Element(2, new java.sql.Timestamp(new DateTime(2017, 8, 9, 11, 5).getMillis))
      , Element(3, new java.sql.Timestamp(new DateTime(2017, 5, 22, 9, 13).getMillis))
      , Element(4, new java.sql.Timestamp(new DateTime(2017, 9, 1, 22, 13).getMillis))
      , Element(5, new java.sql.Timestamp(new DateTime(2017, 7, 31, 23, 13).getMillis))
      , Element(6, new java.sql.Timestamp(new DateTime(2017, 8, 15, 12, 7).getMillis))
    ).toSeq

    //elementArray.sortBy( - _.time.getTime).foreach(println)

//    (new java.sql.Timestamp(new DateTime(2017, 5, 1, 0, 0).getMillis)
//      +: )
//     getMillis .sortBy( - _.getTime)

    val sortedArray = elementSeq.sortBy(-_.time.getTime)
    sortedArray.foreach(println)

    val elementSeq2 = Array(
      Element(1, new java.sql.Timestamp(new DateTime(2017, 8, 12, 15, 11).getMillis))
      , Element(2, new java.sql.Timestamp(new DateTime(2017, 8, 7, 6, 5).getMillis))
      , Element(3, new java.sql.Timestamp(new DateTime(2017, 5, 9, 16, 13).getMillis))
      , Element(4, new java.sql.Timestamp(new DateTime(2017, 9, 1, 23, 59).getMillis))
      , Element(5, new java.sql.Timestamp(new DateTime(2017, 7, 31, 23, 12).getMillis))
      , Element(6, new java.sql.Timestamp(new DateTime(2017, 8, 14, 23, 4).getMillis))
    ).toSeq

    val unionTimeSeq = elementSeq.map(_.time)
      .union(elementSeq2.map(_.time))
      .sortBy(_.getTime)
    unionTimeSeq.foreach(println)

    val startDate = new java.sql.Timestamp(new DateTime(2017, 6, 1, 0, 0).getMillis)
    val timeArray = (startDate +: unionTimeSeq)
      .filter(_.getTime >= startDate.getTime)
      .sortBy(_.getTime)
    timeArray.foreach(println)

    val validRecordSeq = timeArray.map{time=>
      val record = sortedArray
        .filter(_.time.getTime <= time.getTime)
        .headOption.getOrElse(Element(0, new java.sql.Timestamp(new DateTime(2017, 6, 1, 0, 0).getMillis)))
      (time, record)
    }
    validRecordSeq.foreach(println)


  }
}
