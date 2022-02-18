package net.fearlessplus.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.sbox.library.extensions.removeEventReceiver
import net.fearlessplus.preferences.AppPreference

abstract class BaseFragment: Fragment() {
    /*
    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            BaseFragment().apply {
                arguments = bundle
            }

        @JvmStatic
        fun getBundle() = bundleOf()
    }
    */


    var parentActivity: BaseActivity? = null
        get() {
            if(activity is BaseActivity)
                return activity as BaseActivity
            return null
        }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.apply {
            AppPreference.localeWrap(this.baseContext, this.applicationContext)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeEventReceiver()
    }


    open fun resultActivity(requestCode: Int, resultCode: Int, data: Intent?) {

    }


}