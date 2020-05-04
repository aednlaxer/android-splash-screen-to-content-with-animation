package com.example.splashscreendemo

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.annotation.DrawableRes
import org.xmlpull.v1.XmlPullParser

data class ParsedVectorDrawable(
    val width: Float,
    val height: Float,
    val viewportWidth: Float,
    val viewportHeight: Float,
    val pathData: String
)

object VectorDrawableParser {

    private val digitsOnly = Regex("[^0-9.]")

    @SuppressLint("ResourceType")
    fun parsedVectorDrawable(
        resources: Resources,
        @DrawableRes drawable: Int
    ): ParsedVectorDrawable? {
        var pathData: String? = null
        var width: Float? = null
        var height: Float? = null
        var viewportWidth: Float? = null
        var viewportHeight: Float? = null

        // This is very simple parser, it doesn't support <group> tag, nested tags and other stuff
        resources.getXml(drawable).use { xml ->
            var event = xml.eventType
            while (event != XmlPullParser.END_DOCUMENT) {

                if (event != XmlPullParser.START_TAG) {
                    event = xml.next()
                    continue
                }

                when (xml.name) {
                    "vector" -> {
                        width = xml.getAttributeValue(getAttrPosition(xml, "width"))
                            .replace(digitsOnly, "")
                            .toFloatOrNull()
                        height = xml.getAttributeValue(getAttrPosition(xml, "height"))
                            .replace(digitsOnly, "")
                            .toFloatOrNull()
                        viewportWidth = xml.getAttributeValue(getAttrPosition(xml, "viewportWidth"))
                            .toFloatOrNull()
                        viewportHeight =
                            xml.getAttributeValue(getAttrPosition(xml, "viewportHeight"))
                                .toFloatOrNull()
                    }
                    "path" -> {
                        pathData = xml.getAttributeValue(getAttrPosition(xml, "pathData"))
                    }
                }

                event = xml.next()
            }
        }

        return ParsedVectorDrawable(
            width ?: return null,
            height ?: return null,
            viewportWidth ?: return null,
            viewportHeight ?: return null,
            pathData ?: return null
        )
    }

    private fun getAttrPosition(xml: XmlPullParser, attrName: String): Int =
        (0 until xml.attributeCount)
            .firstOrNull { i -> xml.getAttributeName(i) == attrName }
            ?: -1
}
