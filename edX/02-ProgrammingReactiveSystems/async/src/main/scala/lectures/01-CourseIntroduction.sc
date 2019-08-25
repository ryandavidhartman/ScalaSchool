/*
What are we going to cover in this course?
First off we'll discuss how to create reactive systems
see https://github.com/ryandavidhartman/ScalaSchool/wiki/Reactive-Systems
 */

/*
The very foundation of reactive systems are writing event driven
or better yet message driven systems
 */

/* Call backs are a tradition way of handling writing
event driven systems
 */

import java.awt.Button
import java.awt.event.{ActionEvent, ActionListener}

class Counter extends ActionListener {
  private var count = 0
  private val button: Button = new Button()
  button.addActionListener(this)

  def actionPerformed(e: ActionEvent): Unit = {
    count += 1
  }
}

/*
Problems with callbacks:
Event handlers return `Unit` this means we are required to use
shared mutable state.

Event handlers can't be composed easily

this is the so called "call-back hell"
 */

/*
How to do better:
Use fundamental constructions from function programming to get
composable event abstractions.

Events are first class
Events are often represented as messages
Handlers of events are also first class
Complex handlers can be composed from primitive ones

see https://en.wikipedia.org/wiki/First-class_citizen
 */

/* what we are going to cover in the class

Review of functional programming
Abstracting over events: futures
Message passing architecture: actors
Handling failures: supervisors
Scaling out: distributed actors
Abstracting over event streams: reactive streams, flows
 */
