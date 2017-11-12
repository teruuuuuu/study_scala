package try_library.scalikjdbcSample


import try_library.scalikjdbcSample.model.{Todo, TodoWithCategoryTitle}
import scalikejdbc._
import scalikejdbc.config._

object ScalickJdbcSample extends App with DbSetting with LogSettings{


  // Listの扱い(scalickJdbc is blocking io)
  val todos: List[Todo] = DB readOnly {
    implicit session =>
      Todo.all
  }
  todos.foreach( todo =>
    println(todo.category_id + " " + todo.title.trim + " " + todo.text.trim + " " + todo.index)
  )
  val todosWithCategory: List[TodoWithCategoryTitle] = DB readOnly {
    implicit session =>
      Todo.allWithCategory
  }
  todosWithCategory.foreach( todo =>
    println(todo.id + " " + todo.category_title.trim + " " + todo.title.trim + " " + todo.text.trim + " " + todo.index)
  )

  // option型の扱い
  val todo: Option[Todo] = DB readOnly {
    implicit session =>
      Todo.getById(2)
  }
  todo match {
    case Some(x) => println("get by id 12:" +todo.get.text.trim)
    case None => println("Noneです")
  }
  val noTodo: Option[Todo] = DB readOnly {
    implicit session =>
      Todo.getById(12)
  }
  noTodo.getOrElse()
  noTodo match {
    case Some(x) => println("get by id 12:" +todo.get.text)
    case None => println("get by id 12: Noneです")
  }


  //トランザクション管理
  DB localTx {
    implicit session =>
      Todo.deleteByCategoryId(4)
  }
  DB localTx {
    implicit session =>
      Todo.insertTodo(4, "insert_test", "これは入る", 1)
  }
  try{
    DB localTx {
      implicit session =>
        Todo.insertTodo(4, "insert_test", "これは入らない", 1)
        throw new Exception("DBインサートさせない")
    }
  }catch{
    case e: Exception => e.printStackTrace()
  }




  DBs.closeAll()
}


