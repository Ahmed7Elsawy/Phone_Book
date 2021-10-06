package phonebook

import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.math.sqrt

data class Phone(val name: String, val number:String)

fun main(args: Array<String>) {
    val dictionaryList = readDirectoryFile("/home/ahmed/Downloads/directory.txt")
    val findList = readFindFile("/home/ahmed/Downloads/find.txt")

    linearSearch(dictionaryList, findList)
    bubbleSortJumpSearch(dictionaryList.toMutableList(), findList)
    quickSortBinarySearch(dictionaryList.toMutableList(), findList)
    hashTable(dictionaryList, findList)
}

private fun readDirectoryFile(filePath: String): MutableList<Phone> {
    val inputStream: InputStream = File(filePath).inputStream()
    val lineList = mutableListOf<Phone>()
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { element ->
            val line = element.split(" ")
            val number = line[0]
            val name = line.subList(1, line.size).joinToString(" ")
            lineList.add(Phone(name, number))
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

private fun linearSearch(dictionaryList: MutableList<Phone>, findList: MutableList<String>) {
    val startTimeLinear = System.currentTimeMillis()
    println("Start searching (linear search)...")
    var sum = 0
    for (name in findList) {
        for (phone in dictionaryList) {
            if (phone.name == name) {
                sum++
                break
            }
        }
    }

    val endTimeLinear = System.currentTimeMillis()
    val timeLinear = endTimeLinear - startTimeLinear
    val timeLinearFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", timeLinear)
    println("Found $sum / ${findList.size} entries. Time taken: $timeLinearFormat\n")
}

private fun bubbleSort(dictionaryList: MutableList<Phone>) {
    for (i in 1..dictionaryList.size) {
        for (j in 1..dictionaryList.size - i) {
            if (dictionaryList[j - 1].name > dictionaryList[j].name) {
                val temp = dictionaryList[j - 1]
                dictionaryList[j - 1] = dictionaryList[j]
                dictionaryList[j] = temp
            }
        }
    }
}

private fun jumpSearch(name: String, dictionaryList: MutableList<Phone>) :Boolean{
    val blockSize = sqrt(dictionaryList.size.toDouble()).toInt()
    var left = 0
    var right = blockSize
    while (left < dictionaryList.size){
        if (dictionaryList[right].name < name){
            left = right + 1
            right += blockSize
            if (right >= dictionaryList.size)
                right = dictionaryList.size - 1
        }else {
            for (index in right downTo left) {
                if (dictionaryList[index].name == name)
                    return true
            }
            return false
        }
    }
    return false
}

private fun bubbleSortJumpSearch(dictionaryList: MutableList<Phone>, findList: MutableList<String>) {
    println("Start searching (bubble sort + jump search)...")
    val startTimeBubbleSort = System.currentTimeMillis()
    bubbleSort(dictionaryList)
    val endTimeBubbleSort = System.currentTimeMillis()
    val timeBubbleSort = endTimeBubbleSort - startTimeBubbleSort

    var sum = 0
    for (name in findList) {
        if (jumpSearch(name, dictionaryList))
            sum++
    }
    val endTimeJumpSearch = System.currentTimeMillis()
    val timeJumpSearch = endTimeJumpSearch - endTimeBubbleSort

    val sortSearchTime = timeBubbleSort + timeJumpSearch
    val sortSearchTimeFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", sortSearchTime)
    println("Found $sum / ${findList.size} entries. Time taken: $sortSearchTimeFormat")

    val timeBubbleSortFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", timeBubbleSort)
    println("Sorting time: $timeBubbleSortFormat")
    val timeJumpSearchFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", timeJumpSearch)
    println("Searching time: $timeJumpSearchFormat\n")
}

private fun quickSort(dictionaryList: MutableList<Phone>, start:Int = 0, end: Int = dictionaryList.lastIndex) {

    if (start >= end) return

    var pivotIndex = end
    var min = start + 1
    var max = end - 1

    var index = start
    while (index < max) {
        if (dictionaryList[index].name < dictionaryList[pivotIndex].name) {
            min++
            index++
        } else {
            Collections.swap(dictionaryList,index,max)
            max--
        }
    }

    if (max == -1 || dictionaryList[max].name < dictionaryList[pivotIndex].name) {
        Collections.swap(dictionaryList, max + 1, pivotIndex)
        pivotIndex = max + 1
    }else {
        Collections.swap(dictionaryList, max, pivotIndex)
        pivotIndex = max
    }
    quickSort(dictionaryList,start,pivotIndex-1)
    quickSort(dictionaryList,pivotIndex+1,end)
}

private fun binarySearch(name: String, dictionaryList: MutableList<Phone>, start: Int = 0, end:Int = dictionaryList.lastIndex) :Boolean {

    val mid = (start + end) / 2
    if (start == end)
        return dictionaryList[start].name == name

    if (dictionaryList[mid].name == name)
        return true

    return if (dictionaryList[mid].name > name)
        binarySearch(name, dictionaryList, start, mid - 1)
    else
        binarySearch(name, dictionaryList, mid + 1, end)
}

private fun quickSortBinarySearch(dictionaryList: MutableList<Phone>, findList: MutableList<String>) {

    println("Start searching (quick sort + binary search)...")
    val startTimeQuickSort = System.currentTimeMillis()
    quickSort(dictionaryList)
    val endTimeQuickSort = System.currentTimeMillis()
    val timeQuickSort = endTimeQuickSort - startTimeQuickSort

    var sum = 0
    for (name in findList) {
        if (binarySearch(name, dictionaryList))
            sum++
    }
    val endTimeBinarySearch = System.currentTimeMillis()
    val timeBinarySearch = endTimeBinarySearch - endTimeQuickSort

    val sortSearchTime = timeQuickSort + timeBinarySearch
    val sortSearchTimeFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", sortSearchTime)
    println("Found $sum / ${findList.size} entries. Time taken: $sortSearchTimeFormat")

    val timeQuickSortFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", timeQuickSort)
    println("Sorting time: $timeQuickSortFormat")
    val timeBinarySearchFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", timeBinarySearch)
    println("Searching time: $timeBinarySearchFormat\n")
}

private fun hashTable(dictionaryList: MutableList<Phone>, findList: MutableList<String>) {
    println("Start searching (hash table)...")

    val startTimeHashTable = System.currentTimeMillis()
    val dictionaryHashtable = Hashtable<String,String>()
    dictionaryList.forEach{
        dictionaryHashtable[it.name] = it.number
    }
    val endCreatingTime = System.currentTimeMillis()

    var sum = 0
    for (name in findList) {
        if (dictionaryHashtable.containsKey(name)) {
            sum++
        }
    }

    val endTimeSearchHashTable = System.currentTimeMillis()

    val hashTime = endTimeSearchHashTable - startTimeHashTable
    val hashTimeFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", hashTime)
    println("Found $sum / ${findList.size} entries. Time taken: $hashTimeFormat")

    val creationTime = endCreatingTime - startTimeHashTable
    val creationTimeFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", creationTime)
    println("Creating time: $creationTimeFormat")

    val searchTime = endTimeSearchHashTable - endCreatingTime
    val searchTimeFormat = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", searchTime)
    println("Searching time: $searchTimeFormat")
}