/*
 * Copyright (c) 2014 Paul Horn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.knutwalker.forecastio.example;

import de.knutwalker.forecastio.Forecast;
import de.knutwalker.forecastio.ForecastIO;
import de.knutwalker.forecastio.data.DailyDataPoint;
import de.knutwalker.forecastio.japi.Callback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class JavaExample {

    private final ForecastIO forecastIO;

    public JavaExample() {
        forecastIO = ForecastIO.create("your-api-key-here");
    }

    public void shutdown() {
        forecastIO.shutdown();
    }

    public void temperature() throws Exception {
        temperature(forecastIO.getForecast(51.0504, 13.7373));
    }

    public void temperature(final Forecast forecast) {

        System.out.println("Current temperature is " + forecast.getCurrently().getTemperature());
        System.out.println("Apparent temperature is " + forecast.getCurrently().getApparentTemperature());
    }

    public void timezone() throws Exception {
        timezone(forecastIO.getForecast(51.0504, 13.7373));
    }
    public void timezone(final Forecast forecast) {
        final DailyDataPoint today = forecast.getDaily().getDataPoints()[0];

        final Date sunriseDate = new Date(today.getSunriseTime() * 1000L);
        final Date sunsetDate = new Date(today.getSunsetTime() * 1000L);

        final SimpleDateFormat isoFormat = new SimpleDateFormat("HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone(forecast.getTimezone()));

        System.out.println("Sunrise today was at " + isoFormat.format(sunriseDate));
        System.out.println("Sunset today will be at " + isoFormat.format(sunsetDate));
    }

    public void async(final Runnable runnable) {

        forecastIO.asyncForecast(51.0504, 13.7373, new Callback<Forecast>() {
            @Override
            public void onSuccess(Forecast result) {

                temperature(result);
                timezone(result);
                runnable.run();
            }

            @Override
            public void onFailure(Throwable t) {

                System.err.println(t.getMessage());
                runnable.run();
            }
        });

    }

    public static void main(String[] args) throws Exception {

        final JavaExample example = new JavaExample();

        example.temperature();
        example.timezone();

        example.async(new Runnable() {
            @Override
            public void run() {
                example.shutdown();
            }
        });
    }
}
