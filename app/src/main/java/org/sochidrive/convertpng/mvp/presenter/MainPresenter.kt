package org.sochidrive.convertpng.mvp.presenter

import moxy.MvpPresenter
import org.sochidrive.convertpng.mvp.view.MainView
import org.sochidrive.convertpng.navigation.Screens
import ru.terrakok.cicerone.Router

class MainPresenter(val router: Router) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(Screens.ConverterScreen())
    }

    fun backClicked() {
        router.exit()
    }
}