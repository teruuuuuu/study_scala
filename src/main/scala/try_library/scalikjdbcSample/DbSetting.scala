package try_library.scalikjdbcSample

import com.typesafe.config.ConfigFactory
import scalikejdbc.ConnectionPool

/**
  * Created by arimuraterutoshi on 2017/02/11.
  */
trait DbSetting {

  def loadJDBCSettings(): Unit ={

    val config = ConfigFactory.load
    Class.forName(config.getString("db.default.driver"))
    val url = config.getString("db.default.url")
    val user = config.getString("db.default.user")
    val password = config.getString("db.default.password")

    ConnectionPool.singleton(url, user, password)
  }

  loadJDBCSettings()
}
