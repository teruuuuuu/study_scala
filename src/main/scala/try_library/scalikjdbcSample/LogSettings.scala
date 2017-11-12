package try_library.scalikjdbcSample

import scalikejdbc.{GlobalSettings, LoggingSQLAndTimeSettings}


trait LogSettings {
  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = true,
    logLevel = 'DEBUG,
    warningEnabled = true,
    warningThresholdMillis = 1000L,
    warningLogLevel = 'WARN
  )
}
