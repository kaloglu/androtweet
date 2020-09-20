/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.zsk.androtweet.remote

import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterSession
import okhttp3.OkHttpClient

/**
 * A class to allow authenticated access to Twitter API endpoints.
 * Can be extended to provided additional endpoints by extending and providing Retrofit API
 * interfaces to [TwitterApiClient.getService]
 */
class CustomApiClient : TwitterApiClient {

    constructor(client: OkHttpClient?) : super(client)
    constructor(session: TwitterSession?, client: OkHttpClient?) : super(session, client)

    fun getCustomStatusesService() = getService(CustomStatusesService::class.java)
}