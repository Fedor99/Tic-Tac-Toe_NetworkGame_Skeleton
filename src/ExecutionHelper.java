import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.*;

public class ExecutionHelper {

    private static ExecutorService service = Executors.newCachedThreadPool();

    public static void invokeAll(Collection<? extends Callable<Integer>> _instances) throws InterruptedException {
        service.invokeAll(_instances);
    }
}