#!/usr/bin/env python

from Tkinter import *
from ttk import *


class Dialog(Toplevel):
	def __init__(self, parent, title = None, defaultResult = None):
		Toplevel.__init__(self, parent)
		self.transient(parent)

		if title: self.title(title)

		self.parent = parent
		self.result = defaultResult

		body = Frame(self)
		self.initial_focus = self.body(body)
		body.pack(padx=5, pady=5, expand=1, fill=BOTH)
		self.buttonbox()
		self.grab_set()

		if not self.initial_focus: 
			self.initial_focus = self

		self.protocol("WM_DELETE_WINDOW", self.cancel)
		self.geometry("+%d+%d" % (parent.winfo_rootx()+50,parent.winfo_rooty()+50))
		self.initial_focus.focus_set()
		self.wait_window(self)

	def body(self, master):
		pass

	def buttonbox(self):
		box = Frame(self)
		w = Button(box, text="OK", width=10, command=self.ok, default=ACTIVE)
		w.pack(side=LEFT, padx=5, pady=5)
		w = Button(box, text="Cancel", width=10, command=self.cancel)
		w.pack(side=LEFT, padx=5, pady=5)
		self.bind("<Return>", self.ok)
		self.bind("<Escape>", self.cancel)
		box.pack()

	def ok(self, event=None):

		self.withdraw()
		self.update_idletasks()

		self.apply()

		self.cancel()

	def cancel(self, event=None):
		self.parent.focus_set()
		self.destroy()

	def apply(self):
		pass
	
class OptionDialog(Dialog):

	def __init__(self, parent, title, option, value=None, entryType=Entry):
		self._label = option
		self._entryType = entryType
		Dialog.__init__(self, parent, title=title, defaultResult = value)

	def body(self, master):

		Label(master, text=self._label).grid(row=0, column=0)
		master.grid_rowconfigure(0, weight=1)
		master.grid_columnconfigure(1, weight=1)
		
		self.val = self._entryType(master)
		if self._entryType is Text:
			self.val.insert(1.0, self.result)
		else:
			self.val.insert(1, self.result)

		self.val.grid(row=0, column=1, sticky=N+S+E+W)
		return self.val # initial focus

	def apply(self):
		
		if self._entryType is Text:
			val = self.val.get(1.0, END)
		else:
			val = self.val.get()
		if val.strip() != "":
			self.result = val