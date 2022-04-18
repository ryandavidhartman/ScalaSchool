package example.streams

/**
 * Oof I no longer remember what I was going to do with this stuff!
 */


sealed trait ProcessStream[I,O]

case class HaltStream[I,O]() extends ProcessStream[I,O]

case class EmitStream[I,O](head: O, tail: ProcessStream[I,O] = HaltStream[I,O]()) extends ProcessStream[I,O]

case class AwaitStream[I,O](recv: Option[I] => ProcessStream[I,O]) extends ProcessStream[I,O]
