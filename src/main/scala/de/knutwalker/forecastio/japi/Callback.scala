package de.knutwalker.forecastio.japi

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

/** ''Java API''
  *
  * Wrapper for `Future#onComplete`
  *
  *@tparam T generic type (of the Future)
  */
trait Callback[T] {

  /** When the future is completed successfully (i.e. with a value),
    * callback this method.
    *
    * If the future has already been completed with a value,
    * this will either be applied immediately or be scheduled asynchronously.
    *
    * Will not be called in case that the future is completed with a failure.
    */
  def onSuccess(result: T): Unit

  /** When the future is completed with a failure (i.e. with a throwable),
    * callback this method.
    *
    * If the future has already been completed with a failure,
    * this will either be applied immediately or be scheduled asynchronously.
    *
    * Will not be called in case that the future is completed with a value.
    */
  def onFailure(t: Throwable): Unit
}
