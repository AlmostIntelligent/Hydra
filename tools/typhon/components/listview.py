#!/usr/bin/env python

from Tkinter import *
from ttk import *


class ListView(LabelFrame):

	def __init__(self, parent, caption="Listview", items = {}, updateFunc = None):
		LabelFrame.__init__(self, parent, text=caption)
		
		self.columnconfigure(0, weight=1)
		
		self._scroll = Scrollbar(self, orient=VERTICAL)
		self._scroll.grid(row=0, column=2, sticky=N+S)
		
		self._list = Listbox(self, yscrollcommand = self._scroll.set, width=35, selectmode=SINGLE)
		self._list.grid(row=0, column=0, columnspan=2, sticky=N+S+W+E)
		
		self._itemValue = StringVar(self)
		self._value = Entry(self, textvariable=self._itemValue)
		self._value.grid(row=1, column=0, sticky=W+E)
		
		self._change = Button(self, text="Set", width=5, command=self.update)
		self._change.grid(row=1, column=1)
		
		self._list.bind("<Double-Button-1>", lambda e:self.getSelected())
		self._value.bind("<Return>", lambda e:self.update() )
		
		self._scroll.config(command = self._list.yview)
		
		self.setItems(items)
		self._updateFunc = updateFunc
		self._key = None
		
	def getSelected(self):
		sel = self._list.curselection()
		if len(sel) == 1:
			self._key = self._list.get(sel[0])
			if self._key in self._items:
				self._itemValue.set(self._items[self._key])
	
	def update(self):
		if self._updateFunc is not None and self._key in self._items:
			val =  self._itemValue.get()
			self._updateFunc(self._key, val)
			self._items[self._key] = val
	
	def setItems(self, items):
		self._items = items
		self._list.delete(0,END)
		keys = []
		for k in self._items:
			keys.append(k)
		keys.sort()
		for k in keys:
			self._list.insert(END, k)


