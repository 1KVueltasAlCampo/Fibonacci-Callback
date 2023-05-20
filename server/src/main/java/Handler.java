import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import Demo.CallbackPrx;

public class Handler {
    private final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    private static Handler instance = null;
    private ExecutorService threadPool;
    private HashMap<String, CallbackPrx> clientMap;
    private Semaphore semaphore;

    private Handler() {
        this.threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        this.clientMap = new HashMap<>();
        this.semaphore = new Semaphore(1);
    }

    public static Handler getInstance() {
        if (instance == null)
            instance = new Handler();
        return instance;
    }

    public ExecutorService getThreadPool() {
        return this.threadPool;
    }

    public HashMap<String, CallbackPrx> getClients() {
        return this.clientMap;
    }

    public Semaphore getSemaphore() {
        return this.semaphore;
    }

    public Future<String> executeTask(Logic logic) {
        return this.threadPool.submit(logic);
    }

    public void addClient(ToText trans, String message, CallbackPrx client) {
        String hostname = trans.toText(message);
        try {
            this.semaphore.acquire();
            if (!this.clientMap.containsKey(hostname)) {
                this.clientMap.put(hostname, client);
            }
        } catch (InterruptedException e) {
            System.out.println("[ERROR] " + hostname + " failed to join.\n");
            e.printStackTrace();
        } finally {
            this.semaphore.release();
        }
    }

    public void removeClient(String host) {
        try {
            this.semaphore.acquire();
            if (this.clientMap.containsKey(host)) {
                this.clientMap.remove(host);
            }
        } catch (InterruptedException e) {
            System.out.println("[ERROR] " + host + " failed to remove.\n");
            e.printStackTrace();
        } finally {
            this.semaphore.release();
        }
    }
}
