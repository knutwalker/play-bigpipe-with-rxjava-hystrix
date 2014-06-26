package commands

import scala.util.Random


trait BogusHelper {
  protected final val rand = new Random()

  /** sleeps for about `millis` milliseconds */
  def sleep(millis: Int): Unit = try {
    Thread.sleep(millis)
  } catch {
    case e: InterruptedException => // do nothing
  }

  /** returns a random number in the range of [min, max) */
  def between(min: Int, max: Int): Int = rand.nextInt(math.max(1, max - min)) + min

  /** returns true at about 100 * `rate` % of the time  - rate should be [0, 1] */
  def chance(rate: Double): Boolean = rand.nextDouble() < rate

  /** return true at about 100/`percent` % of the time - percent should be [0, 100] */
  def chance(percent: Int): Boolean = rand.nextInt(100) < percent

  /** simulates network latency by sleeping between `min` and `max` milliseconds */
  def simulateNetworkLatency(min: Int, max: Int): Unit = sleep(between(min, max))

  /** simulates failure under load by throwing an exception at a change of `rate` * 100 % or 1/`rate` */
  def simulateFailUnderLoad(rate: Double): Unit = if (chance(rate)) throw new RuntimeException("random failure under heavy network load")

  /** simulates network spikes by sleeping betwwen `min` and `max` milliseconds at a change of `percent` %. */
  def simulateNetworkSpike(rate: Int, min: Int, max: Int): Unit = if (chance(rate)) sleep(between(min, max))
}
