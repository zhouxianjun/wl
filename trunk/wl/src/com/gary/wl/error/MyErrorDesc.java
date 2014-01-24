package com.gary.wl.error;

import com.gary.framework.exception.error.ErrorDesc;

public abstract interface MyErrorDesc extends ErrorDesc {
	final String REPEATED_INSERT = "重复的数据";
	final String NO_LOGIN = "请先登录后再操作.";
}
