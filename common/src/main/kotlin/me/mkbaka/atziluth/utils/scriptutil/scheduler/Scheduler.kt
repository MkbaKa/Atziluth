package me.mkbaka.atziluth.utils.scriptutil.scheduler

import me.mkbaka.atziluth.api.event.AtziluthReloadEvent
import me.mkbaka.atziluth.api.event.ReloadStatus
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import java.util.concurrent.ConcurrentHashMap

/**
 * 创建一个自定义调度器
 * 同源会覆盖旧调度器执行新调度器
 * @param source 源
 */
class Scheduler(val source: String) {

    private var delay: Long = 0

    private var period: Long = 0

    private var isAsync = false

    private var executor: (Long) -> Unit = {}

    fun setDelay(delay: Long): Scheduler {
        this.delay = delay
        return this
    }

    fun setPeriod(period: Long): Scheduler {
        this.period = period
        return this
    }

    fun setAsync(async: Boolean): Scheduler {
        this.isAsync = async
        return this
    }

    fun setExecutor(exec: (Long) -> Unit): Scheduler {
        this.executor = exec
        return this
    }

    fun start() {
        submitScheduler(this)
    }

    fun cancel() {
        cancelScheduler(this)
    }

    companion object {

        private val schedulers = ConcurrentHashMap<String, PlatformExecutor.PlatformTask>()

        fun submitScheduler(scheduler: Scheduler) {
            schedulers.compute(scheduler.source) { _, oldTask ->
                oldTask?.cancel()
                var time: Long = 0
                submit(async = scheduler.isAsync, delay = scheduler.delay, period = scheduler.period) {
                    scheduler.executor(++time)
                }
            }
        }

        fun cancelScheduler(scheduler: Scheduler) {
            cancelScheduler(scheduler.source)
        }

        fun cancelScheduler(source: String) {
            schedulers[source]?.cancel()
            schedulers.remove(source)
        }

        @SubscribeEvent
        fun reload(e: AtziluthReloadEvent) {
            if (e.status == ReloadStatus.PRE) {
                schedulers.forEach { (_, task) ->
                    task.cancel()
                }
                schedulers.clear()
            }
        }

    }

}