package try_library.scalikjdbcSample.model

import scalikejdbc.{AutoSession, DB, DBSession, SQL, WrappedResultSet}

case class Todo(id: Long, category_id: Long, title: String, text: String, index: Int)

case class TodoWithCategoryTitle(id: Long, category_title: String, title: String, text: String, index: Int)


object Todo {
  val allColumns = (rs: WrappedResultSet) =>
    Todo(
      id = rs.long("id"),
      category_id = rs.long("category_id"),
      title = rs.string("title"),
      text = rs.string("text"),
      index = rs.int("index")
    )

  val withCategoryText = (rs: WrappedResultSet) =>
    TodoWithCategoryTitle(
      id = rs.long("id"),
      category_title = rs.string("name"),
      title = rs.string("title"),
      text = rs.string("text"),
      index = rs.int("index")
    )


  def all(implicit session: DBSession = AutoSession) =
    SQL("select * from todo order by category_id, index").map(allColumns).list().apply()

  def allWithCategory(implicit session: DBSession = AutoSession) =
    SQL(
      """select t.id, tc.name, t.title, t.text, t.index from todo t
          left join todo_category tc on t.category_id = tc.id;
          """).map(withCategoryText).list().apply()

  def getById(id: Int)(implicit session: DBSession = AutoSession) =
    SQL(s"select * from todo where id = ${id}").map(allColumns).single.apply()

  def insertTodo(category_id: Long, title: String, text: String, index: Int)
                (implicit session: DBSession = AutoSession) =
    SQL(
      s"""insert into todo (id, category_id, title, text, index)
          values ((select max(id) + 1 from todo), ${category_id}, '${title}', '${text}', ${index})""").
      updateAndReturnGeneratedKey.apply()

  def deleteByCategoryId(category_id: Long)(implicit session: DBSession = AutoSession) =
    SQL(
      s"""delete from todo
          where category_id = ${category_id}""").
      updateAndReturnGeneratedKey.apply()

}