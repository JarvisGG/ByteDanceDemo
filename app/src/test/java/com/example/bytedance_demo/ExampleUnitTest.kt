package com.example.bytedance_demo

import org.junit.Test

import org.junit.Assert.*
import java.lang.RuntimeException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_worker() {
        val life = WorkerLife(1000)
        Thread {
            Thread.sleep(800)
            life.push(Runnable {
                print("work-0 run on Thread: ${Thread.currentThread().name}.\n")
                Thread.sleep(800)
            })
        }.start()

        Thread {
            Thread.sleep(800)
            life.push(Runnable {
                print("work-1 run on Thread: ${Thread.currentThread().name}.\n")
                Thread.sleep(800)
            })
        }.start()

        Thread {
            Thread.sleep(1200)
            life.push(Runnable {
                print("work-2 run on Thread: ${Thread.currentThread().name}.\n")
                Thread.sleep(800)
            })
        }.start()

        Thread {
            Thread.sleep(1000)
            life.push(Runnable {
                print("work-3 run on Thread: ${Thread.currentThread().name}.\n")
                Thread.sleep(800)
            })
        }.start()

        Thread {
            Thread.sleep(2000)
            life.push(Runnable {
                print("work-4 run on Thread: ${Thread.currentThread().name}.\n")
                Thread.sleep(800)
            })
        }.start()

        Thread {
            Thread.sleep(2001)
            life.push(Runnable {
                print("work-5 run on Thread: ${Thread.currentThread().name}.\n")
                Thread.sleep(800)
            })
        }.start()

        Thread {
            Thread.sleep(4000)
            life.push(Runnable {
                print("work-6 run on Thread: ${Thread.currentThread().name}.\n")
                Thread.sleep(800)
            })
        }.start()

        Thread {
            Thread.sleep(4100)
            life.push(Runnable {
                print("work-7 run on Thread: ${Thread.currentThread().name}.\n")
                Thread.sleep(800)
            })
        }.start()

        Thread {
            Thread.sleep(6100)
            life.push(Runnable {
                print("work-8 run on Thread: ${Thread.currentThread().name}.\n")
                throw RuntimeException("error!")
            })
        }.start()

        Thread {
            Thread.sleep(8800)
            life.push(Runnable {
                print("work-9 run on Thread: ${Thread.currentThread().name}.\n")
                Thread.sleep(800)
            })
        }.start()


//        Thread.sleep(5000)
//
//        life.shutdown()

        Thread.sleep(20000)
    }
}