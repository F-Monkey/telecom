package cn.telecom.orm.thread;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;

public interface MultiThreadTransactionExecutor {

    default <T> List<T> executeCallable(PlatformTransactionManager transactionManager,
                                        TransactionDefinition transactionDefinition,
                                        List<Callable<T>> tasks,
                                        int nThread) {
        return executeCallable(transactionManager, transactionDefinition, tasks, nThread, Duration.ZERO);
    }

    <T> List<T> executeCallable(PlatformTransactionManager transactionManager,
                                TransactionDefinition transactionDefinition,
                                List<Callable<T>> tasks,
                                int nThread,
                                Duration maxExecuteTime);


    default void executeRunnable(PlatformTransactionManager transactionManager,
                                 TransactionDefinition transactionDefinition,
                                 List<Runnable> tasks,
                                 int nThread) {
        executeRunnable(transactionManager, transactionDefinition, tasks, nThread, Duration.ZERO);
    }

    void executeRunnable(PlatformTransactionManager transactionManager,
                         TransactionDefinition transactionDefinition,
                         List<Runnable> tasks,
                         int nThread,
                         Duration maxExecuteTime);
}
