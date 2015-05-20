package commands

import com.netflix.hystrix.HystrixCommand.Setter
import com.netflix.hystrix.{HystrixCommandKey, HystrixCommandProperties, HystrixCommandGroupKey, HystrixCommand}


object NewestKittenCommand {
  private final val key = Setter
    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("NewestKitten"))
    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2000))

  private final val kittenNames = List(
    "Bernd", "Julie", "Tommy", "Ralphe", "Kayleigh", "Blaise"
  )

  def apply() = new NewestKittenCommand
}
class NewestKittenCommand extends HystrixCommand[Option[String]](NewestKittenCommand.key) with BogusHelper {
  import NewestKittenCommand.kittenNames

  def run(): Option[String] = {
    simulateNetworkLatency(800, 1500)
    simulateNetworkSpike(3, 500, 1000)

    if (chance(80)) Some(rand.shuffle(kittenNames).head)
    else None
  }

  override def getFallback: Option[String] = None
}


object ProfileViewsCommand {
  private final val key = Setter
    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProfileViews"))
    .andCommandKey(HystrixCommandKey.Factory.asKey("ProfileViews"))
    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2000))

  def apply() = new ProfileViewsCommand
}
class ProfileViewsCommand extends HystrixCommand[Int](ProfileViewsCommand.key) with BogusHelper {

  def run(): Int = {
    simulateNetworkLatency(425, 725)
    simulateFailUnderLoad(0.0001)
    simulateNetworkSpike(5, 800, 1400)

    between(50, 150)
  }

  override def getFallback: Int = 0
}


object KittensViewsCommand {
  private final val key = Setter
    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProfileViews"))
    .andCommandKey(HystrixCommandKey.Factory.asKey("KittensViews"))
    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1500))

  def apply() = new KittensViewsCommand
}
class KittensViewsCommand extends HystrixCommand[Int](KittensViewsCommand.key) with BogusHelper {

  def run(): Int = {
    simulateNetworkLatency(150, 800)
    simulateFailUnderLoad(0.001)
    simulateNetworkSpike(5, 675, 1125)

    between(5, 15)
  }

  override def getFallback: Int = 0
}


object NewCommentsCommand {
  private final val key = Setter
    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProfileUpdates"))
    .andCommandKey(HystrixCommandKey.Factory.asKey("NewComments"))
    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1500))

  def apply() = new NewCommentsCommand
}
class NewCommentsCommand extends HystrixCommand[Int](NewCommentsCommand.key) with BogusHelper {

  def run(): Int = {
    simulateNetworkLatency(280, 900)
    simulateFailUnderLoad(0.001)
    simulateNetworkSpike(12, 700, 1800)

    between(5, 20)
  }

  override def getFallback: Int = 0
}


object NewLikesCommand {
  private final def key = Setter
    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProfileUpdates"))
    .andCommandKey(HystrixCommandKey.Factory.asKey("NewLikes"))

  def apply() = new NewLikesCommand
}
class NewLikesCommand extends HystrixCommand[Int](NewLikesCommand.key) with BogusHelper {

  def run(): Int = {
    simulateNetworkLatency(50, 125)
    simulateFailUnderLoad(0.01)
    simulateNetworkSpike(15, 650, 2250)

    between(20, 35)
  }

  override def getFallback: Int = 0
}

