package com.api.base.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class CodeUtils {
    private static long times = 1L;
    private static long lastTime = 0;
    private static final FastDateFormat pattern = FastDateFormat.getInstance("yyyyMMddHHmmss");

    /**
     * @return
     */
    public static synchronized String createUniqueCode() {
        ReferenceQueue<StringBuilder> queue = new ReferenceQueue<StringBuilder>();
        WeakReference<StringBuilder> weakRef = new WeakReference<StringBuilder>(new StringBuilder(25), queue);
            if (null == weakRef.get()) {
                weakRef = new WeakReference<StringBuilder>(new StringBuilder(25), queue);
            }

            Instant instant = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
            long currentTime = instant.getEpochSecond();
            if(lastTime==0) {
                lastTime = instant.getEpochSecond();
            }else{
                if (lastTime!= currentTime){
                    lastTime = currentTime;
                    times = 1L;
                }else{
                    times++;
                }
            }
            weakRef.get().append(pattern.format(Instant.now().toEpochMilli()));
            weakRef.get().append(String.format ( "%04d",times ));
            return weakRef.get().toString();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Set<String> set = new LinkedHashSet<String>();
        FutureTask<String> task = null;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return createUniqueCode();
                }
            };
            task = new FutureTask<String>(callable);
            new Thread(task).start();
            set.add(task.get());
        }
        System.out.println("总共耗时:" + ((System.currentTimeMillis() - startTime)) + "ms");
        System.out.println("*************** " + set.size());

        for (String order:set) {
            System.out.println(order);
        }
    }
}
