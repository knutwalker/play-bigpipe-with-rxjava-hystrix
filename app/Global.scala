import com.netflix.config.ConfigurationManager
import play.api.{Application, GlobalSettings}


object Global extends GlobalSettings {
  override def beforeStart(app: Application) = {
    super.beforeStart(app)
    // set the rolling percentile more granular so we see data change every second rather than every 10 seconds as is the default
    ConfigurationManager.getConfigInstance.setProperty("hystrix.command.default.metrics.rollingPercentile.numBuckets", 60)
  }
}
