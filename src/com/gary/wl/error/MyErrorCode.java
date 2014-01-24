package com.gary.wl.error;

import com.gary.framework.exception.error.ErrorCode;

public abstract interface MyErrorCode extends ErrorCode {
	final int REPEATED_INSERT = 11;
	final int NO_LOGIN = 405;
}
