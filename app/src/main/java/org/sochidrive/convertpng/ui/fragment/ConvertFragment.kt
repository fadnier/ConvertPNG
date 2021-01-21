package org.sochidrive.convertpng.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_convert.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.sochidrive.convertpng.App
import org.sochidrive.convertpng.mvp.model.Image
import org.sochidrive.convertpng.mvp.presenter.ConvertPresenter
import org.sochidrive.convertpng.mvp.view.ConverterView
import org.sochidrive.convertpng.ui.BackButtonListener
import org.sochidrive.convertpng.ui.Converter
import org.sochidrive.convertpng.R

class ConvertFragment: MvpAppCompatFragment(), ConverterView, BackButtonListener {
    companion object {
        private const val PICK_IMAGE_REQUEST_ID = 1
        fun newInstance() = ConvertFragment()
    }

    val presenter: ConvertPresenter by moxyPresenter { ConvertPresenter(AndroidSchedulers.mainThread(), App.instance.router, Converter(context)) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = View.inflate(context, R.layout.fragment_convert, null)

    override fun init() {
        btn_convert.setOnClickListener {
            presenter.convertClick()
        }
    }

    override fun pickImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(Intent.createChooser(intent, R.string.select_image.toString()), PICK_IMAGE_REQUEST_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                data?.data?.let {uri ->
                    val bytes = context?.contentResolver?.openInputStream(uri)?.buffered()?.use { it.readBytes() }
                    bytes?.let {
                        presenter.imageSelected(Image(bytes))
                    }
                }
            }
        }
    }

    var convertInProgressDialog: Dialog? = null
    override fun showConvertInProgress() {
        context?.let {
            convertInProgressDialog = AlertDialog.Builder(it)
                    .setMessage(R.string.convert_in_progress)
                    .setNegativeButton(R.string.cancel) { dialog, which ->  presenter.convertCancel() }
                    .create()
            convertInProgressDialog?.show()
        }
    }

    override fun hideConvertInProgress() {
        convertInProgressDialog?.dismiss()
    }

    override fun showConvertSuccess() {
        Toast.makeText(context, R.string.convertation_success, Toast.LENGTH_SHORT).show()
    }

    override fun showConvertCancel() {
        Toast.makeText(context, R.string.convertation_cancel, Toast.LENGTH_SHORT).show()
    }

    override fun showConvertError() {
        Toast.makeText(context, R.string.convertation_error, Toast.LENGTH_SHORT).show()
    }

    override fun backPressed() = presenter.backPressed()
}