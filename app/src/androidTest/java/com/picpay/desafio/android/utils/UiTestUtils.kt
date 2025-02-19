import androidx.test.platform.app.InstrumentationRegistry

object UiTestUtils {

    fun getJsonContent(fileName: String): String {
        val context = InstrumentationRegistry.getInstrumentation().context
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}