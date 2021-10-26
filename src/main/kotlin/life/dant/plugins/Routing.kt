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


fun Application.configureRouting() {

    routing {
        get("/") {

            try {
                val url = javaClass.classLoader.getResource("index.html");

                //Hämtar index.html från resources och länkar samman alla raderna till en string.
                val text = File(url.file).readLines().joinToString(
                        separator = "\n"
                    )

                //Skapar en range mellan minsta ord-id och högsta ord-id och tar ut ett slumpmässigt värde.
                //Jag gillar framför allt hur mycket tydligare det är över javas Random().nextInt(min, max)
                var random = (Ord.MIN_ID..Ord.MAX_ID).random()

                //Skapar ett ord-object (eller mer likt struct i c# enligt mig. Men är kanske inte värdebaserat som i c#) från random id.
                var ord = Ord(random)

                //Jag ville ge tillbaka som text men då skapar Routing ett helt htmldokument och lägger texten i body så det fick bli en bytearray istället.
                // Stream hade nog också funkat.
                call.respondBytes(addToHtml(text, ord).toByteArray(Charset.defaultCharset()))

            }catch (e: Exception) {
                //Inte helt nödvändigt med try/catch här men jag ville testa multi-catch och hur exceptions fungerade med kotlin.
                when (e) {
                    is NullPointerException, is FileNotFoundException -> {
                        call.respondText { "Something went wrong. Could not load file or word" }
                        e.printStackTrace()
                    }
                    else -> throw e;
                }
            }
        }
    }
}
