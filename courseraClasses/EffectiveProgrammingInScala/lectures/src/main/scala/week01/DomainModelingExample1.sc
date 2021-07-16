/*
Say the environmental impact of video streaming varies according to the vidoe quality and duration
and the type of the internet connection

A simplified  model footprint  is the following

      * Data centers consume 0.000072 kWh/MB of video
      * Mobile networks consume 0.00088 kWh/MB of video whereas
      * Fixed networks consume 0.00043 kWh/MB of video
      * Producing 1 kWh of electricity emits kg of C02 (world average)

What is the impact of watching a 30 minute series:
1) in high definition (0.6 MB/s) from a mobile phone?
2) in low defintion (0.3 MB/s) from a desktop compupter?
*/

enum Network:
  case Fixed, Mobile

case class Experience(durationInSeconds: Int, definitionInMBPerSecond: Double, network: Network)

val lowQuality = 0.3 // MB/s
val highQuality = 0.6 // MB/s
val dataCenterEnergyRate = 0.000072 // kWh/MB
val kgC02Perkwh = 0.5

def minutesToSeconds(m: Int): Int = m * 60

val case1 = Experience(minutesToSeconds(30), highQuality, Network.Mobile)
val case2 = Experience(minutesToSeconds(30), lowQuality, Network.Fixed)

def networkEnergyRate(network: Network): Double = network match
  case Network.Fixed => 0.00043
  case Network.Mobile => 0.00088

def environmentalImact(usage: Experience): Double =
  val megaBytes = usage.durationInSeconds * usage.definitionInMBPerSecond
  val powerConsumed = (dataCenterEnergyRate + networkEnergyRate(usage.network))*megaBytes

  powerConsumed*kgC02Perkwh



environmentalImact(case1)
environmentalImact(case2)

