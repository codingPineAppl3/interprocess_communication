import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Players implements Runnable{
    Thread thread;
    private BlockingQueue<String> messageSent = new ArrayBlockingQueue<String>(10);;
    private BlockingQueue<String> messageReceived = new ArrayBlockingQueue<String>(10);;
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;
    private String player;
    private int counter;
    boolean isRunning = true;


    public Players(BlockingQueue<String> messageSent, BlockingQueue<String> messageReceived, String player) {
        this.messageSent = messageSent;
        this.messageReceived = messageReceived;
        this.player = player;
    }
    @Override
    public void run() {

        if(player == "initiator")
        {
            System.out.println("I am:\t" + player);
            String data = "msg";
            Random r = new Random();
            AtomicInteger count = new AtomicInteger();
            try {
                while (isRunning) {
                    counter = count.incrementAndGet();
                    if(counter == 10){
                        isRunning = false;
                    }
                    // concatenate message with message counter
                   data = data + counter;
                    if (!messageSent.offer(data, 2, TimeUnit.SECONDS)) {
                        System.out.println("initiator send fail: " + data);
                    } else
                    {System.out.println("Initiator send: " + data);}
                    //initiator wait for 2nd player to reply
                    Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                    //message received
                    data = messageReceived.poll(2, TimeUnit.SECONDS);
                    if (null != data) {
                        System.out.println("Initiator get reply: " + data);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Thread Initiator stop!");
            }

        } else {
            System.out.println("I am:\t" + player);
            String data = null;
            Random r = new Random();
            AtomicInteger count = new AtomicInteger();
            try {
                while (isRunning) {
                    Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                    data = messageReceived.poll(2, TimeUnit.SECONDS);
                    if (null != data) {
                        System.out.println("Player get: " + data);
                        // concatenate message with message counter
                        data = data + count.incrementAndGet();
                        if (!messageSent.offer(data, 2, TimeUnit.SECONDS)) {
                            System.out.println("send back fail: " + data);
                        } else {
                            System.out.println("Player reply: " + data);
                        }
                    } else {
                        System.out.println("Player get null: " + data);
                        isRunning = false;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Thread Player stop!");
            }
        }
    }

    //start thread
    public void start() {
        System.out.println("Thread started");
        if (thread == null) {
            thread = new Thread(this, player);
            thread.start();
        }
    }
}
