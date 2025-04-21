import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Worker implements Runnable {
    private final int id;
    private final BlockingQueue<Task> taskQueue;
    private final Logger logger;

    public Worker(int id, BlockingQueue<Task> taskQueue, Logger logger) {
        this.id = id;   
        this.taskQueue = taskQueue;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Task task = taskQueue.poll();
                if (task == null) {
                    break;
                }
                long startTime = System.currentTimeMillis();
                simulateProcessing(task);
                long duration = System.currentTimeMillis() - startTime;
                logger.info(String.format("Worker %d processed %s in %d ms", id, task.getName(), duration));
            }
        } catch (Exception e) {
            logger.severe("Worker " + id + " encountered an error: " + e.getMessage());
        }
    }

    private void simulateProcessing(Task task) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}