package example.countwords

import akka.cluster.typed.{ Cluster, Subscribe }
import akka.cluster.typed.SelfUp
import akka.actor.typed.scaladsl.{ Behaviors, Routers }
import akka.actor.typed.{ ActorSystem, Behavior }
import com.typesafe.config.ConfigFactory

object CountWordsApp {

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      startup("worker", 0)
    } else {
      require(
        args.size == 2,
        "Usage: two params required 'role' and 'port'")
      startup(args(0), args(1).toInt)
    }
  }

  def startup(role: String, port: Int): Unit = {

    val config = ConfigFactory
      .parseString(s"""
      akka.remote.artery.canonical.port=$port
      akka.cluster.roles = [$role]
      """)
      .withFallback(ConfigFactory.load("words"))

    val guardian =
      ActorSystem(ClusteredGuardian(), "WordsCluster", config)

    println(
      "#################### press ENTER to terminate ###############")
    scala.io.StdIn.readLine()
    guardian.terminate()
  }

  private object ClusteredGuardian {

    def apply(): Behavior[SelfUp] =
      Behaviors.setup[SelfUp] { context =>

        val cluster = Cluster(context.system)
        if (cluster.selfMember.hasRole("director")) {
          // instead of cluster.registerOnMemberUp now on typed
          Cluster(context.system).subscriptions ! Subscribe(
            context.self,
            classOf[SelfUp])
        }
        if (cluster.selfMember.hasRole("aggregator")) {
          val numberOfWorkers =
            context.system.settings.config
              .getInt("example.countwords.workers-per-node")
          for (i <- 0 to numberOfWorkers) {
            //with supervision resume
            context.spawn(Worker(), s"worker-$i")
          }
        }
        Behaviors.receiveMessage {
          case SelfUp(_) =>
            val router = context.spawnAnonymous {
              Routers
                .group(Worker.RegistrationKey)
            }
            context.spawn(Master(router), "master")
            Behaviors.same
        }
      }
  }
}
