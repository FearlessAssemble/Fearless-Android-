package net.fearlessplus.utils

import android.os.CountDownTimer

class TimerManager private constructor() {
    companion object {
        var instance: TimerManager? = null

        init {
            instance = TimerManager()
        }
    }



    fun create(): TimerObject {
        return TimerObject(1000, 0, 0)
    }

    fun start(period: Long): TimerObject {
        val obj = TimerObject(period, 0, 0)
        return obj.start()
    }

    fun start(period: Long, counnt: Int): TimerObject {
        val obj = TimerObject(period, 0, counnt)
        return obj.start()
    }

    inner class TimerObject(
        duration: Long,
        delay: Long,
        count: Int
    ) {
        private var listener: TimerListener? = TimerListener()
        private var delay: Long = 0
        private var duration: Long = 1000
        private var currentCount = 0
        private var completeCount = 1
        private var isOneShot = false
        /*
		private TimerTask mTimerTask;
		private final Handler handler = new Handler();
		private Timer timer = new Timer();
		*/
        private var timer: CountDownTimer? = null
        var isRunning = false
        fun setListener(listener: TimerListener): TimerObject {
            this.listener = listener
            return this
        }

        fun setOneShot(isOneShot: Boolean): TimerObject {
            this.isOneShot = isOneShot
            return this
        }

        fun setDelay(delay: Long): TimerObject {
            this.delay = delay
            return this
        }

        fun setDuration(duration: Long): TimerObject {
            this.duration = duration
            return this
        }

        fun setCount(count: Int): TimerObject {
            completeCount = count
            return this
        }

        private fun initTimer() { /*
			if (mTimerTask != null) {
				currentCount = 0;
				isRunning = false;
				mTimerTask.cancel();
				mTimerTask = null;
			}
			*/
            if (timer != null) {
                isRunning = false
                timer!!.cancel()
                timer = null
                currentCount = 0
            }
        }

        fun start(period: Long): TimerObject {
            setDuration(period)
            return start()
        }

        fun start(period: Long, count: Int): TimerObject {
            setDuration(period)
            setCount(count)
            return start()
        }

        fun start(): TimerObject {
            initTimer()
            isRunning = true
            /*
			mTimerTask = new TimerTask() {
				public void run() {
					handler.post(new Runnable() {
						public void run() {
							if(!isRunning) return;
							currentCount++;
							listener.onUpdate(currentCount);
							if (currentCount >= completeCount && completeCount > 0 ) {
								stop();
							}
						}
					});
				}
			};
			timer.schedule(mTimerTask, delay, period);
			*/if (completeCount <= 0 || completeCount > duration) {
                completeCount = 1
            }
            try {
                val interval = duration / completeCount
                timer = object : CountDownTimer(duration, interval) {
                    override fun onTick(millisUntilFinished: Long) {
                        currentCount++
                        if (listener != null && currentCount < completeCount) listener!!.onUpdate(
                            currentCount
                        )
                    }

                    override fun onFinish() {
                        stop()
                    }
                }
                (timer as CountDownTimer).start()
            } catch (e: NoClassDefFoundError) {
                e.printStackTrace()
            }
            return this
        }

        @JvmOverloads
        fun stop(isCompleteListener: Boolean = true): TimerObject {
            initTimer()
            if (listener != null && isCompleteListener) {
                listener!!.onComplete(completeCount)
                if (isOneShot) destroy()
            }
            return this
        }

        fun destroy() {
            try {
                initTimer()
                listener = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        init {
            setDuration(duration)
            setDelay(delay)
            setCount(count)
        }
    }

    init {
        if (instance != null) throw Error("Singleton double-instantiation, should never happen!")
    }
}

open class TimerListener {
    fun onUpdate(count: Int) {}
    open fun onComplete(count: Int) {}
}