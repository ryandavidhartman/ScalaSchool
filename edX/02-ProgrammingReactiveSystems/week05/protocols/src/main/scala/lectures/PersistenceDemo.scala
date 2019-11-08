//package lectures
//
//import akka.Done
//import akka.actor.typed.{ActorRef, ActorSystem}
//import akka.actor.typed.scaladsl._
//import akka.persistence.typed.scaladsl._
//
//import PersistentBehaviors.CommandHandler
//
//import scala.util.{ Try, Success, Failure }
//
//object PersistenceDemo {
//    sealed trait Ledger
//    case class Debit(account: String, amount: BigDecimal, replyTo: ActorRef[Result]) extends Ledger
//    case class Credit(account: String, amount: BigDecimal, replyTo: ActorRef[Result]) extends Ledger
//
//    sealed trait Result
//    case object Success extends Result
//    case object Failure extends Result
//
//    sealed trait Command
//    case object DebitSuccess extends Command
//    case object DebitFailure extends Command
//    case object CreditSuccess extends Command
//    case object CreditFailure extends Command
//    case object Stop extends Command
//
//    case class TransferConfig(ledger: ActorRef[Ledger], result: ActorRef[Result], amount: BigDecimal, from: String, to: String)
//
//    sealed trait Event
//    case object Aborted extends Event
//    case object DebitDone extends Event
//    case object CreditDone extends Event
//    case object RollbackStarted extends Event
//    case object RollbackFailed extends Event
//    case object RollbackFinished extends Event
//
//    sealed trait State
//    case class AwaitingDebit(config: TransferConfig) extends State
//    case class AwaitingCredit(config: TransferConfig) extends State
//    case class AwaitingRollback(config: TransferConfig) extends State
//    case class Finished(result: ActorRef[Result]) extends State
//    case class Failed(result: ActorRef[Result]) extends State
//
//    def adapter[T](ctx: ActorContext[Command], f: T => Command): ActorRef[T] =
//        ctx.spawnAnonymous(Behaviors.receiveMessage[T] { msg =>
//            ctx.self ! f(msg)
//            Behaviors.stopped
//        })
//
//    val commandHandler: CommandHandler[Command, Event, State] =
//        CommandHandler.byState {
//            case _: AwaitingDebit => awaitingDebit
//            case _: AwaitingCredit => awaitingCredit
//            case _: AwaitingRollback => awaitingRollback
//            case _ => (_, _, _) => Effect.stop
//        }
//
//    val eventHandler: (State, Event) => State = { (state, event) =>
//        println(s"in state $state receiving event $event")
//        (state, event) match {
//            case (AwaitingDebit(tc), DebitDone) => AwaitingCredit(tc)
//            case (AwaitingDebit(tc), Aborted) => Failed(tc.result)
//            case (AwaitingCredit(tc), CreditDone) => Finished(tc.result)
//            case (AwaitingCredit(tc), RollbackStarted) => AwaitingRollback(tc)
//            case (AwaitingRollback(tc), RollbackFinished | RollbackFailed) => Failed(tc.result)
//            case x => throw new IllegalStateException(x.toString)
//        }
//    }
//
//    val awaitingDebit: CommandHandler[Command, Event, State] = {
//        case (ctx, AwaitingDebit(tc), DebitSuccess) =>
//            // Note: the `andThen` method shown in the slides has been
//            // renamed to `thenRun`, see https://github.com/akka/akka/pull/25357
//            Effect.persist(DebitDone).thenRun { state =>
//                tc.ledger ! Credit(tc.to, tc.amount, adapter(ctx, {
//                    case Success => CreditSuccess
//                    case Failure => CreditFailure
//                }))
//            }
//        case (ctx, AwaitingDebit(tc), DebitFailure) =>
//            Effect.persist(Aborted)
//                  .thenRun((state: State) => tc.result ! Failure)
//                  .andThenStop
//        case x => throw new IllegalStateException(x.toString)
//    }
//
//    val awaitingCredit: CommandHandler[Command, Event, State] = {
//        case (ctx, AwaitingCredit(tc), CreditSuccess) =>
//            Effect.persist(CreditDone)
//                  .thenRun((state: State) => tc.result ! Success)
//                  .andThenStop
//        case (ctx, AwaitingCredit(tc), CreditFailure) =>
//            Effect.persist(RollbackStarted).thenRun { state =>
//                tc.ledger ! Credit(tc.from, tc.amount, adapter(ctx, {
//                    case Success => CreditSuccess
//                    case Failure => CreditFailure
//                }))
//            }
//        case x => throw new IllegalStateException(x.toString)
//    }
//
//    val awaitingRollback: CommandHandler[Command, Event, State] = {
//        case (ctx, AwaitingRollback(tc), CreditSuccess) =>
//            Effect.persist(RollbackFinished)
//                  .thenRun((state: State) => tc.result ! Failure)
//                  .andThenStop
//        case (ctx, AwaitingRollback(tc), CreditFailure) =>
//            Effect.persist(RollbackFailed)
//                  .thenRun((state: State) => tc.result ! Failure)
//                  .andThenStop
//        case x => throw new IllegalStateException(x.toString)
//    }
//
//    val happyLedger = Behaviors.receiveMessage[Ledger] {
//        case Debit(_, _, replyTo) => replyTo ! Success; Behaviors.same
//        case Credit(_, _, replyTo) => replyTo ! Success; Behaviors.same
//    }
//
//    def main(args: Array[String]): Unit = {
//        ActorSystem(Behaviors.setup[Result] { ctx =>
//            val ledger = ctx.spawn(happyLedger, "ledger")
//            val config = TransferConfig(ledger, ctx.self, 1000.00, "Alice", "Bob")
//            val transfer = ctx.spawn(PersistentBehaviors.receive(
//                persistenceId = "transfer-1",
//                emptyState = AwaitingDebit(config),
//                commandHandler = commandHandler,
//                eventHandler = eventHandler
//            ).onRecoveryCompleted {
//                case (ctx, AwaitingDebit(tc)) =>
//                    println("resuming debit")
//                    ledger ! Debit(tc.from, tc.amount, adapter(ctx, {
//                        case Success => DebitSuccess
//                        case Failure => DebitFailure
//                    }))
//                case (ctx, AwaitingCredit(tc)) =>
//                    println("resuming credit")
//                    ledger ! Credit(tc.to, tc.amount, adapter(ctx, {
//                        case Success => CreditSuccess
//                        case Failure => CreditFailure
//                    }))
//                case (ctx, AwaitingRollback(tc)) =>
//                    println("resuming rollback")
//                    ledger ! Credit(tc.from, tc.amount, adapter(ctx, {
//                        case Success => CreditSuccess
//                        case Failure => CreditFailure
//                    }))
//                case (ctx, Finished(result)) =>
//                    println("still finished")
//                    ctx.self ! Stop
//                    result ! Success
//                case (ctx, Failed(result)) =>
//                    println("still failed")
//                    ctx.self ! Stop
//                    result ! Failure
//            }, "transfer")
//
//            Behaviors.receiveMessage { _ =>
//                println("saga done")
//                Behaviors.stopped
//            }
//        }, "Persistence")
//    }
//}
