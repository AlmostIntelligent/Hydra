Hydra
=====

by 
	Christian Kulpa <chris@gethydrated.org>,
	Hanno Sternberg <hanno@gethydrated.org>


What is Hydra?
--------------

Hydra is a distributed computing network.

The name originates from the creature *Lernaean Hydra* from the greek mythology. The *Lernaean Hydra* was a creature with nine heads. Killing this creature was one of the twelve Labours of Heracles. Whenever Hercules sliced off one head, two new heads appeared.

This computing network is like a hydra, with its multiple heads, each head with its own brain.



Network structure
-----------------


### The computing node: *head*

The network is organised in computing nodes called *heads*. A *head* can be a dedicated or a virtual machine. 
Theoretically it is also possible to run multiple instances on the same machine. The sense of this is open for discussion.

Each head act as a host for processes.


### Crunching the numbers: The *process*

A process is a piece of (program-) code. 

The network shares the same process code. New nodes automatically distribute and receive the process code to and from the network.
Inside the network every line of code is seen as "trusthworty". Meaning, there is no malicious code.


### Differentiation of heads: The *roles*

Based on its (hardware-) configurations a head has multiple *roles*. 
Each process can require specific roles to fulfill its duties.

As a result, a process can only be *invoked* on a head that provides these prerequisites.
Examples for these *roles* are:
	> - Network connection
	> - Dedicated graphics card


### Getting work done: The *service*

A *service* is a set of processes. By starting a service, one or more processes are invoked inside the net.


### Solving a problem: The *application*

An *application* uses multiple services to solve a problem. 


 




