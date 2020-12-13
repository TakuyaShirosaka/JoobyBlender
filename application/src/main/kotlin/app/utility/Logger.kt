package app.utility

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Logger {
    companion object {
        fun getLogger(forClass: Class<*>): Logger {
            return LoggerFactory.getLogger(forClass)
        }
    }
}