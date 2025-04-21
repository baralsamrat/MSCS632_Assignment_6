package main

import (
	"fmt" // <-- ADD THIS
	"log"
	"os"
	"runtime"
	"sync"
	"time"
)

// Task represents a simple image processing task
type Task struct {
	Name string
}

// process simulates image processing work
func process(task Task) {
	time.Sleep(time.Millisecond * 500) // Simulate time-consuming work
}

func worker(id int, tasks <-chan Task, wg *sync.WaitGroup) {
	defer wg.Done()
	for task := range tasks {
		start := time.Now()
		process(task)
		duration := time.Since(start)
		log.Printf("Worker %d processed %s in %v\n", id, task.Name, duration)
	}
}

func main() {
	// Set up logging to a file
	logFile, err := os.OpenFile("output_go.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		log.Fatalf("Failed to open log file: %v", err)
	}
	defer logFile.Close()
	log.SetOutput(logFile)

	// Detect number of CPU cores
	defaultWorkers := runtime.NumCPU()

	// Read optional number of workers from args
	var numWorkers int
	if len(os.Args) > 1 {
		_, err := fmt.Sscanf(os.Args[1], "%d", &numWorkers)
		if err != nil || numWorkers <= 0 {
			log.Printf("Invalid worker count provided, falling back to default (%d workers)", defaultWorkers)
			numWorkers = defaultWorkers
		}
	} else {
		numWorkers = defaultWorkers
	}

	log.Printf("Starting with %d workers\n", numWorkers)

	// Prepare tasks
	taskList := []Task{
		{"Image_1.jpg"}, {"Image_2.jpg"}, {"Image_3.jpg"},
		{"Image_4.jpg"}, {"Image_5.jpg"}, {"Image_6.jpg"},
		{"Image_7.jpg"}, {"Image_8.jpg"}, {"Image_9.jpg"},
	}

	taskChan := make(chan Task, len(taskList))
	var wg sync.WaitGroup

	// Start workers
	for i := 1; i <= numWorkers; i++ {
		wg.Add(1)
		go worker(i, taskChan, &wg)
	}

	// Enqueue tasks
	for _, task := range taskList {
		taskChan <- task
	}
	close(taskChan)

	// Wait for all workers to finish
	wg.Wait()
	log.Println("All tasks completed.")
}
