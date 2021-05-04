import java.util.concurrent.*;

public class CallAndResponse {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue1 = new ArrayBlockingQueue<String>(10);
        BlockingQueue<String> queue2 = new ArrayBlockingQueue<String>(10);

        //create initate player who will send the initate message
        Players initiator = new Players(queue1, queue2, "initiator");
        initiator.start();
        //create 2nd player who will answer the initiator
        Players secondPlayer = new Players(queue2, queue1,"secondPlayer");
        secondPlayer.start();
    }
}
