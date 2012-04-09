Hydra
=====

Started in 
	Summersemester 2012 
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

This computing network is like a hydra, with its multiple heads, each head with its own brain.
By not implementing the decapitation feature, there should be no way of shutting down (killing) this "network-beast".

(Memo to self) Name the shutdown procedure "cauterize".



Anatomy of a beast: The Network structure
-----------------------------------------


### The computing node: *head*

The network is organised in computing nodes called *heads*. A *head* can be a dedicated or a virtual machine. 
Theoretically it is also possible to run multiple instances on the same machine. 
The sense of this is open for discussion.

The nodes test their connection to each other by frequently sending ping-like request.
After a change in the structure, they (re-)elect an *Immortal* head (there can be only one), which serves their coordinator.
Possible election algorithms:
	- [Bully Algorithm](http://en.wikipedia.org/wiki/Bully_Algorithm "Bully Algorithm in the Wikipedia")

Each head act as a host for processes.


### Crunching the numbers: The *process*

A process is a piece of (program-) code. Every process is packed in one JAR file to ensure its transferability.

The network shares the same process code. 
New nodes automatically distribute and receive the process code to and from the network.
Inside the network every line of code is seen as "trusthworty". Meaning there is no malicious code.


### Differentiation of heads: The *roles*

Based on its (hardware-) configurations a head has multiple *roles*. 
Each process can require specific roles to fulfill its duties.

As a result, a process can only be *invoked* on a head that provides these prerequisites.

Examples for these *roles* are:

- Network connection
- Dedicated graphics card


### Getting work done: The *service*

A *service* is a set of processes. By starting a service, one or more processes are invoked inside the net.
The invokation can happen on multiple *nodes* at the same time. 
A service can't rely on every node in the the net for execution of processes. 
Therefore there must be a handler to react, if 

a. A node with an externally invoked process shuts down / looses its network connection.
b. A node that has invoked processes on other nodes shuts down / looses its connection.


### Solving a problem: The *application*

An *application* uses multiple services to solve a problem. An example can be the encoding of a movie file.

> The application "Movie Render" uses the service "Encode MPG", and "Stream to Disk".
>
> The service "Encode MPG" invokes several "Encode" processes on heads with encoding hardware.
>
> The service "Stream to disk" invokes a process, that saves the result of the encoding to a HDD.

On Screen: The communication
----------------------------

### Base Protocoll

__*TCP* or *UDP* - that is the question.__
Both have their (dis-) advantages over each other.

With a growing number of nodes in the network, managing the TCP-socket can get tough.
On the other hand, the TC-Protocol itself takes care of resending packets if they get lost.

(Open for discussion) Maybe a look into the thesis of chris can help finding the best solution.

### Packetformat

The network packets can be formatted using the *XML* or the *JSON*-language.
*JSON* is a bit more lightweight than *XML* , but since both formats are a representation of a tree, 
they should be exchangable.

A basic packet possibly has the following structure. (This is open for discussion)

		\--root
		   +--Version
		   +--Type
		   +--Command
		   |  +--Command type
		   |  \--Command parameters
		   \--Payload
		      +--payload length
		      +--payload type
		      \--payload data


Getting on with the evolution: Future feature
---------------------------------------------

### One layer on top

Implementing a network layer on top to create a communication between multiple networks. 
This could lead towards a decentralized, distributive computation network.

In this network their could be malicious code, which makes the security of code a
prime aspect of the implementation.

The abstract idea behind this layer is a network, that provides the users with the ability to
access computational resources if needed with autmatic load balancing over the whole network.

My personal example is this:

> I record a video with my smartphone, uploaded the raw material to a cloud server and now want to cut it.
> So I start my cutting application on that smartphone (or any other device in my reach, like a tablet).
> 
> As those devices are connected to my Hydranet ("Being hydrated"),
> I can cut the movies directly on my device and render them remotely.
> By starting the rendering of the new movie with possibly another encoding, my device automatically selecteds
> the nearest Hydra (getting location information by GPS for example) that provides the service *Render Movie*.
> Those head could be one of my one machines, or a machine inside my *peer group* or *ring of trust*.
> 
> The Hydra starts rendering my movie by using its own hardware and/or delegating the work to other hydras inside
> its ring of trust.
>
> As a result, it could be possible, that the movie, I recorded in my vacation in Berlin could be rendered 
> on a Hydra in Hamburg which delegates the work to other Hydras all over the world.
