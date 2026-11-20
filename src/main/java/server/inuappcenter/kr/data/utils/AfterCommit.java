package server.inuappcenter.kr.data.utils;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 캐시 무효화처럼 DB 커밋이 끝난 뒤에 실행해야 하는 작업을 등록한다.
 * 커밋 전에 캐시를 지우면, 그 사이 들어온 조회가 옛 데이터를 다시 캐싱할 수 있다.
 * 트랜잭션이 없으면(단위 테스트 등) 즉시 실행한다.
 */
public final class AfterCommit {

    private AfterCommit() {
    }

    public static void run(Runnable task) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    task.run();
                }
            });
        } else {
            task.run();
        }
    }
}
