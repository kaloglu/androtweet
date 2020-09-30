@file:JvmName("KtxUtilLocal")

package com.zsk.androtweet.utils.extensions

import com.kaloglu.library.ktx.isNotNullOrEmpty

//
// Created by  on 30.09.2020.
//

fun <TargetItem : Any, SourceItem : Any> TargetItem.findAndUpdate(
        sourceCollection: Collection<SourceItem>,
        update: (TargetItem, SourceItem) -> Unit
): TargetItem {
    sourceCollection.firstOrNull { this == it }?.let { source -> update(this, source) }
    return this
}

fun <TargetItem : Any, SourceItem : Any> Collection<TargetItem>.updateBy(
        sourceCollection: List<SourceItem>,
        update: (TargetItem, SourceItem) -> Unit
) = takeIf(Collection<TargetItem>::isNotNullOrEmpty)
        ?.map {
            it.findAndUpdate(sourceCollection) { target, source ->
                update(target, source)
            }
        }?: listOf()
