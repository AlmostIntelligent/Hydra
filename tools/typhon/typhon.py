#!/usr/bin/env python

import os
import time
import threading

# Tk import
from Tkinter import *
from ttk import *
import tkFileDialog as fileDiag
import tkMessageBox as mbox

from components.scrolltext import ScrollText

from hydra import Hydra

class TyphonGUI():
	
	def __init__(self):
		
		self._hydra = None
		#self._hydra = Hydra('python echo.py')
	
		self._wHnd = Tk()
		self._wHnd.title("Hydra")
		self._wHnd.columnconfigure(0, weight=1)
		self._wHnd.rowconfigure(0, weight=1)
		
		self._cmdVar = StringVar()
		
		self._mMenu = Menu(self._wHnd)
		self._fMenu = Menu(self._mMenu, tearoff=0)
		self._fMenu.add_command(label="Start", command=self.start, accelerator="Ctrl+s")
		self._fMenu.add_command(label="Restart", command=self.restart, accelerator="Ctrl+r")
		self._fMenu.add_command(label="Shutdown", command=self.stop)
		self._fMenu.add_command(label="Quit", command=self.close, accelerator="Ctrl+q")
		
		self._logText = ScrollText(self._wHnd)
		self._command = Entry(self._wHnd, textvariable=self._cmdVar)
		self._send = Button(self._wHnd, text="Send", command=self.execute)
		
		self._logText.tagEdit("in", foreground="blue")
		#self._logText.tagEdit("out", foreground="blue")
		self._logText.tagEdit("err", foreground="red")
		self._logText.tagEdit("note", foreground="yellow", background="red")
		
		self._mMenu.add_cascade(label="Hydra", menu=self._fMenu)
		
		self._wHnd.config(menu=self._mMenu)
		
		self._logText.grid(row=0, column=0, columnspan=2,  sticky=N+S+W+E)
		self._command.grid(row=1, column=0, sticky=W+E)
		self._send.grid(row=1, column=1)
		
		self._wHnd.protocol("WM_DELETE_WINDOW", self.close)
		self._wHnd.bind("<Control-q>", lambda e:self.close() )
		self._wHnd.bind("<Control-s>", lambda e:self.start() )
		self._wHnd.bind("<Control-r>", lambda e:self.restart() )
		self._command.bind("<Return>", lambda e:self.execute() )
		
	def execute(self):
		text = self._cmdVar.get()
		self._cmdVar.set("")
		self._logText.append("> "+text, "in")
		self._hydra.write(text)
		
	def _textListener(self, text, type = "out"):
		self._logText.append("< "+text, type)

	def close(self):
		if self._hydra is not None:
			if mbox.askokcancel("Quit", "This will shutdown the hydra.\n Do you wish to continue?"):
				self._hydra.shutdown()
				print "Awaiting for process termination"
				time.sleep(1)
				self._wHnd.destroy()
		else:
			self._wHnd.destroy()
	
	def start(self):
		if self._hydra is None:
			fname = fileDiag.askopenfilename()
			if fname != "":
				self._hydra = Hydra(fname)
				self.startHydra()
			
	def restart(self):
		self._textListener("Restarting ....", "note")
		self._hydra.shutdown()
		self.startHydra()
			
	def startHydra(self):
		self._hydra.start()
		self._hydra.listen("out", self._hydra.stdout(), lambda t: self._textListener(t, "out") )
		self._hydra.listen("err", self._hydra.stderr(), lambda t: self._textListener(t, "err") )
		
	def stop(self):
		if self._hydra is not None:
			self._hydra.shutdown()
			self._hydra = None
			
			
		
	def show(self):
		self._wHnd.mainloop()



def typhon():
	gui = TyphonGUI();
	gui.show()
	
	

if __name__ == "__main__":
	typhon()

