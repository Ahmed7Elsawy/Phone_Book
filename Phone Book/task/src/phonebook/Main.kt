package phonebook

import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val dictionaryList = readDirectoryFile("/home/ahmed/Downloads/directory.txt")
    val findList = readFindFile("/home/ahmed/Downloads/find.txt")
    val startTime = System.currentTimeMillis()
    println("Start searching...")
    var sum = 0
    for (name in findList) {
        if (dictionaryList.containsKey(name))
            sum++
        Thread.sleep(100)
    }

    val endTime = System.currentTimeMillis()
    val time = endTime - startTime
    val s = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", time)
    println("Found $sum / ${findList.size} entries. Time taken: $s")
}

private fun readDirectoryFile(filePath: String): MutableMap<String, String> {
    val inputStream: InputStream = File(filePath).inputStream()
    val lineList = mutableMapOf<String, String>()
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach {
            val line = it.split(" ")
            val name = line.subList(1, line.size).joinToString(" ")
            lineList[name] = line[0]
        }
    }
    return lineList
}

private fun readFindFile(filepath: String): MutableList<String> {
    val inputStream: InputStream = File(filepath).inputStream()
    val lineList = mutableListOf<String>()
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { lineList.add(it) }
    }
    return lineList
}

