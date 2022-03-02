package ca.vikelabs.maps.routes

import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.extensions.levenshteinDistanceTo
import ca.vikelabs.maps.routes.Suggest.ResponseBody.Companion.lens
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString

class Suggest(private val mapData: MapData) : HttpHandler {
    companion object {
        val query = Query.nonEmptyString().required("query", "The query string the user has started typing")
        val max = Query.int().defaulted("max", 10, "The max number of results returned.")
        val response = Body.auto<ResponseBody>().toLens()

        val spec = "suggest" meta {
            summary = "lists possible completions for a given string"
            description =
                """Similar to search but doesn't use levenshteinDistance, instead just scanning the start of buildings 
                    |and their abbreviations, it is however ordered by levenshteinDistance, capitalization are ignored 
                    |for suggestions but included for sorting.""".trimMargin()
            queries += query
            queries += max
            returning(
                OK,
                response to ResponseBody(
                    listOf("ECS", "Elliot Building")
                ),
                "a list of suggested search terms"
            )
        } bindContract Method.GET
    }

    data class ResponseBody(val suggestions: List<String>) {
        companion object {
            val lens = Body.auto<ResponseBody>().toLens()
        }
    }

    override fun invoke(req: Request) = contractRoute(req)
    val contractRoute: ContractRoute = spec to ::suggest

    private fun suggest(): HttpHandler = { req ->
        val query = query(req)
        val max = max(req)

        Response(OK).with(lens of ResponseBody(
            mapData.buildings()
                .flatMap { listOfNotNull(it.name, it.abbrName) }
                .filter { it.lowercase().startsWith(query.lowercase()) }
                .sortedBy { it.levenshteinDistanceTo(query) }
                .take(max)))
    }
}
