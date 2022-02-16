import java.util.Stack;

public class ConsumerProducer {

    static Stack<Double> stapel = new Stack<>();

    public static void main (String[] args) throws InterruptedException {

        Producer p = new Producer();
        Consumer c = new Consumer();
        Consumer d = new Consumer();

        p.start();
        new Thread(c).start();
        new Thread(d).start();

    }
}

class Producer extends Thread {

    @Override
    public void run() {

        while (true) {

            A.stapel.push(Math.random() * 1000);

            synchronized (A.stapel) {
                A.stapel.notify();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

class Consumer implements Runnable {

    @Override
    public void run() {

        while (true) {

            try {
                synchronized (A.stapel) {
                    A.stapel.wait();
                }
            } catch (Exception e) {
                System.out.println("Der Consumer wurde benachrichtigt.");
            }

            System.out.println(A.stapel.pop() + " " + this);

        }
    }
}
