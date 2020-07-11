/*
Exercise 1.1

Prove that Fib(n) is the closest integer to

ϕn/√5, where ϕ = (1 + √5)/2.

Hint: Let ψ = (1 −√5)/2.


Use induction and the definition of the Fibonacci numbers
(see Section 1.2.2) to prove that Fib(n) = (ϕn − ψn)/√5.

 */

/*

STEP 1 Note
ϕ0 = 1
ϕ1 = (1 + √5)/2
ϕ2 = (3 + √5)/2
ϕ3 = (4 + 2√5)/2
ϕ4 = (7 + 3√5)/2
ϕ5 = (11 + 5√5)/2

STEP 2 Observe
ϕ2 - ϕ1 = 1 = ϕ0            =>  ϕ2 = ϕ1 + ϕ0
ϕ3 - ϕ2 = (1 + √5)/2 = ϕ1   =>  ϕ3 = ϕ2 + ϕ1
ϕ4 - ϕ3 = (3 + √5)/2 = ϕ2   =>  ϕ4 = ϕ3 + ϕ2
ϕ5 - ϕ4 = (4 + 2√5)/2 = ϕ3  =>  ϕ5 = ϕ4 + ϕ3

say ϕn = ϕn-1 + ϕn-2
consider ϕn+1 = ϕ*(ϕn) =  ϕ*(ϕn-1 + ϕn-2) = ϕn + ϕn-1
Therefore given
 ϕn = ϕn-1 + ϕn-2  => ϕn+1 = ϕn + ϕn-1


Step 3
Using Step 1 + Step2 proves by induction

 ϕn = ϕn-1 + ϕn-2

STEP 4 Note
ψ0 = 1
ψ1 = (1 - √5)/2
ψ2 = (3 - √5)/2
ψ3 = (4 - 2√5)/2
ψ4 = (7 - 3√5)/2
ψ5 = (11 - 5√5)/2

STEP 5 Observe
ψ2 - ψ1 = 1 = ψ0            =>  ψ2 = ψ1 + ψ0
ψ3 - ψ2 = (1 - √5)/2 = ψ1   =>  ψ3 = ψ2 + ψ1
ψ4 - ψ3 = (3 - √5)/2 = ψ2   =>  ψ4 = ψ3 + ψ2
ψ5 - ψ4 = (4 - 2√5)/2 = ψ3  =>  ψ5 = ψ4 + ψ3

say ψn = ψn-1 + ψn-2
consider ψn+1 = ψ*(ψn) =  ψ*(ψn-1 + ψn-2) = ψn + ψn-1
Therefore given
 ψn = ψn-1 + ψn-2  => ψn+1 = ψn + ψn-1

 Step 6
 Using Step 1 + Step2 proves by induction
 ψn = ψn-1 + ψn-2

 Put it all together:

 ϕ0 - ψ0 = 1 - 1 = 0     = √5 * Fib(0)
 ϕ1 - ψ1 = 2*√5/2 = √5   = √5 * Fib(1)
 ϕ2 - ψ2 = 2*√5/2 = √5   = √5 * Fib(2)
 ϕ3 - ψ3 = 4*√5/2 = 2√5  = √5 * Fib(3)

 SAY √5 * Fib(n) = ϕn - ψn

 Consider ϕn+1 - ψn+1 =
                      = ϕn + ϕn-1  - ψn+1   (From step3)
                      = ϕn + ϕn-1 - (ψn + ψn-1)   (From step 6)
                      = (ϕn - ψn) + (ϕn-1 - ψn-1)
                      =  √5 * Fib(n) +  √5 * Fib(n-1)
                      =  √5 * (Fib(n) + Fib(n-1))
                      =  √5 * Fib(n+1)  // From the definition of Fib

THEREFORE given
√5 * Fib(n) = ϕn - ψn  =>  ϕn+1 - ψn+1 = √5 * Fib(n+1

so by induction
 √5 * Fib(n) = ϕn - ψn  =>  Fib(n) = (ϕn - ψn)/ √5


*/

