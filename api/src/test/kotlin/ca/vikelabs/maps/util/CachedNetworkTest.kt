package ca.vikelabs.maps.util

import mu.KotlinLogging
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.TrafficFilters
import org.http4k.traffic.ReadWriteCache

private val logger = KotlinLogging.logger { }

abstract class CachedNetworkTest(
    private val networkClient: HttpHandler? = null,
    storage: ReadWriteCache = ReadWriteCache.Disk("src/test/resources/cache")
) {

    val cachedClient = TrafficFilters.ServeCachedFrom(storage)
        .then(TrafficFilters.RecordTo(storage))
        .then { request ->
            logger.warn { "a CachedNetworkTest is falling back onto a network call. This should only occur on the first round of testing." }
            networkClient?.invoke(request)
                ?: throw Exception("no network client to fall back on")
        }
}
