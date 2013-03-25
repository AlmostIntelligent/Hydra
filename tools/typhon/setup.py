#!/usr/bin/env python

import os
from setuptools import setup


def read(fname):
    return open(os.path.join(os.path.dirname(__file__), fname)).read()

setup(
	name='typhon',
	version='1.0',
	description='Hydra CLI wrapper',
	author='Hanno Sternberg',
	author_email='hanno@almostintelligent.de',
	url='',
	packages=['components'],
	py_modules=['hydra','typhon','__main__'],
	license=read('LICENSE'),
	long_description=read('README.md'),
	entry_points={
		'setuptools.installation':[
			"eggsecutable = typhon:main"
		]
	},
	zip_safe=True,
)