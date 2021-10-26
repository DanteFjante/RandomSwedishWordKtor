package life.dant.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*
import life.dant.Ord
import life.dant.addToHtml
import java.io.File
import java.io.FileNotFoundException
import java.lang.NullPointerException
import java.nio.charset.Charset
import java.util.*


fun Application.configureRouting() {

    routing {
        get("/") {

            try {
                val url = javaClass.classLoader.getResource("index.html");
                val text = File(url.file).readLines().joinToString(
                        separator = "\n"
                    )
                var random = (Ord.MIN_ID..Ord.MAX_ID).random()
                var ord = Ord(random)

                call.respondBytes(addToHtml(text, ord).toByteArray(Charset.defaultCharset()))

            }catch (e: Exception) {
                when (e) {
                    is NullPointerException, is FileNotFoundException -> {
                        call.respondText { "Something went wrong. Could not load file" }
                        e.printStackTrace()
                    }
                    else -> throw e;
                }
            }
        }
    }
}
