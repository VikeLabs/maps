package ca.vikelabs.maps.extensions

import kotlin.math.min

fun CharSequence.levenshteinDistanceTo(rhs: CharSequence): Int {
    if (this == rhs) {
        return 0
    }
    if (this.isEmpty()) {
        return rhs.length
    }
    if (rhs.isEmpty()) {
        return this.length
    }

    val thisLength = this.length + 1
    val rhsLength = rhs.length + 1

    var cost = Array(thisLength) { it }
    var newCost = Array(thisLength) { 0 }

    for (i in 1 until rhsLength) {
        newCost[0] = i

        for (j in 1 until thisLength) {
            val match = if (this[j - 1] == rhs[i - 1]) 0 else 1

            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1

            newCost[j] = min(min(costInsert, costDelete), costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost[thisLength - 1]
}
