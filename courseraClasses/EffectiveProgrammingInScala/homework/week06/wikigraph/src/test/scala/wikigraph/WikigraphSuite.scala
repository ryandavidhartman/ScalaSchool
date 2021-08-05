package wikigraph

import org.scalacheck.*
import Prop.{forAll, propBoolean}

import wikigraph.*
import wikigraph.WikiResult.*
import wikigraph.Articles.*
import wikigraph.implementations.InMemory
import wikigraph.errors.*
import wikigraph.errors.WikiError.*

import wikigraph.testing.*

import scala.util.{Try, Success => TSucc, Failure => TFail}
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.given

class WikigraphSuite extends munit.FunSuite:
  given Gen[Int] = Arbitrary.arbitrary[Int]
  given Gen[String] = Arbitrary.arbitrary[String]
  given idGen: Gen[ArticleId] =
    Gen.posNum[Int].map(ArticleId(_))
  given artNotFoundGen: Gen[ArticleNotFound] =
    idGen.map(ArticleNotFound(_))
  given wikiErrorGen: Gen[WikiError] =
    for
      id <- idGen
      func <- Gen.oneOf(ArticleNotFound(_), TitleNotFound(_))
    yield func(id)

  given wikiExceptionGen: Gen[WikiException] =
    Gen.oneOf(WikiException.Timeout, WikiException.TooManyRequests, WikiException.ResourceNotFound("test"))

  def wikiResultGen[A](withErrors: Boolean, withFailure: Boolean)(using errGen: Gen[WikiError], resGen: Gen[A]): Gen[WikiResult[A]] =
    val eitherGen: Gen[Either[Seq[WikiError], A]] = 
      val success: Gen[Either[Seq[WikiError], A]] = resGen.map(res => Right(res))
      if !withErrors then success
      else Gen.containerOf[List, WikiError](errGen).flatMap { errors =>
        if errors.nonEmpty then errGen.map(err => Left(errors))
        else success
      }
    
    val success = eitherGen.map(ei => WikiResult(Future.successful(ei)))
    if !withFailure then success
    else Gen.oneOf(true, false).flatMap { isFailed =>
        if isFailed then wikiExceptionGen.map(err => WikiResult(Future.failed(err)))
        else success
      }

  property("zip without errors") {
    val genA = wikiResultGen[Int](false, false)
    val genB = wikiResultGen[String](false, false)
    val genAB = genA.flatMap(l => genB.map(r => l -> r))
    forAll(genAB) { (a: WikiResult[Int], b: WikiResult[String]) =>
      val obtained = a.zip(b)
      val expected = WikiResult(a.value.flatMap(eiL => b.value.map(eiR => eiL.flatMap(l => eiR.map(r => l -> r)))))

      blockAndCompare(obtained, expected)
    }
  }

  property("zip with domain errors") {
    val genA = wikiResultGen[Int](true, false)
    val genB = wikiResultGen[String](true, false)
    val genAB = genA.flatMap(l => genB.map(r => l -> r))

    forAll(genAB) { (a: WikiResult[Int], b: WikiResult[String]) =>
      val obtained = a.zip(b)

      val errs: Seq[WikiError] = a.errors ++ b.errors

      if errs.nonEmpty then
        (errs == obtained.errors) :| "Errors are not accumulated correctly"
      else 
        ((a.extractUnsafe, b.extractUnsafe) == obtained.extractUnsafe) :| "Result values are not zipped correctly"
    }
  }

  property("zip with domain errors and failures") {
    val genA = wikiResultGen[Int](true, true)
    val genB = wikiResultGen[String](true, true)
    val genAB = genA.flatMap(l => genB.map(r => l -> r))

    forAll(genAB) { (a: WikiResult[Int], b: WikiResult[String]) =>
      val obtained = a.zip(b)

      if a.failure.nonEmpty || b.failure.nonEmpty then
        sameErrorMessage(List(a, b).find(_.failure.isDefined).get.failure, obtained.failure) :| "System failure is not reported correctly"
      else 
        val errs: Seq[WikiError] = a.errors ++ b.errors
        if errs.nonEmpty then 
          (errs == obtained.errors) :| "Errors are not accumulated correctly"
        else 
          ((a.extractUnsafe, b.extractUnsafe) == obtained.extractUnsafe) :| "Results are not accumulated correctly"
    }
  }

  property("traverse without errors") {
    forAll(Gen.containerOf[List, Int](summon[Gen[Int]])) { ls =>
      val pure: (Int => String) = x => (x + 42).toString
      val toResult: (Int => WikiResult[String]) = x => WikiResult.successful(pure(x))

      val obtained: WikiResult[Seq[String]] = WikiResult.traverse(ls)(toResult)
      val expected: WikiResult[Seq[String]] = WikiResult.successful(ls.map(pure))

      blockAndCompare(expected, obtained)
    }
  }

  property("traverse with domain errors") {
    forAll(Gen.containerOf[List, WikiResult[Int]](wikiResultGen[Int](true, false))) { listOfResults =>
      val pure: (Int => String) = x => (x + 42).toString

      val obtained: WikiResult[Seq[String]] = WikiResult.traverse(listOfResults)(_.map(pure))

      if listOfResults.forall(_.errors.isEmpty) then
        val expected: Seq[String] = listOfResults.map(_.extractUnsafe).map(pure)
        blockAndCompare(WikiResult.successful(expected), obtained) :| "Result sequence is incorrect"
      else
        val expectedErrors = listOfResults.flatMap(_.errors)
        val expectedResult = WikiResult(Future.successful(Left[Seq[WikiError], Seq[String]](expectedErrors))) 
        blockAndCompare(expectedResult, obtained) :| "Errors are not reported correctly"
    }
  }

  property("traverse with domain errors and failures") {
    forAll(Gen.containerOf[List, WikiResult[Int]](wikiResultGen[Int](true, true))) { listOfResults =>
      val pure: (Int => String) = x => (x + 42).toString

      val obtained: WikiResult[Seq[String]] = WikiResult.traverse(listOfResults)(_.map(pure))

      if listOfResults.exists(_.failure.nonEmpty) then
        val failure = listOfResults.find(_.failure.nonEmpty).flatMap(_.failure).get
        val expectedResult = WikiResult.systemFailure[Seq[String]](failure.asInstanceOf[WikiException])
        blockAndCompare(expectedResult, obtained) :| "System failure is not reported correctly"
      else if listOfResults.exists(_.errors.nonEmpty) then
        val errors = listOfResults.flatMap(_.errors)
        val expectedResult = WikiResult(Future.successful(Left[Seq[WikiError], Seq[String]](errors))) 
        blockAndCompare(expectedResult, obtained) :| "Errors are not reported correctly"
      else
        val expectedResults = listOfResults.map(_.extractUnsafe).map(pure)
        (obtained.extractUnsafe.toList == expectedResults) :| "Result sequence is incorrect"
    }
  }

  property("map") {
    forAll(wikiResultGen[Int](true, true)) { (w: WikiResult[Int]) =>
      val obtained = w.map(_ + 1).map(_.toString)

      if w.failure.nonEmpty then
        sameErrorMessage(w.failure, obtained.failure) :| "map on a WikiResult failed with a system failure propagates the failure"
      else if w.errors.nonEmpty then
        (w.errors == obtained.errors) :| "map on a WikiResult failed with a domain error is correct propagates the domain error"
      else
        ((w.extractUnsafe + 1).toString == obtained.extractUnsafe) :| "map on successfull WikiResult is correct"
    }
  }

  property("flatMap with a successful function") {
    forAll(wikiResultGen[Int](true, true)) { (w: WikiResult[Int]) =>
      def f(x: Int): WikiResult[String] = WikiResult(Future.successful(Right((x + 2).toString)))
      val obtained = w.flatMap(f)

      if w.failure.nonEmpty then
        sameErrorMessage(w.failure, obtained.failure) :| "flatMap on a WikiResult failed with a system failure propagates the failure"
      else if w.errors.nonEmpty then
        (w.errors == obtained.errors) :| "flatMap on a WikiResult failed with a domain error with a successful function propagates the error"
      else
        val expected = WikiResult.successful((w.extractUnsafe + 2).toString)
        blockAndCompare(expected, obtained) :| "flatMap on successfull WikiResult with a successful function is correct"
    }
  }

  property("flatMap with a function producing a domain error") {
    forAll(wikiResultGen[Int](true, true)) { (w: WikiResult[Int]) =>
      val err = ArticleNotFound(ArticleId(42))
      def f(x: Int): WikiResult[String] = WikiResult.domainError(err)
      val obtained = w.flatMap(f)

      if w.failure.nonEmpty then
        sameErrorMessage(w.failure, obtained.failure) :| "flatMap on a WikiResult failed with a system failure is correct"
      else if w.errors.nonEmpty then
        (w.errors == obtained.errors) :| "flatMap on a WikiResult with domain error using a failing function propagates the failure"
      else
        blockAndCompare(f(42), obtained) :| "flatMap on a successfull WikiResult with a failing function produces a failed WikiResult with the correct domain error"
    }
  }

  property("flatMap with a function producing a system failure") {
    forAll(wikiResultGen[Int](true, true)) { (w: WikiResult[Int]) =>
      val ex = wikigraph.errors.WikiException.Timeout
      def f(x: Int): WikiResult[String] = WikiResult.systemFailure(ex)

      val obtained = w.flatMap(f)

      if w.failure.isDefined then
        sameErrorMessage(w.failure, obtained.failure) :| "the old failure is not reported"
      else if w.errors.nonEmpty then
        (w.errors == obtained.errors) :| "the function is not invoked if the initial result contains errors"
      else
        sameErrorMessage(obtained.failure, Some(ex)) :| "the new failure is not reported"
    }
  }

  test("breadthFirstSearch finds unique path") {
    val g: Map[ArticleId, Set[ArticleId]] = Map(
      ArticleId(1) -> Set(2 ,3).map(ArticleId(_)),
      ArticleId(2) -> Set(4).map(ArticleId(_)),
      ArticleId(3) -> Set(5).map(ArticleId(_)),
      ArticleId(5) -> Set(6).map(ArticleId(_))
    )
    val res = Wikigraph(InMemory(g)).breadthFirstSearch(ArticleId(1), ArticleId(6), 10)

    assert(res.extractUnsafe == Option(3))
  }

  test("breadthFirstSearch finds shortest path path") {
    val g: Map[ArticleId, Set[ArticleId]] = Map(
      ArticleId(1) -> Set(2 ,3).map(ArticleId(_)),
      ArticleId(2) -> Set(4).map(ArticleId(_)),
      ArticleId(3) -> Set(5).map(ArticleId(_)),
      ArticleId(5) -> Set(6).map(ArticleId(_)),
      ArticleId(6) -> Set(4).map(ArticleId(_))
    )

    val res = Wikigraph(InMemory(g)).breadthFirstSearch(ArticleId(1), ArticleId(4), 10)

    assert(res.extractUnsafe == Option(2))
  }

  test("breadthFirstSearch exits when maxDepth is reached") {
    val g: Map[ArticleId, Set[ArticleId]] = Map(
      ArticleId(1) -> Set(2).map(ArticleId(_)),
      ArticleId(2) -> Set(3).map(ArticleId(_)),
      ArticleId(3) -> Set(4).map(ArticleId(_)),
      ArticleId(4) -> Set(5).map(ArticleId(_)),
    )
    val res = Wikigraph(InMemory(g)).breadthFirstSearch(ArticleId(1), ArticleId(5), 2)

    assert(blockAndCompare(res, WikiResult.successful[Option[Int]](None)))
  }

  test("breadthFirstSearch does not fail on errors") {
    val g = Map(ArticleId(0) -> Set.empty[ArticleId])
    val res = Wikigraph(InMemory(g)).breadthFirstSearch(ArticleId(1), ArticleId(1000), 4)
    assert(blockAndCompare(res, WikiResult.successful[Option[Int]](None)))
  }

  test("distanceMatrix") {
    val g = Map(
      ArticleId(1) -> Set(2).map(ArticleId(_)),
      ArticleId(2) -> Set(3).map(ArticleId(_)),
      ArticleId(3) -> Set(1).map(ArticleId(_))
    )

    val res = Wikigraph(InMemory(g)).distanceMatrix(List("TestArticle-1", "TestArticle-3")) 
    val exp = WikiResult.successful[Seq[(String, String, Option[Int])]](Seq(("TestArticle-1", "TestArticle-3", Option(2)), ("TestArticle-3", "TestArticle-1", Option(1))))

    assert(blockAndCompare(res, exp))
  }

  property("namedLinks") {
    val links = Set(2 ,3, 4, 5).map(ArticleId(_))
    val graph: Map[ArticleId, Set[ArticleId]] = 
      Map(ArticleId(1) -> links)

    import scala.collection.convert.ImplicitConversionsToScala._

    def setUp(withErrors: Boolean, withFailure: Boolean): Prop = {
      // inject failures into the lnksFrom result
      val failableLinks: Gen[WikiResult[Set[ArticleId]]] = 
        val setOfGen = links.map(Gen.const).map(g => wikiResultGen(withErrors, withFailure)(using summon[Gen[WikiError]], g))
        Gen.sequence(setOfGen).map(set => WikiResult.traverse(set.toSeq)(identity).map(_.toSet))
  
      // inject failrues into the nameOfArticle function
      val nameSearcher: Gen[ArticleId => WikiResult[String]] =
        val all: List[ArticleId] = links.toList
        val gens: List[Gen[(ArticleId, WikiResult[String])]] = all.map { (node: ArticleId) => 
          wikiResultGen(withErrors, withFailure)(using summon[Gen[WikiError]], Gen.const(s"TestArticle-${node.raw}")).map(r => node -> r)
        }
        Gen.sequence(gens).map { listOfTuples =>
          (art: ArticleId) => 
            List.from(listOfTuples).find(_._1 == art) match
              case None => WikiResult.domainError(TitleNotFound(art))
              case Some((_, res)) => res
        }

      // zip the two generators
      val generator = 
        for 
          ls <- failableLinks
          f <- nameSearcher
        yield (ls -> f)
  
      forAll(generator) { (links, f) =>
        // Create a dummy Wikipedia instance for testing
        val client = new Wikipedia:
          override def linksFrom(art: ArticleId)(using ExecutionContext) = links
          override def nameOfArticle(art: ArticleId)(using ExecutionContext) = f(art)
          override def searchId(title: String)(using ExecutionContext) = ???
  
        val wg = Wikigraph(client)

        val result: WikiResult[Set[String]] = wg.namedLinks(graph.keys.head) 

        if links.failure.nonEmpty || links.errors.nonEmpty then
          // check when linksFrom fails or errors
          (sameErrorMessage(result.failure, links.failure) ||
            (result.errors == links.errors)) :| "did not report the error or failure issued by linksFrom"
        else
          // check nameOfArticle when linksFrom succeeds
          val results = links.extractUnsafe.map(f)
          // We need only the first failure because failures are fail-fast
          val nameFail = results.collectFirst { case w if w.failure.nonEmpty => w.failure.get }
          if nameFail.nonEmpty then
            sameErrorMessage(nameFail, result.failure) :| "did not report the failure issued by nameOfArticle"
          else 
            // check errors, which are accumulated
            val nameErrors = results.toSeq.flatMap(_.errors)
            if nameErrors.nonEmpty then
              (result.errors.toSet == nameErrors.toSet) :| "did not report the error(s) issued by nameOfArticle"
            else
              (result.extractUnsafe == results.map(_.extractUnsafe)) :| "did not construct the correct set of names"
      }
    }
    val res: Prop = 
      setUp(false, false) && 
        setUp(true, false) &&
        setUp(false, true) &&
        setUp(true, true)
    res
  }

  def property(name: String)(prop: => Prop)(using munit.Location) =
    test(name)(checkProperty(prop))

  def checkProperty(prop: Prop): Unit =
    val result = org.scalacheck.Test.check(org.scalacheck.Test.Parameters.default, prop)
    def failure(labels: Set[String], fallback: String): Nothing =
      if labels.isEmpty then throw AssertionError(fallback)
      else throw AssertionError(labels.mkString(". "))
    result.status match
      case org.scalacheck.Test.Passed | _: org.scalacheck.Test.Proved => ()
      case org.scalacheck.Test.Failed(_, labels) => failure(labels, "A property failed.")
      case org.scalacheck.Test.PropException(_, e: munit.FailException, labels) => failure(labels, e.message)
      case org.scalacheck.Test.PropException(_, e, labels) => failure(labels, s"An exception was thrown during property evaluation: $e")
      case org.scalacheck.Test.Exhausted => failure(Set.empty, "Unable to generate test data.")

end WikigraphSuite
