// Agent agentTemperature in project intelligentOffice

/* Initial beliefs and rules */

stateOfAC(0).
window(closed).

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Activated").

+need(stop) : true & window(closed) <- !stop.

+need(heating) : true & window(closed) <- !heat.

+need(cooling) : true & window(closed) <- !cool.

+!window : window (closed) <- -+window(opened); .wait(50); !stop.
+!window.

+!nowindow : window (opened) <- -+window(closed); !heat; !cool.
+!nowindow.

+!stop : window(opened) & not stateOfAC(0) <- turnoff(ac); -+stateOfAC(0).
+!stop : need(stop) & not stateOfAC(0) <- turnoff(ac); -+stateOfAC(0).
+!stop.

+!heat : window(closed) & need(heating) & not stateOfAC(1) <- heaton(ac); -+stateOfAC(1).
+!heat : need(heating) & not stateOfAC(1) <- !heat.
+!heat.

+!cool : window(closed) & need(cooling) & not stateOfAC(2) <- coolon(ac); -+stateOfAC(2).
+!cool : need(cooling) & not stateOfAC(2) <- !cool.
+!cool.

										