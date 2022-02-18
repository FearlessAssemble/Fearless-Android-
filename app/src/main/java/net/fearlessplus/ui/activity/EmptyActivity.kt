package net.fearlessplus.ui.activity

import android.content.Intent
import android.os.Bundle
import net.fearlessplus.R
import net.fearlessplus.ui.base.BaseActivity
import net.fearlessplus.ui.base.BaseFragment


class EmptyActivity : BaseActivity() {

    companion object {
        const val FRAGMENT = "FRAGMENT"
    }

    var fragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        val bundle = intent.extras
        val frag = Class.forName(bundle?.getString(FRAGMENT).toString())
        try {
            fragment = frag.getMethod("newInstance", Bundle::class.java)?.let {
                it.invoke(null, bundle) as BaseFragment
            }
            commitFragment()
//            return
        } catch (e: Exception) {
            e.printStackTrace()
        }
/*

        try {
            fragment = frag.getMethod("newInstance")?.let {
                it.invoke(null) as BaseFragment
            }

            commitFragment()
            return
        } catch (e: Exception) {
            e.printStackTrace()
        }
*/


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.resultActivity(requestCode, resultCode, data)
    }

    fun commitFragment() {
        fragment?.let {
            val transaction = supportFragmentManager.beginTransaction()
            if (it.isAdded) transaction.show(it).commit()
            else transaction.replace(R.id.empty_container, it).commit()
        }
    }
}