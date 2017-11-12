package try_library.json.j4s
object J4sParseError{
  class BaseParameterInvalid(message :String = null, cause :Throwable = null) extends RuntimeException(message, cause)
  class JsonParameterInvalid(message :String = null, cause :Throwable = null) extends RuntimeException(message, cause)
  class ParameterLoaderException(message :String = null, cause :Throwable = null) extends RuntimeException(message, cause)
  val defaultErrorMessage = ""
  /**
    * ベースパラメータのロード処理中に有効なパラメータが見つからなかった場合のエラー生成
    * @param record
    * @param udatedate
    * @return
    */
  def invalidRecord(record: String, udatedate: java.sql.Timestamp): BaseParameterInvalid =  {
    val errorMessage = "ベースパラメータロード中にエラー発生:" + udatedate + "の時間帯で" + record + "に有効なレコードが見つかりませんでした。" + defaultErrorMessage
    new BaseParameterInvalid(errorMessage, null)
  }
  /**
    * jsonパラメータのsinceまたは, configを読み込めなかった場合のエラー生成
    * @param row
    * @param source
    * @return
    */
  def invalidJsonParameterByFormat(row: Any, source: String): JsonParameterInvalid =  {
    val errorMessage = "Jsonパラメータロード中にエラー発生: sinceまたはconfigを読み込めなかったため" + row + "をパースできませんでした。対象Json="+ source + defaultErrorMessage
    new JsonParameterInvalid(errorMessage, null)
  }
  /**
    * jsonパラメータのsinceが"d/hh:mm:ss"と異なっている場合のエラー生成
    * @param row
    * @param source
    * @return
    */
  def invalidJsonParameterBySinceFormat(row: Any, source: String): JsonParameterInvalid =  {
    val errorMessage = "Jsonパラメータロード中にエラー発生: sinceのフォーマットが'd/hh:mm:ss'と異なるため" + row + "をパースできませんでした。対象Json="+ source + defaultErrorMessage
    new JsonParameterInvalid(errorMessage, null)
  }
  /**
    * 実行時例外が発生した場合
    * @param cause
    * @param e
    * @return
    */
  def paramLoadException(cause: String, e: Exception) : ParameterLoaderException = {
    val errorMessage = cause
    new ParameterLoaderException(errorMessage, e)
  }

}