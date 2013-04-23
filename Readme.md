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

The nodes share a global registry, which holds information about a network 
structure. Each time a new nodes connects into the network, it will receive a 
eplica of the registry. It is seen as consensus, that the registry is always in 
sync. The ability to force a re-sync will be added later.

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

Build & run instructions
------------------------

### Using a command line

Hydra uses the [gradle](http://www.gradle.org/) buildsystem.
It comes bundled with a wrapper script `gradlew`.

Assuming the repository is already checked out, open a command line and type:

	gradlew build dist
	
This will start the build process. The initial build will take a bit longer as 
usual as gradle downloads all the dependencies needed for the build.

Hydra can be run from the command line by executing the 
`build\assemble\bin\hydra` startup script. This script can also be executed 
from a graphical file browser.

### Using the bundled `typhon` tool

Inside the `tools\typhon` directory is a python script (`typhon.py`) 
which provides a small command line interface to hydra.
It will try to locate the gradle wrapper and offers the ability to build and 
start a local hydra instance.


Usage instructions
------------------

After successfully starting the hydra instance the builtin command line 
interface will open up. This serves a the main administration tool.

### Connecting to another node / network

To connect to another network you need to set up your own network connector 
interface.
This is done by typing

	port <PORT>
	
into the command line. `<PORT>` is the network port, on which the hydra will 
listen to incoming packets.

A connection to a remote node can be established using the `connect` command.

	connect <REMOTE_IP> <REMOTE_PORT>
	
`<REMOTE_IP>` is the address of the remote node; `<REMOTE_PORT>` refers to the 
port on which the remote node is listening.

After the connection is established, the two nodes will exchange information 
about their peer network and start interconnecting to every other node inside 
the network.

### Switching between nodes

It is possible to switch between the nodes inside the network, from the builtin
command line interface.

Type

	nodes
	
to get a list of all nodes in the network.

To switch to another node type

	node <Number>
	
where `<Number>` is the number of the node in the `nodes` list.

To switch back to your local node type

	local

### Node shutdown

To shutdown your hydra instance, type 

	shutdown
	
### Complete Command Reference

The complete reference for all commands can be found in the wiki.



