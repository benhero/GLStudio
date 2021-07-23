package com.benhero.glstudio.util

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * GLSL文件加载工具
 *
 * @author Benhero
 * @date   2021-07-23
 */
object GLSLFileReadUtil {

    fun readFromRaw(context: Context, resourceId: Int): String? {
        val inputStream: InputStream = context.resources.openRawResource(
                resourceId)
        val inputStreamReader = InputStreamReader(
                inputStream)
        val bufferedReader = BufferedReader(
                inputStreamReader)
        var nextLine: String?
        val body = StringBuilder()
        try {
            while (bufferedReader.readLine().also { nextLine = it } != null) {
                body.append(nextLine)
                body.append('\n')
            }
        } catch (e: IOException) {
            return null
        }
        return body.toString()
    }
}