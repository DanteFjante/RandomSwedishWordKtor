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


    //val regurl = Regex("(?<=\")http[s]?:\\/\\/.*?(?=\")")
    //val regcontent = Regex("(?<=>)[0-9A-z]+(?=<)")
    //val regcontent = Regex("(?<=<div id="content">>)[0-9A-z]+(?=<)")
}
