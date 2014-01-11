forecast-io-scala
=================

http://blog.knutwalker.de/forecast-io-scala/

Scala wrapper library for the v2 Forecast API provided by The Dark Sky Company, LLC.

> Based on https://github.com/MartinSeeler/forecast-io-wrapper

The Scala wrapper runs on top of [Akka](http://akka.io).

## About

The Forecast API lets you query for most locations on the globe, and returns:

- Current conditions
- Minute-by-minute forecasts out to 1 hour (where available)
- Hour-by-hour forecasts out to 48 hours
- Day-by-day forecasts out to 7 days


## Usage

### Samples

A simple request for the current temperature:
```scala
// specify your API key
val apiKey = "replace-with-your-key"
val forecastIO = ForecastIO(apiKey)

// coordinates for Dresden, Germany
val latitude = 51.0504
val longitude = 13.7373

// For asynchronous execution
import ForecastIO.executionContext

// retrieve the current forecast
val forecast = forecastIO(latitude, longitude)

// deal with the result when it is available
forecast.foreach { result =>
  println(s"Current temperature is ${result.currently.map(_.temperature)}")
  println(s"Apparent temperature is ${result.currently.map(_.apparentTemperature)")
}
```

`ForecastIo.apply` simply returns a `Future[Forecast]`.
`Forecast` and other nested POJOs are all case classes, so you can pattern match all the way through:
```scala
// specify your API key
val apiKey = "replace-with-your-key"
val forecastIO = ForecastIO(apiKey)

// coordinates for Dresden, Germany
val latitude = 51.0504
val longitude = 13.7373

// For asynchronous execution
import ForecastIO.executionContext

// retrieve the current forecast
val forecast = forecastIO(latitude, longitude)

// deal with the result when it is available
forecast onComplete {

  case Success(Forecast(_, _, timezone, _, Some(HourlyDataPoint(_, _, temperature, _, _, _, _, _)), _, _, Some(DailyDataBlock(_, _, Vector(DailyDataPoint(_, sunset), _*))))) =>

    println(s"It is $temperature degrees in $timezone and the sun sets at $sunset")
}
```

### Java API

`forecast-io-scala` also tries to provide a proper Java API, similar to [`forecast-io-wrapper`](https://github.com/MartinSeeler/forecast-io-wrapper)s.

```java
// specify your API key
final String apiKey = "replace-with-your-key";
final ForecastIO forecastIO = new ForecastIO(apiKey);

// coordinates for Dresden, Germany
final double latitude = 51.0504d;
final double longitude = 13.7373d;

// retrieve the current forecast
final Forecast forecast = forecastIO.getForecast(latitude, longitude);

System.out.println("Current temperature is " + forecast.getCurrently().getTemperature());
System.out.println("Apparent temperature is " + forecast.getCurrently().getApparentTemperature());
```

That is teh traditional blocking API. In addition to that, `forecast-io-scala` also provides an asynchronous API for Java

```java
// specify your API key
final String apiKey = "replace-with-your-key";
final ForecastIO forecastIO = new ForecastIO(apiKey);

// coordinates for Dresden, Germany
final double latitude = 51.0504d;
final double longitude = 13.7373d;

// retrieve the current forecast
forecastIO.asyncForecast(latitude, longitude, new Callback<Forecast>() {
    @Override
    public void onSuccess(Forecast result) {

        System.out.println("Current temperature is " + result.getCurrently().getTemperature());
        System.out.println("Apparent temperature is " + result.getCurrently().getApparentTemperature());
    }

    @Override
    public void onFailure(Throwable t) {}
});
```


### Shutdown

`forecast-io-scala` runs on top of [Akka](http://akka.io) and thus, needs to be shutdown properly.
You can do so beu calling either the static or the instance method `shutdown()` on `ForecastIO`

If omit the shutdown, your JVM will not terminate and you have to kill it abruptly.


## Development

You'll only need [sbt](http://www.scala-sbt.org/) and a JDK 7.
Scala, Akka, Spray and other dependencies are downloaded during the build process.


### Fatjar

`forecast-io-scala` features a fatjar, that prints some simple information about some place.
You can build the jar with `sbt assembly` and use it like so:
```bash
java -jar -Dforecast.apikey="your-api-key-here" target/scala-2.10/forecast-io-scala-0.1.0.jar 13.37 42
```

### Scaladoc

Scaladoc is available at http://blog.knutwalker.de/forecast-io-scala/current/
