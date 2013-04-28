#!/usr/bin/env python

import os
import sys
import time
import subprocess
import threading

class Hydra():

	def __init__(self, path = ""):
		self._path = path
		self._pHnd = None
		self._listenThread = {}
		self._listener = {}
	
	def start(self):
		print "Hydra executable: {}".format(self._path)
		self._pHnd = subprocess.Popen(self._path, shell=False, bufsize=1, universal_newlines=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, stdin=subprocess.PIPE)
		print "Hydra process started with PID {}".format(self._pHnd.pid)
		
	def shutdown(self):
		if self._pHnd is not None:
			self.write("local")
			self.write("shutdown")
			self._listenThread = {}
			self._listener = {}
	
	def write(self, msg):
		if self._pHnd is not None:
			self._pHnd.stdin.write(msg.strip()+"\n")
			self._pHnd.stdin.flush()
	
	def addListener(self, type, listener):
		if not type in self._listener:
			self._listener[type] = [listener]
		else:
			self._listener[type].append(listener)
		
	def removeListener(self, type, listener = None, all = False):
		if not all:
			if type in self._listener:
				self._listener[type].remove(listener)
		else:
			self._listener[type] = []
			
	
	def stdout(self):
		return self._pHnd.stdout
		
	def stderr(self):
		return self._pHnd.stderr
	
	def listen(self, type, stream, listener = None):
		if listener is not None:
			self.addListener(type, listener)
		if not type in self._listenThread:
			self._listenThread[type] = threading.Thread(target=self.__listener, args=(type,stream), name="Hydra-Listener-{}".format(type))
			self._listenThread[type].daemon = True
			self._listenThread[type].start()
	
	def __listener(self, type, stream):
		line = ''
		print "Listener {} started".format(type)
		while self._pHnd is not None and self._pHnd.poll() == None:
			char = stream.read(1)
			if char != '\n':
				line += char
			else:
				#sys.stdout.write("line: {}\n".format(line) )
				for l in self._listener[type]:
					l(line)
				line = ''
		print "Listener {} stopped".format(type)
		self._pHnd = None
