
DATE=$(date  '+%Y-%m-%dT%H%M')

echo "Results will be output into file results-${DATE}-[threadCount].txt"
sleep 1

DATE=$(date  '+%Y-%m-%dT%H%M')

for TC in 1 2 4 8 16 32 64;
do
    mvn clean
    mvn install
    echo "Number of threads $TC"
    # default run duration of 10 seconds, single fork, time unit: microseconds, 2 warm up iterations, 4 iterations,
    # $TC is the thread count, timeout 3 seconds, output results to "results-${DATE}-$TC.txt"
    java -jar target/benchmarks.jar ".*FileAppenderBenchmark.*" -f 1 -tu ms -wi 2 -i 4 -t $TC -to 3 -o "results-${DATE}-$TC.txt" 
done


 

