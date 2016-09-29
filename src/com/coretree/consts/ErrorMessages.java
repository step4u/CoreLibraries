package com.coretree.consts;

public class ErrorMessages {
	// 100001 ~ 100050 : IO error
	public static final int ERR_FILE_DOES_NOT_EXIST = 100001;
	
	// 100051 ~ 100070 : SQL error
	public static final int ERR_SQL_EXCEPTION = 100051;
	
	// 100071 ~ 100090 : RUNTIME error
	public static final int ERR_RUNTIME_EXCEPTION = 100071;
	
	// 100091 ~ 100100 : Crypto error
	public static final int ERR_CRYPTO_EXCEPTION = 100091;
	
	
	// CDR ฐüทร ERROR
	// 100001 ~ 1000010
	public static final int ERR_CDR_SOCKET = 100001;
	public static final int ERR_CDR_IO = 100002;
	public static final int ERR_CDR_SQL = 100003;
	public static final int ERR_CDR_PARSING = 100004;
}
