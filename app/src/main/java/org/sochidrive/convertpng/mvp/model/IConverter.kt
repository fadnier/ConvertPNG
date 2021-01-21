package org.sochidrive.convertpng.mvp.model

import io.reactivex.rxjava3.core.Completable

interface IConverter {
    fun convert(image: Image): Completable
}