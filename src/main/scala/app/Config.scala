package app

import pureconfig.generic.semiauto._
import pureconfig.{ConfigReader, ConfigSource}

final case class Config(s3Endpoint: String)

object Config {

  implicit val configReader: ConfigReader[Config] = deriveReader[Config]

  def read(): Config =
    ConfigSource.default
      .value()
      .flatMap { source =>
        ConfigReader[Config].from(source)
      }.right.get


}
