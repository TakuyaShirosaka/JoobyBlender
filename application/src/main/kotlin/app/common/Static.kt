package app.common

object Static {
    fun suffix(fileName: String): String {
        return when (val point = fileName.lastIndexOf(".")) {
            -1 ->
                fileName
            else ->
                fileName.substring(0, point)
        }
    }
}