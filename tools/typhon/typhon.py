#!/usr/bin/env python

import os
import time
import threading
import subprocess

# Tk import
from Tkinter import *
from ttk import *
import tkFileDialog as fileDiag
import tkMessageBox as mbox

from streamListener import StreamListener

from components.scrolltext import ScrollText
from components.optiondialog import OptionDialog

from hydra import Hydra

class TyphonGUI():
	
	def __init__(self):
		
		self._hydra = None
		self._gradlePath = os.path.abspath("../../")
		self._typhonPath = os.getcwd()
	
		self._wHnd = Tk()
		self._wHnd.title("Hydra")
		self._wHnd.columnconfigure(0, weight=1)
		self._wHnd.rowconfigure(0, weight=1)
		
		self._cmdVar = StringVar()
		
		self._mMenu = Menu(self._wHnd)
		self._fMenu = Menu(self._mMenu, tearoff=0)
		self._gMenu = Menu(self._mMenu, tearoff=0)
		
		self._fMenu.add_command(label="Start", command=self.start, accelerator="Ctrl+s")
		self._fMenu.add_command(label="Restart", command=self.restart, accelerator="Ctrl+r")
		self._fMenu.add_command(label="Shutdown", command=self.stop)
		self._fMenu.add_command(label="Quit", command=self.close, accelerator="Ctrl+q")
		
		self._gMenu.add_command(label="all", command=lambda:self._gradle("build dist -x test javadoc"), accelerator="Ctrl+a")
		self._gMenu.add_command(label="build", command=lambda:self._gradle("build"), accelerator="Ctrl+b")
		self._gMenu.add_command(label="dist", command=lambda:self._gradle("dist"), accelerator="Ctrl+d")
		self._gMenu.add_command(label="clean", command=lambda:self._gradle("clean"), accelerator="Ctrl+c")
		self._gMenu.add_command(label="test", command=lambda:self._gradle("test"), accelerator="Ctrl+t")
		self._gMenu.add_command(label="custom", command=lambda:self._gradle(), accelerator="Ctrl+g")
		
		self._logText = ScrollText(self._wHnd)
		self._command = Entry(self._wHnd, textvariable=self._cmdVar)
		self._send = Button(self._wHnd, text="Send", command=self.execute)
		
		
		self._logText.tagEdit("in", foreground="blue")
		self._logText.tagEdit("err", foreground="red")
		self._logText.tagEdit("note", foreground="yellow", background="red")
		
		self._mMenu.add_cascade(label="Hydra", menu=self._fMenu)
		self._mMenu.add_cascade(label="Gradle", menu=self._gMenu)
		
		self._wHnd.config(menu=self._mMenu)
		
		self._logText.grid(row=0, column=0, columnspan=2, sticky=N+S+W+E)
		self._command.grid(row=1, column=0, sticky=W+E)
		self._send.grid(row=1, column=1)
		
		self._wHnd.protocol("WM_DELETE_WINDOW", self.close)
		self._wHnd.bind("<Control-q>", lambda e:self.close() )
		self._wHnd.bind("<Control-s>", lambda e:self.start() )
		self._wHnd.bind("<Control-r>", lambda e:self.restart() )
		self._wHnd.bind("<Control-a>", lambda e:self._gradle("build dist -x test javadoc"))
		self._wHnd.bind("<Control-b>", lambda e:self._gradle("build -x test javadoc"))
		self._wHnd.bind("<Control-d>", lambda e:self._gradle("dist"))
		self._wHnd.bind("<Control-c>", lambda e:self._gradle("clean"))
		self._wHnd.bind("<Control-t>", lambda e:self._gradle("test"))
		self._wHnd.bind("<Control-g>", lambda e:self._gradle())
		self._command.bind("<Return>", lambda e:self.execute() )
		
				
	def execute(self, text = None):
		if text is None:
			text = self._cmdVar.get()
		self._cmdVar.set("")
		self._logText.append("> "+text, "in")
		self._hydra.write(text)
		
	def _gradleInit(self, args):
		self._textListener("Starting: gradlew {}".format(args), "note")
		 
	def _gradleFinish(self):
		self._textListener(" ... gradlew has finished", "note")
		
	def _gradle(self, args=None):
		if self._gradlePath is None or not os.path.exists(os.path.abspath(self._gradlePath + "/gradlew")):
			fname = fileDiag.askdirectory(title="Hydra root directory")
			if fname != "":
				self._gradlePath = fname
			else:
				self._gradlePath = None
		if self._gradlePath is not None:
			if args is None:
				args = str(OptionDialog(self._wHnd, "Gradle arguments", "Custom arguments:", "").result)
			if os.name == "nt":
				p = os.path.abspath(self._gradlePath +"/gradlew.bat")+" "+args
			else:
				p = os.path.abspath(self._gradlePath +"/gradlew")+" "+args
			proc = subprocess.Popen(p, shell=True, stdout = subprocess.PIPE, stderr = subprocess.STDOUT, cwd=self._gradlePath, universal_newlines=True)
			listener = StreamListener( 
				process = proc,
				name = "gradlew listener",
				startCallback=lambda: self._gradleInit(args), 
				endCallback = lambda: self._gradleFinish()
			)
			listener.listen(proc.stdout, listener=self._textListener)
		
			
	
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
			fname = fileDiag.askopenfilename(title="Hydra path")
			if fname != "":
				self._hydra = Hydra(fname)
				self._textListener("Starting ....", "note")
				self.startHydra()
			
	def restart(self):
		if self._hydra is None:
			self._textListener("Starting ....", "note")
			if os.name == "nt":
				self._hydra = Hydra(os.path.abspath("../../build/assemble/bin/hydra.bat"))
			else:
				self._hydra = Hydra(os.path.abspath("../../build/assemble/bin/hydra"))
		else:
			self._textListener("Restarting ....", "note")
			self._hydra.shutdown()
		self.startHydra()
			
	def startHydra(self):
		self._hydra.start()
		self._hydra.listen("out", self._hydra.stdout(), lambda t: self._textListener(t, "out") )
		self._hydra.listen("err", self._hydra.stderr(), lambda t: self._textListener(t, "err") )
		
	def stop(self):
		if self._hydra is not None:
			self._textListener("Stopping ....", "note")
			self._hydra.shutdown()
			self._hydra = None
		
	def show(self):
		self._wHnd.mainloop()

def typhon():
	gui = TyphonGUI();
	gui.show()

if __name__ == "__main__":
	typhon()

