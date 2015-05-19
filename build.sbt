name := "play-bigpipe-with-rxjava-hystrix"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.netflix.hystrix"  % "hystrix-core"   % "1.4.0-RC4",
  "com.netflix.hystrix"  % "hystrix-metrics-event-stream"  % "1.4.0-RC4",
  "com.netflix.rxjava"   % "rxjava-scala"   % "0.20.7",
  "org.webjars" % "bootstrap" % "3.1.1",
  "org.webjars" % "font-awesome" % "4.1.0",
  "org.webjars" % "headjs" % "1.0.3"
)

TwirlKeys.templateFormats += ("pagelet" -> "pagelet.PageletObservableFormat")

includeFilter in (Assets, LessKeys.less) := "*.less"
