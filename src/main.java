import java.io.IOException;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private static final int DEFAULT_TASK_COUNT = 9;

    public static void main(String[] args) {
        int numWorkers = Runtime.getRuntime().availableProcessors();  // Default to CPU cores

        if (args.length > 0) {
            try {
                int parsed = Integer.parseInt(args[0]);
                if (parsed > 0) {
                    numWorkers = parsed;
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid worker count provided, using default.");
            }
        }

        System.out.println("Starting with " + numWorkers + " workers.");

        BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
        for (int i = 1; i <= DEFAULT_TASK_COUNT; i++) {
            taskQueue.add(new Task("Image_" + i + ".jpg"));
        }

        Logger logger = setupLogger();

        ExecutorService executor = Executors.newFixedThreadPool(numWorkers);
        for (int i = 1; i <= numWorkers; i++) {
            executor.submit(new Worker(i, taskQueue, logger));
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        logger.info("All tasks completed.");
    }

    private static Logger setupLogger() {
        Logger logger = Logger.getLogger("DataProcessorLogger");
        try {
            FileHandler fileHandler = new FileHandler("output.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Disable console logging
        } catch (IOException e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
            System.exit(1);
        }
        return logger;
    }
}
