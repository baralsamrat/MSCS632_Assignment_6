#!/bin/bash

echo "============================================================================="
echo "Build and Run Go Multi-Threaded Data Processor"
echo "============================================================================="



echo "============================================================================="
echo " Using Go "
echo "============================================================================="
set -e  # Exit immediately on errors
rm -f output_go.log

# Build
echo "Building Go project..."
# go mod init main.go
go build -o processor main.go



# Run
echo "Running Data Processor..."
if [ "$#" -eq 1 ]; then
    ./processor "$1"  # Pass worker count if provided
else
    ./processor       # Default worker count
fi

echo "============================================================================="
echo " Using Java "
echo "============================================================================="
set -e  # Exit immediately if a command exits with a non-zero status

rm -f output_java.log

echo "Compiling Java project..."
mkdir -p bin
javac  -d bin *.java
echo "Running Data Processor..."
if [ "$#" -eq 1 ]; then
    java -cp bin Main "$1"
else
    java -cp bin Main
fi

echo "============================================================================="
