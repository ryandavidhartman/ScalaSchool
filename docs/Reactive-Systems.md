## Reactive Systems are software applications written based the principles outlined in the [Reactive Manifesto](https://www.reactivemanifesto.org/).

In general reactive applications are written with the following concepts in mind:

* React to events -> event driven (or message driven)
* React to load -> scalable (or elastic)
* React to failures -> resilient
* React to users -> responsive


![reactive-systems](https://github.com/ryandavidhartman/ScalaSchool/blob/master/imgs/reactive-traits.svg)

## Event Driven
Traditionally: Systems are composed of multiple threads, which communicate with shared, synchronized state.
* Strong coupling, hard to compose.

Now: Systems are composed from loosely coupled event handlers.
* Events can be handled asynchronously, without blocking.
* This allows for the efficient usage of resources.

## Scalable
An application is scalable if it is able to be expanded according to its usage

* scale up: makes use of parallelism in multi-core systems
* scala out: make use of multiple server nodes

Important for scalability: Minimum use of mutable state
Important for scale out: Location transparency, resilience.

## Resilience:
An application is resilient if it can recovery quickly from failures.

Failures can be:

* software failures
* hardware failures, or
* connection failures

Typically, resilience cannot be added as an afterthought; it needs to be part of the design from the beginning.

Needed:

* loose coupling
* strong encapsulation of state
* pervasive supervisor hierarchies

## Responsive
An application is responsive if it provides rich, real-time interaction with its users even under load and in the presence of failures.

Responsive applications can be build on a event-driven, scalable, and resilient architecture.

Even with these things careful attention to algorithm design, back-pressure and many other details.

