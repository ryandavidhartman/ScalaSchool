// Question 1

// What is the difference between "hello world" vs println("hello world")

val q1a = "hello world"
q1a.isInstanceOf[String]

val q1b = println("hello world")
q1b.isInstanceOf[Unit]

// Questions 2  what is the value of someValue?
val someValue = {
  2 < 3
}

// Question 3 what is the value of someOtherValue
val someOtherValue = {
  if(someValue) 239 else 986
  42
}
