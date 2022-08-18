package fe.ThreadFe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现线程按顺序执行
 */
public class TestfeThread {
    public static void main(String[] args) {
        MyService myService = new MyService();
        for (int i = 0; i < 5; i++) {
            ThreadA threadA = new ThreadA(myService);
            threadA.start();
            ThreadB threadB = new ThreadB(myService);
            threadB.start();
            ThreadC threadC = new ThreadC(myService);
            threadC.start();
        }
        System.out.println("second add");
        System.out.println("third add");
        System.out.println("hot-fix add");
    }
}

class ThreadA extends  Thread{
    private MyService myService;

    public ThreadA(MyService myService){
        this.myService = myService;
    }

    @Override
    public void run() {
        myService.testMethod01();
    }
}

class ThreadB extends  Thread{
    private MyService myService;

    public ThreadB(MyService myService){
        this.myService = myService;
    }

    @Override
    public void run() {
        myService.testMethod02();
    }
}

class ThreadC extends  Thread{
    private MyService myService;

    public ThreadC(MyService myService){
        this.myService = myService;
    }

    @Override
    public void run() {
        myService.testMethod03();
    }
}


class MyService{
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    volatile private int nextWhoPrint = 1;

    public void testMethod01(){
        try{
            lock.lock();
            while(nextWhoPrint != 1){
                condition.await();
            }
            System.out.println("AAA");
            nextWhoPrint = 2;
            condition.signalAll();
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testMethod02(){
        try{
            lock.lock();
            while(nextWhoPrint != 2){
                condition.await();
            }
            System.out.println("BBB");
            nextWhoPrint = 3;
            condition.signalAll();
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testMethod03(){
        try{
            lock.lock();
            while(nextWhoPrint != 3){
                condition.await();
            }
            System.out.println("CCC");
            nextWhoPrint = 1;
            condition.signalAll();
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
