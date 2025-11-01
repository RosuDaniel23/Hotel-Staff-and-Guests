#!/bin/bash
# Run the Hotel Management Java Client

cd "$(dirname "$0")"

# Compile if needed
if [ ! -d "out" ] || [ -z "$(ls -A out 2>/dev/null)" ]; then
    echo "Compiling Java client..."
    javac -d out -cp libs/gson-2.10.1.jar src/main/java/*.java src/main/java/dto/*.java
    if [ $? -ne 0 ]; then
        echo "Compilation failed!"
        exit 1
    fi
    echo "Compilation successful!"
fi

# Run the client
echo "Starting Hotel Client..."
java -cp out:libs/gson-2.10.1.jar MainApp

