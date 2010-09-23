/* $Id: Test_Runner_Crash_DivisionByZero.cpp, v 0.1 2008/07/16 11:25:00 alt Exp $ */

/* Copyright (C) 2008 Olexiy Palinkash <olexiy.palinkash@gmail.com> */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */ 

#include <stdio.h>
#include <vector>
#include <math.h>

using namespace std;

int main()
{
	int a = 0, b = 1;
	b /= a;
	return 0;
}

