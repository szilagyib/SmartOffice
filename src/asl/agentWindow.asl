// Agent agentWindow in project intelligentOffice

/* Initial beliefs and rules */

window(closed).
co(absent).

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Activated").

+need(open) : true <- !open.

+need(close) : true & co(absent) <- !close.

+!safe <- -+co(absent); !close.
+!safe.

+!critical <- -+co(present); !open.
+!critical.

+!open : co(present) & window(closed) <- open(window); .send(agentTemperature, achieve, window); -+window(opened).								
+!open : co(absent) & need(open) & window(closed) <- open(window); .send(agentTemperature, achieve, window); -+window(opened).
+!open.

+!close : co(absent) & need(close) & window(opened) <- close(window); .send(agentTemperature, achieve, nowindow); -+window(closed).
+!close.


