package cn.sichu;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sichu
 * @date 2022/11/22
 **/
public class DirSize {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 文件夹大小 8, 17, 24
         * 总计 49
         * 要加锁对象
         */
        DirTotalSize dirTotalSize = new DirTotalSize();
        ReentrantLock lock = new ReentrantLock();
        CountDownLatch latch = new CountDownLatch(3);
        // CountDownLatch latch = new CountDownLatch(4);

        new Thread(new TT(dirTotalSize, "C:\\Users\\sichu\\dev\\IDEA-Workspace\\java-demos\\0002-multithread-dirsize\\1", lock,
            latch)).start();
        new Thread(new TT(dirTotalSize, "C:\\Users\\sichu\\dev\\IDEA-Workspace\\java-demos\\0002-multithread-dirsize\\2", lock,
            latch)).start();
        new Thread(new TT(dirTotalSize, "C:\\Users\\sichu\\dev\\IDEA-Workspace\\java-demos\\0002-multithread-dirsize\\3", lock,
            latch)).start();
        // 不好人为设定
        // Thread.sleep(500);
        // Thread.sleep(1000);
        System.out.println("start");
        latch.await();
        System.out.println("end");
        new Thread(new Total(dirTotalSize)).start();
    }

    static class TT implements Runnable {
        private DirTotalSize dirTotalSize;
        private String dirPath;
        private ReentrantLock lock;
        private CountDownLatch latch;

        public TT(DirTotalSize dirTotalSize, String dirPath, ReentrantLock lock, CountDownLatch latch) {
            this.dirTotalSize = dirTotalSize;
            this.dirPath = dirPath;
            this.lock = lock;
            this.latch = latch;
        }

        /**
         * 线程 1, 2, 3 在这里调用 DirTotalSize 的 add 方法
         * <p>
         * 需要一个dir的路径
         */
        @Override
        public void run() {
            lock.lock();
            try {
                dirTotalSize.add(FileUtils.sizeOfDirectory(new File(dirPath)));
            } finally {
                latch.countDown();
                lock.unlock();
            }
        }
    }

    static class Total implements Runnable {
        private DirTotalSize dirTotalSize;

        public Total(DirTotalSize dirTotalSize) {
            this.dirTotalSize = dirTotalSize;
        }

        @Override
        public void run() {
            //调用线程4 DirTotalSize 的 getTotalSize 方法
            System.out.println("总大小:" + dirTotalSize.getTotalSize());
        }
    }

    static class DirTotalSize {
        // 要加 volatile, 不然多线程之间存在可见性问题, 总大小输出0
        private volatile long totalSize;

        public void add(long size) {
            totalSize += size;
        }

        public long getTotalSize() {
            return totalSize;
        }
    }
}
