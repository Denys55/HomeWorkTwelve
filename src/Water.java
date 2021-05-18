import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Water {
    CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
    Semaphore oxygen = new Semaphore(1);
    Semaphore hydrogen = new Semaphore(2);

    public void releaseOxygen(Runnable runOxygen) throws InterruptedException {
        oxygen.acquire();
        runOxygen.run();
        try{
            cyclicBarrier.await();
        } catch (BrokenBarrierException e){
            e.printStackTrace();
        }
        hydrogen.release();
    }

    public void releaseHydrogen(Runnable runHydrogen) throws InterruptedException {
        hydrogen.acquire();
        runHydrogen.run();
        try{
            cyclicBarrier.await();
        } catch (BrokenBarrierException e){
            e.printStackTrace();
        }
        hydrogen.release();
    }

    public static void main(String[] args) {
        Water water = new Water();
        String molecule = "HOHOO";

        Runnable runOxygen = () -> System.out.print("O");
        Runnable runHydrogen = () -> System.out.print("H");

        for(int i = 0; i< molecule.length(); i++){
            char ch = molecule.charAt(i);
            if(ch=='O'){
                new Thread(() -> {
                    try {
                        water.releaseOxygen(runOxygen);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            else if(ch=='H'){
                new Thread(() -> {
                    try{
                        water.releaseHydrogen(runHydrogen);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
