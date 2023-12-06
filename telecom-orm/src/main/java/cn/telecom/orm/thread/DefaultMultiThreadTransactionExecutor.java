package cn.telecom.orm.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultMultiThreadTransactionExecutor implements MultiThreadTransactionExecutor {
    private static final Logger log = LoggerFactory.getLogger(DefaultMultiThreadTransactionExecutor.class);

    @Override
    public <T> List<T> executeCallable(PlatformTransactionManager transactionManager, TransactionDefinition transactionDefinition, List<Callable<T>> tasks, int nThread, Duration maxExecuteTime) {
        BlockingQueue<TransactionStatus> transactionStatuses = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Callable<T>> taskQueue = new LinkedBlockingQueue<>(tasks);
        BlockingQueue<T> resultQueue = new LinkedBlockingQueue<>();
        AtomicBoolean errorFlag = new AtomicBoolean(false);
        ExecutorService executorService = Executors.newFixedThreadPool(nThread);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(nThread, () -> {
            if (errorFlag.get()) {
                transactionStatuses.forEach(transactionManager::rollback);
                executorService.shutdownNow();
                countDownLatch.countDown();
            }
            if (taskQueue.isEmpty()) {
                transactionStatuses.forEach(transactionManager::commit);
                executorService.shutdownNow();
                countDownLatch.countDown();
            }
        });
        for (int i = 0; i < nThread; i++) {
            executorService.execute(new CallableTask<>(cyclicBarrier, transactionManager, transactionDefinition, taskQueue, transactionStatuses, resultQueue, errorFlag));
        }
        try {
            long nanos = maxExecuteTime.toNanos();
            if (nanos > 0) {
                boolean await = countDownLatch.await(nanos, TimeUnit.NANOSECONDS);
                if (!await) {
                    throw new RuntimeException(new TimeoutException("executeCallable timeout"));
                }
            } else {
                countDownLatch.await();
            }
        } catch (InterruptedException ignore) {
        }
        if (errorFlag.get()) {
            throw new RuntimeException("executeCallable execute error");
        }
        return resultQueue.stream().toList();
    }

    @Override
    public void executeRunnable(PlatformTransactionManager transactionManager, TransactionDefinition transactionDefinition, List<Runnable> tasks, int nThread, Duration maxExecuteTime) {
        BlockingQueue<TransactionStatus> transactionStatuses = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(tasks);
        AtomicBoolean errorFlag = new AtomicBoolean(false);
        ExecutorService executorService = Executors.newFixedThreadPool(nThread);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(nThread, () -> {
            if (errorFlag.get()) {
                transactionStatuses.forEach(transactionManager::rollback);
                countDownLatch.countDown();
                executorService.shutdownNow();
            }
            if (taskQueue.isEmpty()) {
                transactionStatuses.forEach(transactionManager::commit);
                countDownLatch.countDown();
                executorService.shutdownNow();
            }
        });
        for (int i = 0; i < nThread; i++) {
            executorService.execute(new RunnableTask(cyclicBarrier, transactionManager, transactionDefinition, taskQueue, transactionStatuses, errorFlag));
        }
        try {
            long nanos = maxExecuteTime.toNanos();
            if (nanos > 0) {
                boolean await = countDownLatch.await(nanos, TimeUnit.NANOSECONDS);
                if (!await) {
                    throw new RuntimeException(new TimeoutException("executeRunnable timeout"));
                }
            } else {
                countDownLatch.await();
            }
        } catch (InterruptedException ignore) {
        }
        if (errorFlag.get()) {
            throw new RuntimeException("executeRunnable execute error");
        }
    }


    record CallableTask<T>(CyclicBarrier cyclicBarrier, PlatformTransactionManager transactionManager,
                           TransactionDefinition transactionDefinition, BlockingQueue<Callable<T>> taskQueue,
                           BlockingQueue<TransactionStatus> transactionStatuses,
                           BlockingQueue<T> resultQueue, AtomicBoolean errorFlag) implements Runnable {
        @Override
        public void run() {
            for (; ; ) {
                Callable<T> task = this.taskQueue.poll();
                if (task == null || this.errorFlag.get()) {
                    try {
                        this.cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        Thread.currentThread().interrupt();
                    }
                    return;
                }
                TransactionStatus transaction = this.transactionManager.getTransaction(transactionDefinition);
                try {
                    T call = task.call();
                    this.resultQueue.add(call);
                } catch (Throwable e) {
                    log.error("task execute error: \n", e);
                    this.errorFlag.set(true);
                } finally {
                    try {
                        this.cyclicBarrier.await();
                        this.transactionStatuses.add(transaction);
                    } catch (InterruptedException | BrokenBarrierException ignore) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    record RunnableTask(CyclicBarrier cyclicBarrier, PlatformTransactionManager transactionManager,
                        TransactionDefinition transactionDefinition, BlockingQueue<Runnable> taskQueue,
                        BlockingQueue<TransactionStatus> transactionStatuses,
                        AtomicBoolean errorFlag) implements Runnable {

        @Override
        public void run() {
            for (; ; ) {
                Runnable task = this.taskQueue.poll();
                if (task == null || this.errorFlag.get()) {
                    try {
                        this.cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        Thread.currentThread().interrupt();
                    }
                    return;
                }
                TransactionStatus transaction = this.transactionManager.getTransaction(transactionDefinition);
                try {
                    task.run();
                } catch (Throwable e) {
                    log.error("task execute error: \n", e);
                    this.errorFlag.set(true);
                } finally {
                    try {
                        this.cyclicBarrier.await();
                        this.transactionStatuses.add(transaction);
                    } catch (InterruptedException | BrokenBarrierException ignore) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
