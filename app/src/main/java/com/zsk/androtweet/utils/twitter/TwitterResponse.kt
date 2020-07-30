package com.zsk.androtweet.utils.twitter

import com.twitter.sdk.android.core.Result
import java.util.regex.Pattern

@Suppress("unused") // T is used in extending classes
sealed class TwitterResponse<T> {
    companion object {
        fun <T> create(error: Throwable): Error<T> = Error(error.message ?: "unknown error")

        fun <T> create(result: Result<T>): TwitterResponse<T> {
            return with(result.response) {
                when {
                    isSuccessful -> {
                        when {
                            code() == 204 -> Empty()
                            else -> {
                                val body = result.data ?: return Empty()
                                val headers = headers()

                                Success(body, headers?.get("link"))
                            }
                        }
                    }
                    else -> {
                        val msg = errorBody()?.string()
                        Error(when {
                            msg.isNullOrEmpty() -> message()
                            else -> msg
                        })
                    }
                }
            }
        }

    }

    /**
     * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
     */
    class Empty<T> : TwitterResponse<T>()

    data class Success<T>(
            val body: T,
            val links: Map<String, String>
    ) : TwitterResponse<T>() {
        constructor(body: T, linkHeader: String?) : this(
                body = body,
                links = linkHeader?.extractLinks() ?: emptyMap()
        )

        val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
            links[NEXT_LINK]?.let { next ->
                val matcher = PAGE_PATTERN.matcher(next)
                if (!matcher.find() || matcher.groupCount() != 1) {
                    null
                } else {
                    matcher.group(1)?.let {
                        try {
                            Integer.parseInt(it)
                        } catch (ex: NumberFormatException) {
                            null
                        }
                    }
                }
            }
        }

        companion object {
            private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
            private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
            private const val NEXT_LINK = "next"

            private fun String.extractLinks(): Map<String, String> {
                val links = mutableMapOf<String, String>()
                val matcher = LINK_PATTERN.matcher(this)

                while (matcher.find()) {
                    val count = matcher.groupCount()
                    if (count == 2) {
                        matcher.group(2)?.let { second ->
                            matcher.group(1)?.let { first -> links[second] = first }
                        }
                    }
                }
                return links
            }

        }
    }

    data class Error<T>(val message: String) : TwitterResponse<T>()
}
