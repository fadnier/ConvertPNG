package org.sochidrive.convertpng.navigation

import org.sochidrive.convertpng.ui.fragment.ConvertFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class ConverterScreen() : SupportAppScreen() {
        override fun getFragment() = ConvertFragment.newInstance()
    }
}