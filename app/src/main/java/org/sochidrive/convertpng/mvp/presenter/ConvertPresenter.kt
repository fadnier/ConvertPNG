package org.sochidrive.convertpng.mvp.presenter

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import org.sochidrive.convertpng.mvp.model.IConverter
import org.sochidrive.convertpng.mvp.model.Image
import org.sochidrive.convertpng.mvp.view.ConverterView
import ru.terrakok.cicerone.Router

class ConvertPresenter(val mainThreadScheduler: Scheduler, val router: Router, val converter: IConverter) : MvpPresenter<ConverterView>() {

    override fun onFirstViewAttach() {
        viewState.init()
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    fun convertClick() {
        viewState.pickImage()
    }



    var conversionDisposable: Disposable? = null
    fun imageSelected(image: Image) {
        viewState.showConvertInProgress()
        conversionDisposable = converter.convert(image)
            .observeOn(mainThreadScheduler)
            .subscribe({
                viewState.hideConvertInProgress()
                viewState.showConvertSuccess()
            },{
                viewState.hideConvertInProgress()
                viewState.showConvertError()
            })
    }

    fun convertCancel() {
        conversionDisposable?.dispose()
        viewState.hideConvertInProgress()
        viewState.showConvertCancel()
    }
}