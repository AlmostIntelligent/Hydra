#!/usr/bin/env python

import os
import threading

class StreamListener:

	def __init__(self, name = "Listener", process = None, startCallback = None, endCallback = None):
		self._name = name
		self._listener = []
		self._thread = None
		self._pHnd = process
		self._startCallback = startCallback
		self._endCallback = endCallback
		
	def addListener(self, listener):
		self._listener.append(listener)
		
	def removeListener(self, listener = None, all = False):
		if not all:
			del self._listener[listener]
		else:
			self._listener = []
			
		
	def listen(self, stream, listener = None):
		if listener is not None:
			self.addListener(listener)
		if self._thread is None:
			self._thread = threading.Thread(target=self.__listener, args=(stream,), name=self._name)
			self._thread.daemon = True
			self._thread.start()
		return self._thread
	
	def __listener(self, stream):
		line = ''
		self._startCallback()
		while self._pHnd is not None and self._pHnd.poll() == None:
			char = stream.read(1)
			if char != '\n':
				line += char
			else:
				#sys.stdout.write("line: {}\n".format(line) )
				for l in self._listener:
					l(line)
				line = ''
		
		self._endCallback()
		self._pHnd = None
