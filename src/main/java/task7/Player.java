package task7;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class Player {
    public static void main(String args[]) throws IOException, InterruptedException {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println("PID: " + name.split("@")[0]);
        int counter = 0;
        RandomAccessFile rd = new RandomAccessFile("C:/xiaominjin/mapped.txt", "rw");
        RandomAccessFile rd2 = new RandomAccessFile("C:/xiaominjin/mapped2.txt", "rw");
        FileChannel fc = rd.getChannel();
        FileChannel fc2 = rd2.getChannel();
        MappedByteBuffer mem = fc.map(FileChannel.MapMode.READ_WRITE, 0, 4000);
        MappedByteBuffer mem2 = fc2.map(FileChannel.MapMode.READ_WRITE, 0, 4000);
        CharBuffer charBuf = mem.asCharBuffer();
        CharBuffer charBuf2 = mem2.asCharBuffer();

        String msg = "hallo" ;
        char[] data;
        char c;
        AtomicInteger count = new AtomicInteger();
        for(int i=0; i < 10; i++){
            counter = count.incrementAndGet();
            msg = msg + counter + "\0";
            data = msg.toCharArray();
            charBuf.put(data);                   // send data to player2
            System.out.println("Process1 send : " + msg);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            charBuf2.put(0, '\0');      // let player2 release from waiting
            while( charBuf.get( 0 ) != '\0' );  // wait until release by player2
            charBuf.put(0, 'h');
            msg = "" ;
            while ((c = charBuf2.get()) != '\0') {    // read data sent by player2
                msg = msg + c;
            }
            System.out.println("Process1 receive : " + msg);
        }
        charBuf2.put(0, '\0');
        rd.close();
        rd2.close();
}
}
