#!/bin/bash

# =============================================================================
# run.sh - Build and Run Go Multi-Threaded Data Processor
# =============================================================================

# set -e  # Exit immediately on errors
# rm -f output.log

# # Build
# echo "Building Go project..."
# # go mod init main.go
# go build -o processor main.go



# # Run
# echo "Running Data Processor..."
# if [ "$#" -eq 1 ]; then
#     ./processor "$1"  # Pass worker count if provided
# else
#     ./processor       # Default worker count
# fi


#!/bin/bash

# =============================================================================
# run.sh - Build and Run Java Multi-Threaded Data Processor
# =============================================================================

set -e  # Exit immediately if a command exits with a non-zero status

echo "Compiling Java project..."
rm -f output.log
mkdir -p bin
javac -d bin src/*.java

echo "Running Data Processor..."
if [ "$#" -eq 1 ]; then
    java -cp bin Main "$1"
else
    java -cp bin Main
fi
