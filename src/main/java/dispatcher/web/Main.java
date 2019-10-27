package dispatcher.web;

import java.util.concurrent.CountDownLatch;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 18.10.2019
 */
public class Main {
    private int anInt;
    private int a;
    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    private void doChange() {
        new Thread(() -> {
            int s = a;
            if (s == a) {
                System.out.println("start");
                for (; anInt < 2000000000; anInt++) {
                    if (s != a) {
                        System.out.println("see: " + anInt);
                        break;
                    }
                }
                System.out.println("finished");
            }
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(5);
                a++;
                System.out.println("changed: " + anInt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }).start();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.doChange();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

