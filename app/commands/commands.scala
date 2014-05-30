package commands

import com.netflix.hystrix.HystrixCommand.Setter
import com.netflix.hystrix.{HystrixCommandKey, HystrixCommandProperties, HystrixCommandGroupKey, HystrixCommand}
import scala.util.Random

class NewestKitten() extends HystrixCommand[String](NewestKitten.key) {
  private val rand = new Random()

  def run(): String = rand.shuffle(NewestKitten.possibleNewKittens).head
}

object NewestKitten {
  private final def key = HystrixCommandGroupKey.Factory.asKey("NewestKitten")
  private final val possibleNewKittens = List(
    "Bernd", "Julie", "Tommy", "Ralphe", "Kayleigh", "Blaise"
  )

  def apply() = new NewestKitten
}

class ProfileViews extends HystrixCommand[Int](ProfileViews.key) {
  private val rand = new Random()

  def run(): Int = {
    Thread.sleep(1800)
    rand.nextInt(100) + 50
  }
}

object ProfileViews {
  private final def key = Setter
    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProfileViews"))
    .andCommandKey(HystrixCommandKey.Factory.asKey("ProfileViews"))
    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(3000))

  def apply() = new ProfileViews
}

class KittensViews extends HystrixCommand[Int](KittensViews.key) {
  private val rand = new Random()

  def run(): Int = {
//    Thread.sleep(1400)
    rand.nextInt(10) + 5
  }
}

object KittensViews {
  private final def key = Setter
    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProfileViews"))
    .andCommandKey(HystrixCommandKey.Factory.asKey("KittensViews"))
    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(3000))

  def apply() = new KittensViews
}


class NewComments extends HystrixCommand[Int](NewComments.key) {
  private val rand = new Random()

  def run(): Int = rand.nextInt(10) + 5
}

object NewComments {
  private final def key = Setter
    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProfileUpdates"))
    .andCommandKey(HystrixCommandKey.Factory.asKey("NewComments"))
    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(3000))

  def apply() = new NewComments
}


class NewLikes extends HystrixCommand[Int](NewLikes.key) {
  private val rand = new Random()

  def run(): Int = rand.nextInt(200) + 10
}

object NewLikes {
  private final def key = Setter
    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProfileUpdates"))
    .andCommandKey(HystrixCommandKey.Factory.asKey("NewLikes"))
    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(3000))

  def apply() = new NewLikes
}
