package ca.vikelabs.maps.routes

/**
 * base interface for every response maps returns
 */
sealed class MapsResponse(val success: Boolean) {
    open class Success : MapsResponse(success = true)
    open class Failure(open val reason: String) : MapsResponse(success = false)
}
