package book.scalaScalableProgramming.chap23

object MyStudy {
/*
  case class Todo(TodoId: Int, CategoryId: Int, Title: String, Text: String, Order: Int)
  case class TodoCategory(CategoryId: Int, Category: String, Order: Int)

  class TodoSet(category: TodoCategory, todoList: List[Todo])

  val collecting_data = Todo(1, 1, "collect data", "資料を集める", 1)
  */

  case class Todo(TodoId: Int, CategoryId: Int, Title: String, Text: String, Order: Int, worker: List[String]){
    override def toString = Text
  }
  case class TodoCategory(CategoryId: Int, Category: String, Order: Int, TodoList: List[Todo])

  val collecting_doc = Todo(1, 1, "collect data", "資料を集める", 1, List("sato", "nakata"))
  val prepare_tool = Todo(2, 1, "prepare tool", "ツールを準備する", 2, List("kato", "nakata"))
  val investigate = Todo(3, 2, "investigate", "調査する", 1, List("sato"))
  val summarize_result = Todo(4, 2, "summarize result", "調査結果をまとめる", 2, List("kato", "nakata"))
  val review = Todo(5, 2, "review", "レビューを受ける", 3, List("sato", "nakata"))
  val presentation = Todo(6, 3, "presentation", "発表する", 1, List("sato"))
  val improve = Todo(7, 3, "improve", "改善する", 2, List("sato"))

  val done = TodoCategory(1, "完了", 1, List(collecting_doc, prepare_tool))
  val doing = TodoCategory(2, "実施中", 2, List(investigate, summarize_result, review))
  val next = TodoCategory(3, "待ち", 3, List(presentation, improve))

  val allTodo: List[TodoCategory] = List(done, doing, next)


  def main(args: Array[String]): Unit ={

    workerTaskFor("nakata") foreach(println)

    workerTaskFlatmap("nakata") foreach(println)

  }

  def workerTaskFor(name: String): List[Todo] ={
    for(
      category <- allTodo;
      todo <- category.TodoList;
      tw <- todo.worker
      if tw eq name
    ) yield todo
  }

  def workerTaskFlatmap(name: String): List[Todo] = {
    allTodo flatMap(category =>
      category.TodoList flatMap( todo =>
        todo.worker withFilter (worker => worker eq name) map( worker =>
          todo
        )
      )
    )
  }
}
