/* $Id$ */

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

package djudge.judge.validator;

public interface ValidatorLimits
{
	final int MAX_VALIDATOR_RUNNING_TIME = 60000;
	final int MAX_VALIDATOR_CONSUMED_MEMORY = 1024 * 1024 * 256;
	final int MAX_VALIDATOR_OUTPUT_SIZE = 1024 * 1024;
}
