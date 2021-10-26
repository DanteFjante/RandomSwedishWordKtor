package life.dant

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

//class? Jag är inte så tekniskt insatt än, men den liknar mer struct till utseendet.
class Ord(val id: Int) {

    private var title: String
    private var lemmaLista: Element

    init {
        //En funktion jag verkligen gillar med kotlin är require(x)
        require(id >= Companion.MIN_ID)
        require(id <= Companion.MAX_ID)

        //Hämtar htmldoc från svenska.se  för att hanteras med Jsoup.
        // Jag har inte använt JSoup innan så det var mycket jag fick lära mig med det här projektet
        val doc = Jsoup.connect("https://svenska.se/so/?id=$id").get()
        title = doc.title()


        //Jag har inte bestämt mig om ? eller x != null är bäst. Men i det här fallet var ju lösningen en if-sats i alla fall
        val lemmaList = doc.getElementsByClass("lemmalista")
        if (lemmaList.size > 0) {
            lemmaLista = lemmaList.first()!!
        } else {
            lemmaLista = Element("<p id=\"error\">Kunde inte hitta ordet</p>")
        }
    }

    //Ingen regex behövdes
    fun getWord() = title.split("|")[0]
    fun getDictionary() = title.split("|")[1]
    fun getLemmalista() = lemmaLista

    //Jag testade mig fram för att hitta minsta och högsta värdet av id för SO (svenska ordlistan). SAOL (Svenska akademiens ordlista) och SAOB (-Ordbok) hade
    // inte integer-id så jag valde att använda SO.
    companion object {
        const val MIN_ID = 100001
        const val MAX_ID = 198119
    }
}

//Den här funktionen är allt från "ren" eller funktionell men jag tror inte
// Jsoup stödjer en funktionell metod till att ändra sina dokument så det fick bli imperativt.
fun addToHtml(html: String, ord: Ord):String
{

    var doc = Jsoup.parse(html)
    doc.title(ord.getWord())


    //Här försöker jag få bort en länk till ljudfil men det går sådär.
    doc.selectFirst("a.ljudfil")?.remove()

    //content har jag endast som en flagga för att kunna hitta vart svenska.se's markup ska hamna.
    doc.selectFirst("div#content")?.before(ord.getLemmalista().html())

    //Jag behövde flytta detaljer till lexem för att de ska synas. Annars är de dolda av en "visa-mer" som inte fungerar.
    var detaljer = doc.getElementsByClass("detaljer").first()

    //Den gamla detaljer. Den syns ändå inte men kan ju försöka ta bort den.
    // Koden är inte jättetestad eftersom jag försökte fixa allt i hast för att skicka vidare till er så jag vet inte hur användbar den här linjen är.
    doc.selectFirst("span.detaljer")?.remove()
    doc.selectFirst("div.lexem")?.before(detaljer)

    //Bilder funkar inte med min lösning så jag tar bort alla.
    doc.select("img").remove()

    println(doc.html())
    return doc.html()
}


//Gammal lösning jag använde innan jag implementerade jsoup. Det var därifrån regexen kom in. Jag tänkte att jag låter den ligga av samma anledning.
tailrec fun trimWhiteSpace(text: String): String {
    if(!text.contains("  "))
        return text
    return trimWhiteSpace(text.replace("  ", " "))
}
//Också gammal lösning
tailrec fun trimExtraLines(text: String): String {
    if(!text.contains("\n\n"))
        return text
    return trimExtraLines(text.replace("\n\n", "\n"))
}