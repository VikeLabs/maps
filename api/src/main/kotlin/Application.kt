import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status

val application: HttpHandler = { _: Request ->
    Response(Status.OK)
}