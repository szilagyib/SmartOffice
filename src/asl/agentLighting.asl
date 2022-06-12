// Agent agentLighting in project intelligentOffice

/* Initial beliefs and rules */

light(off).

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Activated.").

+present(user) : true <- !lightOn.

+absent(user) : true <- !lightOff.


+!lightOn : present(user) & light(off) <-	turnon(light); -+light(on).
+!lightOn.

+!lightOff : absent(user) & light(on) <- 	turnoff(light); -+light(off).
+!lightOff.
