package com.example.bytedance_demo

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author: yyf
 * @date: 2020/11/20
 * @desc:
 */
class WorkerLife(
    private val interval: Long,
    max: Int = Runtime.getRuntime().availableProcessors()
) {

    private val es: ExecutorService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Executors.newFixedThreadPool(max)
    }

    private val worker by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Worker()
    }

    fun push(runnable: Runnable) {
        worker.tryWork(runnable)
    }

    private inner class Worker {

        private var work: Runnable? = null

        @Synchronized
        fun tryWork(runnable: Runnable) {
            work = runnable
            wakekUp()
        }

        @Volatile private var thread: Thread? = null

        private fun wakekUp() {
            if (thread != null) {
                return
            }
            synchronized(Worker::class.java) {
                if (thread != null) {
                    return
                }
                thread = Thread {
                    Thread.sleep(interval)
                    doWork()
                    thread = null
                }
                thread?.start()
            }
        }

        private fun doWork() {
            if (work != null && !es.isShutdown) {
                es.execute(work!!)
            }
        }
    }

    /** 罢工 */
    @Synchronized
    fun shutdown() = es.shutdown()
}