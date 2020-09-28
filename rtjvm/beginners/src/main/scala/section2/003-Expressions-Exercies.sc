// Question 1

// What is the difference between "hello world" vs println("hello world")

val q1a = "hello world" // this is called a string literal
q1a.isInstanceOf[String]

val q1b = println("hello world") // this is an expression of type unit, and it has a side effect
q1b.isInstanceOf[Unit]

// Questions 2  what is the value of someValue?
val someValue = {  //this is a value of type Boolean, with value  of true
  2 < 3
}

// Question 3 what is the value of someOtherValue
val someOtherValue = {  // this is an expression of type Int with value 42
  if(someValue) 239 else 986
  42
}

// No else if key word
// Since if else is an Expression we need no else if statement

val x = 5
val message = if(x < 0)
  "negative"
else if (x > 1000)
  "to big"
else
  "just right"


