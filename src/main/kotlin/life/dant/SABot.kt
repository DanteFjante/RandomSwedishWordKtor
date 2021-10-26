package life.dant

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Ord(val id: Int) {

    private var title: String
    private var lemmaLista: Element

    init {
        require(id >= Companion.MIN_ID)
        require(id <= Companion.MAX_ID)
        val doc = Jsoup.connect("https://svenska.se/so/?id=$id").get()
        title = doc.title()

        val lemmaList = doc.getElementsByClass("lemmalista");

        if (lemmaList.size > 0) {
            lemmaLista = lemmaList.first()!!
        } else {
            lemmaLista = Element("<p id=\"error\">Kunde inte hitta ordet</p>")
        }
    }

    fun getWord() = title.split("|")[0]
    fun getDictionary() = title.split("|")[1]
    fun getLemmelista() = lemmaLista

    companion object {
        const val MIN_ID = 100001;
        const val MAX_ID = 198119;
    }
}

fun addToHtml(html: String, ord: Ord):String
{

    var doc = Jsoup.parse(html)
    doc.title(ord.getWord())


    doc.selectFirst("a.ljudfil")?.remove()
    doc.selectFirst("div#content")?.before(ord.getLemmelista().html())

    var detaljer = doc.getElementsByClass("detaljer").first()

    doc.selectFirst("span.detaljer")?.remove()
    doc.selectFirst("div.lexem")?.before(detaljer)
    doc.select("img").remove()

    println(doc.html())
    return doc.html()
}

tailrec fun trimWhiteSpace(text: String): String {
    if(!text.contains("  "))
        return text;
    return trimWhiteSpace(text.replace("  ", " "))
}

tailrec fun trimExtraLines(text: String): String {
    if(!text.contains("\n\n"))
        return text
    return trimExtraLines(text.replace("\n\n", "\n"))
}