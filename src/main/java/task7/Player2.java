package task7;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class Player2 {
    public static void main(String args[]) throws IOException {
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
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String msg;
        char[] data;
        char c;
        AtomicInteger count = new AtomicInteger();
        for(int j =0; j < 10; j++) {
            counter = count.incrementAndGet();
            msg = "";
            while ((c = charBuf.get()) != '\0') {  // read data sent by player
                msg = msg + c;
            }
            System.out.println("Process2 receive " + msg);
            msg = msg + counter + "\0";
            data = msg.toCharArray();
            charBuf2.put(data);                    // send data to player
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Process2 send " + msg);
            charBuf.put(0, '\0');        // let player release from waiting
            while( charBuf2.get( 0 ) != '\0' );  // wait until release by player
            charBuf2.put(0, 'h');
        }
        rd.close();
        rd2.close();
    }
    }

