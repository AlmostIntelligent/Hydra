#!/usr/bin/env python

import sys
import time

line = "Welcome to echo\n"
while True:
	sys.stdout.write(line)
	sys.stdout.flush()
	char = ""
	line = ""
	while char != "\n":
		char = sys.stdin.read(1)
		line += char
	if line.startswith("exit"):
		print "bye bye"
		break
	time.sleep(1);
	