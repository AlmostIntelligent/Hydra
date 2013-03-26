#!/usr/bin/env python

# Text with scroll bar

from Tkinter import *
from ttk import *

class ScrollText(Frame):

	def __init__(self, parent):
		Frame.__init__(self, parent)
		
		self._scroll = Scrollbar(self, orient=VERTICAL)
		self._scroll.pack(side = RIGHT, fill = Y)
		
		self._text = Text(self, yscrollcommand = self._scroll.set, state=DISABLED)
		self._text.pack(side = LEFT, fill = BOTH, expand = 1)
		
		self._scroll.config(command = self._text.yview)
		
		
	def get(self):
		return self._text.get(1.0, END)
		
	def tagEdit(self, tag, **kwargs):
		self._text.tag_config(tag, **kwargs)
		
	def append(self, text, tag = None):
		self._text.config(state=NORMAL)
		self._text.insert(END, text+"\n", tag)
		self._text.yview(END)
		self._text.config(state=DISABLED)
			
	