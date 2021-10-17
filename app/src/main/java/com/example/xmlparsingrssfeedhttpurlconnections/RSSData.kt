package com.example.xmlparsingrssfeedhttpurlconnections

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class RSSData(val title: String, val link: String, val description: String, val enclosure : String, val pubDate: String)

class XMLParser {
    //////////////////////////////////////////

    private val ns: String? = null

    fun parse(inputStream: InputStream): List<RSSData> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readDataRssFeed(parser)
        }
    }

    private fun readDataRssFeed(parser: XmlPullParser): List<RSSData> {

        val data = mutableListOf<RSSData>()

        parser.require(XmlPullParser.START_TAG, ns, "rss")


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "channel") {
                parser.require(XmlPullParser.START_TAG, ns, "channel")
                while (parser.next() != XmlPullParser.END_TAG) {

                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }

                    if (parser.name == "item") {
                        parser.require(XmlPullParser.START_TAG, ns, "item")
                        var link: String? = null
                        var title: String? = null
                        var description: String? = null
                        var pubDate: String? = null
                        var enclosure: String? = null

                        while (parser.next() != XmlPullParser.END_TAG) {
                            if (parser.eventType != XmlPullParser.START_TAG) {
                                continue
                            }
                            when (parser.name) {
                                "title" -> title = read(parser, "title")
                                "link" -> link = read(parser, "link")
                                "description" -> description = read(parser, "description")
                                "enclosure" -> {
                                    enclosure = parser.getAttributeValue(null, "url").replace("http", "https")
                                    skip(parser)
                                }
                                "pubDate" -> pubDate = read(parser, "pubDate")
                                else -> skip(parser)
                            }
                        }
                        data.add(RSSData(title.toString(), link.toString(), description.toString(), enclosure.toString(), pubDate.toString().split(" ED")[0]))
                    } else {
                        skip(parser)
                    }
                }
            } else {
                skip(parser)
            }
        }
        return data
    }

    ///////////////////////////////////////

    private fun read(parser: XmlPullParser, str: String): String {

        val res = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, str)
        return res
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}