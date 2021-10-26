package life.dant

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import life.dant.plugins.*

fun main() {
    embeddedServer(Netty, port = 80, host = "192.168.1.2") {
        configureRouting()
        configureSockets()
        configureSerialization()
    }.start(wait = true)

    //Lite regex jag använde innan jag refaktorerade ut användningen.
    // Men ville inte ta bort de helt utifall att jag får framtida användning för de.
    // Det kanske går emot er policy att inte fokusera på hypotetiska problem.
    // Men här handlar det inte om att skriva ny kod. Endast spara gammal kod.

    //val regurl = Regex("(?<=\")http[s]?:\\/\\/.*?(?=\")")
    //val regcontent = Regex("(?<=>)[0-9A-z]+(?=<)")
    //val regcontent = Regex("(?<=<div id="content">>)[0-9A-z]+(?=<)")
}
