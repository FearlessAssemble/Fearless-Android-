package net.fearlessplus.model.item

import android.content.Context
import android.view.View
import net.fearlessplus.extensions.newFragment
import net.fearlessplus.ui.webview.WebViewFragment


data class ChartItem(
    var id: Int,
    var no: Int,
    var name: String,
    var title: String,
    var writeDate: String,
    var dcUrl: String,

    /** custom **/
    var isCustom: Boolean = false

) {


    fun onClick(context: Context, view: View?) {

        dcUrl?.let {
//            context.browse(it)

            newFragment<WebViewFragment>(
                WebViewFragment.getBundle(
                    it,
                    title, true
                )
            )
        }

    }

}