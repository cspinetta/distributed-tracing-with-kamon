package kamon.demo.tracing.item.model;

import kamon.Kamon;
import kamon.trace.SpanCustomizer$;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;
import scala.runtime.AbstractFunction0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Value
public class QuerySimulator {

    static ExecutorService parallelExecutor = Executors.newFixedThreadPool(20);

    static {
        try {
            getDbConnection().prepareStatement("CREATE ALIAS SLEEP FOR \"java.lang.Thread.sleep(long)\"").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void runN(int executions) {
        run(executions, 2000); // 2 seconds as max delay;
    }

    @SneakyThrows
    public static void runInParallelN(int parallelExecutions) {
        List<Callable<Void>> callables = new ArrayList<>();
        for(int i= 0; i < parallelExecutions; i++) {
            int id = i;
            callables.add(() -> {
                execute(2000, id);
                return null;
            });
        }

        parallelExecutor.invokeAll(callables).forEach(QuerySimulator::await);
    }

    public static void run(int executions, int maxDelay) {
        for(int i= 0; i < executions; i++) {
            execute(maxDelay, i);
        }
    }

    private static void execute(int maxDelay, int id) {
        val randomTime = getRandom(maxDelay); // max delay
        val ctx = Kamon.currentContext().withKey(SpanCustomizer$.MODULE$.ContextKey(), SpanCustomizer$.MODULE$.forOperationName("query-simulation-" + id));
        Kamon.withContext(ctx, new AbstractFunction0<Object>() {
            @Override
            @SneakyThrows
            public Object apply() {
                return getDbConnection().prepareStatement("CALL SLEEP(" + randomTime + ")").execute();
            }
        });
    }

    @SneakyThrows
    private static Connection getDbConnection() {
        val connection = DriverManager.getConnection("jdbc:h2:mem:tracing;MULTI_THREADED=TRUE;", "SA", "");
        connection.setAutoCommit(false);
        return connection;
    }

    private static void await(Future<Void> future) {
        try {
             future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int getRandom(int max){
        int i = (int) (Math.random() * max);
        System.out.println("RandomTime " + i);
        return i;
    }


    public static void main(String... args) {
        QuerySimulator.runInParallelN(5);
        parallelExecutor.shutdown();
//        QuerySimulator.runInParallelN(10);
//        QuerySimulator.runInParallelN(10);
    }
}
