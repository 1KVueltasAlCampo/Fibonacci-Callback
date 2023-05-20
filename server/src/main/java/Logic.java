import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.math.BigInteger;

import Demo.CallbackPrx;

public class Logic implements Callable<String> {
    private String message;
    private CallbackPrx proxy;
    private Handler handler;
    private Semaphore sem;

    public Logic(String message, CallbackPrx proxy, Handler handler) {
        this.message = message;
        this.proxy = proxy;
        this.handler = handler;
        this.sem = handler.getSemaphore();
    }

    public String getMessage() {
        return this.message;
    }

    public CallbackPrx getProxy() {
        return this.proxy;
    }

    public Handler getHandler() {
        return this.handler;
    }

    @Override
    public String call() throws Exception {
        String host = this.message.split(":", 2)[0]; // host: message
        String message = this.message.split(":", 2)[1].toLowerCase(); // host: message
        String response = "";
        try {
            if (message.startsWith("exit")) {
                this.handler.removeClient(host);
            } else if (message.startsWith("list clients")) { // list clients
                replyHosts(getHosts());
            } else if (message.startsWith("bc")) { // bc message
                emitBroadcast(host, message);
            } else if (message.startsWith("to")) { // to host:message
                String to = message.replace("to", "").trim().split(":", 2)[0];
                String msg = message.replace("to", "").trim().split(":", 2)[1];
                send(host, to, msg);
            } else { // Fibonacci Calculation
                response = validationLayer(this.message);
                this.proxy.response(response);
                return response;
            }
        } catch (InterruptedException e) {
            sem.release();
            e.printStackTrace();
        }
        return null;
    }

    public String validationLayer(String message) {
        String response = "0";
        try {
            long number = Long.parseLong(message.trim().split(":", 2)[1]);
            if (number > 0) {
                System.out.print(message.split(":", 2)[0]+" ");
                response = fibonacci(number);
            }
        } catch (Exception e) {
            System.out.println("Fibonacci number must be a positive integer");
        }
        return response;
    }

    private String fibonacci(long posicion) {
        BigInteger siguiente = BigInteger.valueOf(1), actual = BigInteger.valueOf(0), temporal = BigInteger.valueOf(0);
		for (long x = 1; x <= posicion; x++) {
			temporal = actual;
			actual = siguiente;
			siguiente = siguiente.add(temporal);
			System.out.print((x == posicion)?actual.toString(0):(actual.toString(0)+","));
		}
		return actual.toString(0);
		
	}

    public ArrayList<String> getHosts() throws InterruptedException {
        sem.acquire();
        ArrayList<String> hosts = new ArrayList<String>();
        for (String host : this.handler.getClients().keySet()) {
            hosts.add(host);
        }
        sem.release();
        return hosts;
    }

    public void replyHosts(ArrayList<String> hosts) {
        String response = "\n Hosts: \n";
        for (String host : hosts) {
            response += "\t" + host + "\n";
        }
        this.proxy.response(response);
    }

    public void emitBroadcast(String source, String message) throws InterruptedException {
        System.out.println("Broadcasting message from " + source + " to all clients");
        sem.acquire();
        for (String host : this.handler.getClients().keySet()) {
            if (!host.equals(source)) {
                this.handler.getClients().get(host).response(source + ": " + message);
            }
        }
        sem.release();
    }

    public void send(String from, String to, String message) throws InterruptedException {
        System.out.println("Sending message from " + from + " to " + to);
        sem.acquire();
        if (this.handler.getClients().containsKey(to)) {
            this.handler.getClients().get(to).response(from + ": " + message);
        } else {
            this.handler.getClients().get(from).response("Host " + to + " not found");
        }
        sem.release();
    }
}
