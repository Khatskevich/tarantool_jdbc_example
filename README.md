# This repo demonstrates Tarantool jdbc driver features

## Requirements
- Tarantool 2.0
- jdk 1.8

SQL support was added in Tarantool 2.0.


## Run project
1. Start tarantool:
    ```
    cd tarantool
    tarantool start.lua
    ```
2.  
    ```
    mvn clean compile assembly:single
    java -cp target/java_dbc-1.0-SNAPSHOT-jar-with-dependencies.jar Main 
    ```
