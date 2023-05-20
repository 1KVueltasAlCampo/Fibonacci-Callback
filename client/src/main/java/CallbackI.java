import com.zeroc.Ice.Current;

public class CallbackI implements Demo.Callback {
    private String result; // Variable para almacenar el resultado del servidor
    
    @Override
    public void response(String message, Current current) {
        System.out.println("Server response: " + message);
        this.result = message; // Guardar el resultado en la variable result
        synchronized (this) {
            notify(); // Notificar al método waitForResult que el resultado está disponible
        }
    }
    
    /**
     * Método para esperar y obtener el resultado del servidor
     * @return el resultado del servidor como String
     */
    @Override
    public synchronized String waitForResult(Current current) {
        System.out.println("Waiting for result...");
        System.out.println("Result: " + result);
        while (result == null) {
            try{
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
            }
        }
        return result;
    }
}
