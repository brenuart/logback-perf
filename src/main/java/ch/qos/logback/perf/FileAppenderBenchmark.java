/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package ch.qos.logback.perf;

import static ch.qos.logback.perf.CommonConstants.LOG4J2_CONFGIGURATION_FILE_KEY;
import static ch.qos.logback.perf.CommonConstants.LOG4J_CONFGIGURATION_FILE_KEY;
import static ch.qos.logback.perf.CommonConstants.LOGBACK_CONFGIGURATION_FILE_KEY;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.slf4j.LoggerFactory;

/**
 * Benchmarks Log4j 2, Log4j 1, Logback and JUL using the DEBUG level which is
 * enabled for this test. The configuration for each uses a FileAppender
 */
// HOW TO RUN THIS TEST
// java -jar logback-perf/target/benchmarks.jar ".*FileAppenderBenchmark.*" -f 1
// -wi 10 -i 20
//
// RUNNING THIS TEST WITH 4 THREADS:
// java -jar logback-perf/target/benchmarks.jar ".*FileAppenderBenchmark.*" -f 1
// -wi 10 -i 20 -t 4
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class FileAppenderBenchmark {
    public static final String MESSAGE = "This is a debug message";

    Logger log4j2Logger;
    Logger log4j2RandomLogger;
    org.slf4j.Logger slf4jLogger;
    org.apache.log4j.Logger log4j1Logger;
    java.util.logging.Logger julLogger;
    String outFolder = "";

    @Setup
    public void setUp() throws Exception {
        // System.setProperty("logback.statusListenerClass", "ch.qos.logback.core.status.OnConsoleStatusListener");

        System.setProperty(LOGBACK_CONFGIGURATION_FILE_KEY, "logback-file.xml");
        System.setProperty(LOG4J_CONFGIGURATION_FILE_KEY, "log4j1-file.xml");
        System.setProperty(LOG4J2_CONFGIGURATION_FILE_KEY, "log4j2-file.xml");

        outFolder = System.getProperty("outFolder", "");

        deleteLogFiles();

        log4j2Logger = LogManager.getLogger(FileAppenderBenchmark.class);
        log4j2RandomLogger = LogManager.getLogger("TestRandom");
        slf4jLogger = LoggerFactory.getLogger(FileAppenderBenchmark.class);
        log4j1Logger = org.apache.log4j.Logger.getLogger(FileAppenderBenchmark.class);

    }

    @TearDown
    public void tearDown() {
        System.clearProperty(LOGBACK_CONFGIGURATION_FILE_KEY);
        System.clearProperty(LOG4J_CONFGIGURATION_FILE_KEY);
        System.clearProperty(LOG4J2_CONFGIGURATION_FILE_KEY);

        // deleteLogFiles();
    }

    private void deleteLogFiles() {
        final File logbackFile = new File("target/testlogback.log");
        logbackFile.delete();

        final File log4jFile = new File("target/testlog4j.log");
        log4jFile.delete();

        final File log4jRandomFile = new File("target/testRandomlog4j2.log");
        log4jRandomFile.delete();

        final File log4j2File = new File("target/testlog4j2.log");
        log4j2File.delete();
    }

    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Benchmark
    public void log4j2RAF() {
        log4j2RandomLogger.debug(MESSAGE);
    }

    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Benchmark
    public void log4j2File() {
        log4j2Logger.debug(MESSAGE);
    }

    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Benchmark
    public void logbackFile() {
        slf4jLogger.debug(MESSAGE);
    }

    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Benchmark
    public void log4j1File() {
        log4j1Logger.debug(MESSAGE);
    }
}
