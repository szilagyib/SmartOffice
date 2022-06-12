// Agent agentUser in project intelligentOffice

/* Initial beliefs and rules */

co(absent).

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Activated"); !move.

+!move : true & co(absent) <- move(user); .wait(1000); !move.
+!move : true & co(present) <- home(user); .wait(1000); !move.
+!move.

+!home <- -+co(present).
+!home.

+!again <- -+co(absent).
+!again.

