# Akka remote lunapark
[![Build Status](https://travis-ci.org/tkurylek/akka-remote-lunapark.svg?branch=master)](https://travis-ci.org/tkurylek/akka-remote-lunapark)

## Compile, test and package
```
mvn clean install
```
## Running
Run the lunapark employees:
```
java -jar lunapark-employees/target/lunapark-employees-1.0-SNAPSHOT-allinone.jar
```
They will be waiting for the events that `WeatherReporter` and `NewsReporter` provide.

To produce weather events execute weather-forecast project:
```
java -jar weather-forecast/target/weather-forecast-1.0-SNAPSHOT-allinone.jar
```

To produce news events execute news-report project:
```
java -jar news-report/target/news-report-1.0-SNAPSHOT-allinone.jar
```
