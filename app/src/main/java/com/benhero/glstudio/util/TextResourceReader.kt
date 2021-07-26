/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 */
package com.benhero.glstudio.util

import android.content.Context
import android.content.res.Resources
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 文本文件加载工具
 *
 * @author Benhero
 * @date   2021-07-23
 */
object TextResourceReader {

    fun readTextFileFromRaw(context: Context, resourceId: Int): String {
        val body = StringBuilder()
        try {
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            while (true) {
                val nextLine = bufferedReader.readLine() ?: break
                body.append(nextLine)
                body.append('\n')
            }
        } catch (e: IOException) {
            throw RuntimeException(
                "Could not open resource: $resourceId", e
            )
        } catch (nfe: Resources.NotFoundException) {
            throw RuntimeException("Resource not found: $resourceId", nfe)
        }

        return body.toString()
    }
}
