package de.reiss.bible2net.theword.twdparser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Calendar

@Suppress("FunctionName", "IllegalIdentifier")
class ParserTest {

    @Test
    fun `when file content corrupt return empty list`() {
        val rawString = readTwd("corrupt_file.twd")
        val parsed = TwdParser.parse(rawString)

        assertNotNull(parsed)
        assertTrue(parsed.isEmpty())
    }

    @Test
    fun `parse af_AfrikaanseNuweVertaling_2017`() {
        check(
            fileName = "af_AfrikaanseNuweVertaling_2017.twd",
            bible = "AfrikaanseNuweVertaling",
            bibleName = "Bybel in Afrikaans (1983-vertaling)",
            languageCode = "af",
            year = 2017
        )
    }

    @Test
    fun `parse ar_NewArabicVersionKetabElHayat_2017`() {
        check(
            fileName = "ar_NewArabicVersionKetabElHayat_2017.twd",
            bible = "NewArabicVersionKetabElHayat",
            bibleName = "New Arabic Version (Ketab el Hayat)",
            languageCode = "ar",
            year = 2017
        )
    }

    @Test
    fun `parse da_Bibelen_2017`() {
        check(
            fileName = "da_Bibelen_2017.twd",
            bible = "Bibelen",
            bibleName = "Bibelen",
            languageCode = "da",
            year = 2017
        )
    }

    @Test
    fun `parse de_HoffnungFuerAlle_2017`() {
        check(
            fileName = "de_HoffnungFuerAlle_2017.twd",
            bible = "HoffnungFuerAlle",
            bibleName = "Hoffnung für Alle",
            languageCode = "de",
            year = 2017
        )
    }

    @Test
    fun `parse de_LeonbergerBibel_2017`() {
        check(
            fileName = "de_LeonbergerBibel_2017.twd",
            bible = "LeonbergerBibel",
            bibleName = "Leonberger Bibel",
            languageCode = "de",
            year = 2017
        )
    }

    @Test
    fun `parse de_NeueEvangelistischeUebersetzung_2017`() {
        check(
            fileName = "de_NeueEvangelistischeUebersetzung_2017.twd",
            bible = "NeueEvangelistischeUebersetzung",
            bibleName = "Neue Evangelistische Übersetzung",
            languageCode = "de",
            year = 2017
        )
    }

    @Test
    fun `parse de_Schlachter2000_2018`() {
        check(
            fileName = "de_Schlachter2000_2018.twd",
            bible = "Schlachter2000",
            bibleName = "Schlachter 2000",
            languageCode = "de",
            year = 2018
        )
    }

    @Test
    fun `parse en_EnglishStandardVersion_2018`() {
        check(
            fileName = "en_EnglishStandardVersion_2018.twd",
            bible = "EnglishStandardVersion",
            bibleName = "English Standard Version",
            languageCode = "en",
            year = 2018
        )
    }

    @Test
    fun `parse es_ReinaValera1995_2017`() {
        check(
            fileName = "es_ReinaValera1995_2017.twd",
            bible = "ReinaValera1995",
            bibleName = "Reina-Valera 1995",
            languageCode = "es",
            year = 2017
        )
    }

    @Test
    fun `parse fr_Segond21_2017`() {
        check(
            fileName = "fr_Segond21_2017.twd",
            bible = "Segond21",
            bibleName = "Segond 21",
            languageCode = "fr",
            year = 2017
        )
    }

    @Test
    fun `parse ga_AnBioblaNaofa1981_2017`() {
        check(
            fileName = "ga_AnBioblaNaofa1981_2017.twd",
            bible = "AnBioblaNaofa1981",
            bibleName = "An Bíobla Naofa 1981",
            languageCode = "ga",
            year = 2017
        )
    }

    @Test
    fun `parse he_ModernHebrew2004_2017`() {
        check(
            fileName = "he_ModernHebrew2004_2017.twd",
            bible = "ModernHebrew2004",
            bibleName = "ספר הבריתות 2004",
            languageCode = "he",
            year = 2017
        )
    }

    @Test
    fun `parse hu_Karoli1990_2017`() {
        check(
            fileName = "hu_Karoli1990_2017.twd",
            bible = "Karoli1990",
            bibleName = "Karoli 1990",
            languageCode = "hu",
            year = 2017
        )
    }

    @Test
    fun `parse pl_BibliaTysiaclecia_2017`() {
        check(
            fileName = "pl_BibliaTysiaclecia_2017.twd",
            bible = "BibliaTysiaclecia",
            bibleName = "Biblia Tysiąclecia",
            languageCode = "pl",
            year = 2017
        )
    }

    @Test
    fun `parse ru_JubilaeumsBibel_2017`() {
        check(
            fileName = "ru_JubilaeumsBibel_2017.twd",
            bible = "JubilaeumsBibel",
            bibleName = "Юбилейная Библия",
            languageCode = "ru",
            year = 2017
        )
    }

    @Test
    fun `parse swg_BibelFuerSchwoba_2017`() {
        check(
            fileName = "swg_BibelFuerSchwoba_2017.twd",
            bible = "BibelFuerSchwoba",
            bibleName = "Bibel für Schwoba",
            languageCode = "swg",
            year = 2017
        )
    }

    @Test
    fun `parse th_ThaiHolyBible1971_2017`() {
        check(
            fileName = "th_ThaiHolyBible1971_2017.twd",
            bible = "ThaiHolyBible1971",
            bibleName = "Thai Holy Bible 1971",
            languageCode = "th",
            year = 2017
        )
    }

    @Test
    fun `parse tr_KutsalKitap2001_2017`() {
        check(
            fileName = "tr_KutsalKitap2001_2017.twd",
            bible = "KutsalKitap2001",
            bibleName = "Kutsal Kitap 2001",
            languageCode = "tr",
            year = 2017
        )
    }

    @Test
    fun `parse ur_UrduGeoVersion_2017`() {
        check(
            fileName = "ur_UrduGeoVersion_2017.twd",
            bible = "UrduGeoVersion",
            bibleName = "Urdu Geo Version",
            languageCode = "ur",
            year = 2017
        )
    }

    @Test
    fun `parse uz_Cyrl_OzbektilidagiMuqaddasKitob2012Cyrillic_2017`() {
        check(
            fileName = "uz-Cyrl_OzbektilidagiMuqaddasKitob2012Cyrillic_2017.twd",
            bible = "OzbektilidagiMuqaddasKitob2012Cyrillic",
            bibleName = "Ўзбек тилидаги Муқаддас Китоб 2012",
            languageCode = "uz-Cyrl",
            year = 2017
        )
    }

    @Test
    fun `parse uz_Latn_OzbektilidagiMuqaddasKitob2012Latin_2017`() {
        check(
            fileName = "uz-Latn_OzbektilidagiMuqaddasKitob2012Latin_2017.twd",
            bible = "OzbektilidagiMuqaddasKitob2012Latin",
            bibleName = "O‘zbek tilidagi Muqaddas Kitob 2012",
            languageCode = "uz-Latn",
            year = 2017
        )
    }

    @Test
    fun `parse zh_Hans_ChineseUnionVersionSimplified_2017`() {
        check(
            fileName = "zh-Hans_ChineseUnionVersionSimplified_2017.twd",
            bible = "ChineseUnionVersionSimplified",
            bibleName = "Chinese Union Version (Simplified Chinese)",
            languageCode = "zh-Hans",
            year = 2017
        )
    }

    private fun check(
        fileName: String,
        bible: String,
        bibleName: String,
        languageCode: String,
        year: Int
    ) {

        val rawString = readTwd(fileName)
        val parsed = TwdParser.parse(rawString)

        assertEquals(365, parsed.size)

        for ((index, twdItem) in parsed.withIndex()) {
            assertEquals(bible, twdItem.bible)
            assertEquals(bibleName, twdItem.bibleName)
            assertEquals(languageCode, twdItem.bibleLanguageCode)

            checkItem(index, twdItem, year)
        }
    }

    private fun checkItem(index: Int, twdItem: TwdItem, expectedYear: Int) {
        with(twdItem) {
            assertEquals(expectedDate(year = expectedYear, dayOfYear = index + 1), date)

            assertFalse(title.isEmpty())

            checkParol(parol1)
            checkParol(parol2)
        }
    }

    private fun checkParol(parol: Parol) {
        with(parol) {
            assertFalse(book.isEmpty())
            assertFalse(chapter.isEmpty())
            assertFalse(verse.isEmpty())
            assertFalse(id.isEmpty())
            assertFalse(text.isEmpty())
            assertFalse(ref.isEmpty())
        }
    }

    private fun expectedDate(year: Int, dayOfYear: Int) =
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.DAY_OF_YEAR, dayOfYear)

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

    private fun readTwd(xmlFileName: String): String {
        val reader = BufferedReader(
            InputStreamReader(
                javaClass.classLoader!!.getResourceAsStream(xmlFileName)
            )
        )

        val total = StringBuilder()

        var line = reader.readLine()
        while (line != null) {
            total.append(line).append('\n')
            line = reader.readLine()
        }
        return total.toString()
    }
}
