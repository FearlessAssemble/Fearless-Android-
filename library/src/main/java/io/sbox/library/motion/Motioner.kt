package io.sbox.library.motion

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.LinearInterpolator


class Motioner {

    companion object {
        fun create() : Motioner {
            return Motioner()
        }


        fun to(target: View, duration: Long, complete: ()->Unit, vararg propertys: Property) {

            var listProperty = arrayListOf<PropertyValuesHolder>()
            for (holder in propertys) {
                holder.value?.let { listProperty.add(PropertyValuesHolder.ofFloat(holder.name, it)) }
            }
            ObjectAnimator.ofPropertyValuesHolder(target, *listProperty.toTypedArray()).apply {
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }
                    override fun onAnimationEnd(animation: Animator?) {
                        complete()
                    }
                    override fun onAnimationCancel(animation: Animator?) {
                    }
                    override fun onAnimationStart(animation: Animator?) {
                    }
                })
                setDuration(duration).interpolator = LinearInterpolator()
                start()
            }
        }

    }






    fun to(target: View, duration: Int, vararg propertys: Property) : Motioner {

        return this
    }

    fun from() : Motioner {

        return this
    }





}


enum class Property {
    x, y, scaleX, scaleY, rotation, alpha, delay;
    var value: Float? = null
    open fun setValue(value: Float) : Property {
        this.value = value
        return this
    }
}




/*
fun reverse rewind


call, animate, property, animate, duration, easing, delay

.to
.from

left: 100,
top: 75,
backgroundColor:"#000",
ease: Power4.easeIn
scaleX: 1.5,
  scaleY: 1.5,

var tl = new TimelineLite;

tl.to("#myDiv", 2, {
  x:100,
  y:75,
  backgroundColor:"#000",
  ease: Power4.easeIn
})
.to ("#myDiv", 1 , {
  scaleX: 1.5,
  scaleY: 1.5,
  backgroundColor: "#454545",
  ease: Back.easeOut.config(1.7)
})
.from("#myCircle", 1, {
  opacity: 0,
});
opacity

//타겟을 0.5초간 회전시킵니다.
TweenLite.to(target, 0.5, { rotation:360 } );

//타겟을 1초동안 두번 뒤집습니다.
TweenLite.to(target, 0.5, { scaleX: -1 } );
TweenLite.to(target, 0.5, { delay:0.5, scaleX: 1, overwrite:false} );



 */