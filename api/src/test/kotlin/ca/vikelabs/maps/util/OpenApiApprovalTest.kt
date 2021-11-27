package ca.vikelabs.maps.util

import com.github.underscore.lodash.Json
import com.github.underscore.lodash.U
import org.http4k.testing.ApprovalContent
import org.http4k.testing.ApprovalSource
import org.http4k.testing.BaseApprovalTest
import org.http4k.testing.NamedResourceApprover
import org.http4k.testing.ReadResource
import org.http4k.testing.ReadWriteResource
import org.http4k.testing.TestNamer
import org.junit.jupiter.api.extension.ExtensionContext
import org.opentest4j.AssertionFailedError
import java.io.InputStream
import java.io.OutputStream
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

class OpenApiApprovalTest : BaseApprovalTest {
    override fun approverFor(context: ExtensionContext) = NamedResourceApprover(
        TestNamer.ClassAndMethod.nameFor(context.requiredTestClass, context.requiredTestMethod),
        jsonApprover,
        approvalSource
    )

    private val jsonApprover = ApprovalContent.HttpBodyOnly { input ->
        try {
            U.formatJson(input, Json.JsonStringBuilder.Step.FOUR_SPACES)
        } catch (e: Json.ParseException) {
            throw AssertionFailedError("Invalid JSON generated", "<valid JSON>", input)
        }
    }

    private val approvalSource = object : ApprovalSource {
        val pathString = "src/test/resources/openapi.json"
        override fun actualFor(testName: String) = object : ReadWriteResource {
            val actualPath = Path("$pathString.actual")

            override fun input(): InputStream? {
                return if (actualPath.exists()) {
                    actualPath.inputStream()
                } else {
                    null
                }
            }

            override fun output(): OutputStream = actualPath.outputStream()
        }

        override fun approvedFor(testName: String) = ReadResource { Path(pathString).inputStream() }
    }
}
