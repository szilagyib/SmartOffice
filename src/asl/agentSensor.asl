// Agent agentSensor in project intelligentOffice

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Activated").

+present(co) <- .print("ALARM"); .send(agentUser, achieve, home); .send(agentWindow, achieve, critical).			

+absent(co) <- .send(agentUser, achieve, again); .send(agentWindow, achieve, safe).