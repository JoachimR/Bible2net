package de.reiss.bible2net.theword2.twdparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object TwdParser {

    private const val xmlLang = "xml:lang"

    private const val thewordfile = "thewordfile"
    private const val bible = "bible"
    private const val head = "head"
    private const val biblename = "biblename"

    private const val theword = "theword"
    private const val date = "date"
    private const val title = "title"

    private const val parol = "parol"
    private const val book = "book"
    private const val chapter = "chapter"
    private const val verse = "verse"
    private const val id = "id"

    private const val intro = "intro"
    private const val text = "text"
    private const val ref = "ref"

    fun parse(string: String): List<TwdItem> =
        Jsoup.parse(string).body().getElementsByTag(thewordfile).let { content ->

            content.attr(bible)?.takeUnless { it.isEmpty() }?.let { bible ->

                content.attr(xmlLang)?.takeUnless { it.isEmpty() }?.let { bibleLanguage ->

                    content.tagName(head)?.first()
                        ?.getElementsByTag(biblename)?.text()
                        ?.takeUnless { it.isEmpty() }?.let { bibleName ->

                            content
                                .flatMap { it.getElementsByTag(theword) }
                                .mapNotNull { element ->
                                    parseItem(element, bible, bibleName, bibleLanguage)
                                }
                        }
                }
            }
        } ?: emptyList()

    private fun parseItem(
        element: Element,
        bible: String,
        bibleName: String,
        bibleLanguage: String
    ) =
        dateFromString(element.attr(date))?.let { date ->
            element.getElementsByTag(title).text()?.let { title ->
                element.getElementsByTag(parol).takeIf { it.size == 2 }?.let { parolElements ->
                    TwdItem(
                        bible = bible,
                        bibleName = bibleName,
                        bibleLanguageCode = bibleLanguage,
                        date = date,
                        title = title,
                        parol1 = createParol(parolElements[0]),
                        parol2 = createParol(parolElements[1])
                    )
                }
            }
        }

    private fun createParol(element: Element) = Parol(
        book = element.attr(book),
        chapter = element.attr(chapter),
        verse = element.attr(verse),
        id = element.attr(id),
        intro = element.getElementsByTag(intro).text(),
        text = element.getElementsByTag(text).html(),
        ref = element.getElementsByTag(ref).text()
    )
}
