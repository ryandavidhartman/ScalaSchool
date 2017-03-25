package Week02.ObserverPattern

trait Subscriber {
  def handler(pub: Publisher)
}
