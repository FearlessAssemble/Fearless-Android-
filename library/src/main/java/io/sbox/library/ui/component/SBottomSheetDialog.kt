package io.sbox.library.ui.component

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SBottomSheetDialog: BottomSheetDialogFragment() {

    companion object {
        init {

        }
        var instance: SBottomSheetDialog? = null
            get() {
                if(instance == null) instance = SBottomSheetDialog()
                return instance
            }

    }


    override fun dismiss() {
        super.dismiss()

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }




    var menuRes = ""

    fun show(fragment: Fragment) {
        show(fragment.childFragmentManager, menuRes.toString())
    }

    fun show(activity: AppCompatActivity) {
        show(activity.supportFragmentManager, menuRes.toString())
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, menuRes.toString())
    }
}


/*

                    bottomSheetDialog.behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            trace("@@@@@@@@@@@@@@@@b ", newState, bottomSheet)
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {
                            trace("@@@@@@@@@@@@@@@@a ", slideOffset, bottomSheet)
                        }

                    })

                    /*
                    header_Arrow_Image.setRotation(slideOffset * 180);



                    if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                     */

//                    val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                    var v = layoutInflater.inflate(R.layout.bottom_sheet_dialog_layout, null)
//                    var sheetBehavior = BottomSheetBehavior.from(v)

 */