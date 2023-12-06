package cn.telecom.orm.reactive.thread;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public interface ReactiveTransactionExecutor {
    <T> Flux<T> batchExecute(ReactiveTransactionManager transactionManager,
                             TransactionDefinition transactionDefinition,
                             List<T> tasks,
                             Function<List<T>, Flux<T>> taskCallBack,
                             int bucketSize);

    default <T> Flux<T> executeCallable(PlatformTransactionManager transactionManager,
                                        TransactionDefinition transactionDefinition,
                                        List<Mono<T>> tasks,
                                        int nThread) {
        return executeCallable(transactionManager, transactionDefinition, tasks, nThread, Duration.ZERO);
    }

    <T> Flux<T> executeCallable(PlatformTransactionManager transactionManager,
                                TransactionDefinition transactionDefinition,
                                         List<Mono<T>> tasks,
                                int nThread,
                                Duration maxExecuteTime);


    default CorePublisher<Void> executeRunnable(PlatformTransactionManager transactionManager,
                                                TransactionDefinition transactionDefinition,
                                                List<Mono<Void>> tasks,
                                                int nThread) {
        return executeRunnable(transactionManager, transactionDefinition, tasks, nThread, Duration.ZERO);
    }

    CorePublisher<Void> executeRunnable(PlatformTransactionManager transactionManager,
                                        TransactionDefinition transactionDefinition,
                                        List<Mono<Void>> tasks,
                                        int nThread,
                                        Duration maxExecuteTime);
}
