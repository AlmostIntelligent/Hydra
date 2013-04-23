Hydra
=====

Started in 
	summer semester 2012 
at the
	[University of Applied Science, Wedel](http://fh-wedel.de)


Created by:
	Christian Kulpa <chris@gethydrated.org>,
	Hanno Sternberg <hanno@gethydrated.org>
	
Overseen by:
	Prof. Dr. Ulrich Hoffmann


What is Hydra?
--------------

Hydra is a distributed computing network.

The name originates from the creature out of the greek mythology the *Lernaean Hydra*. 
It was described as a creature with nine heads, one immortal in the center and eight mortal heads around. 
Killing this creature was one of the twelve Labours of Heracles. 
Whenever Hercules sliced off one head, two new heads appeared. 
He could only overcome the creature, because of his nephew Iolaus, 
who cauterized the stumps of the heads after Hercules decapitated them.

This distributed computing network with its nodes is like a hydra with its heads.
Hydra acts as a distributed service-plattform. 

The structure
-------------

Like every other network, the hydra is build from different interconnected nodes.
A node can either be a dedicated or a virtual machine. 
It is even possible to connected two JVMs on the same host machine.

The network is fully interconnected, allowing every node to directly communicate
with the others. By monitoring the TCP connections, the nodes can easily detect,
if a node is still up and reachable.

The nodes share a global registry, which holds information about a network structure.
Each time a new nodes connects into the network, it will receive a replica of the registry.
It is seen as consensus, that the registry is always in sync. 
The ability to force a re-sync will be added later.

The network shares an eventstream, acting as an abstraction layer for the 
interprocess communication.

A single Node inside the net has two main layers.
On the bottom layer, their is an actor system, encapsulating every process, so 
they behave like being atomic. 

Each actor can listen to specific messages inside the eventstream and can send 
messages in response. 
The system will also generate eventmessages, if for example a new node connects
to the network.

In the layer above the actor system, their are services.
A service can be everything, from a single (low level) database query to a 
complex business application, build from other lower level services.

This layer holds the "userland" - the application forming code.
As a result, it should by atypical to have the need to write an actor as part
of a user service or application.



