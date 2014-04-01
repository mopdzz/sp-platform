package com.sp.platform.timer;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimerMain extends Thread {
    private static final Logger logger = Logger.getLogger(TimerMain.class);

    private List<AbstractBaseTimer> list;
    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(10);

    private static AtomicBoolean isExecFlag = new AtomicBoolean(false);
    private long initialDelay = 1000;
    private long period = 60000;

    public TimerMain() {

    }

    public TimerMain(List<AbstractBaseTimer> list) {
        this.list = list;
    }

    public void run() {

        Runnable task = new Runnable() {
            public void run() {
                update();
            }
        };

        scheduler.scheduleAtFixedRate(task, initialDelay, period,
                TimeUnit.MILLISECONDS);
    }

    private void update() {
        try {
            if (list != null) {
                if (isExecFlag.get()) {
                    logger.info("上次任务未结束，当前任务跳出...");
                    return;
                }
                isExecFlag.set(true);
                List<Future<FutureBody>> futures = scheduler.invokeAll(list);
                for (Future<FutureBody> future : futures) {
                    FutureBody futureBody = future.get();
                    if (futureBody.isFlag()) {
                        logger.debug(futureBody.getName() + "--->执行同步成功...");
                    } else {
                        logger.error(futureBody.getName() + "--->执行同步失败, 错误："
                                + futureBody.getErrorMessage());
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            isExecFlag.set(false);
        }
    }

    public List<AbstractBaseTimer> getList() {
        return list;
    }

    public void setList(List<AbstractBaseTimer> list) {
        this.list = list;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
