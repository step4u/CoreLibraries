package com.coretree.util;

public class Const4pbx
{
	// for UC //
	public static final byte UC_REGISTER_REQ = 60;
	public static final byte UC_REGISTER_RES = 61;
	public static final byte UC_UNREGISTER_REQ = 62;
	public static final byte UC_UNREGISTER_RES = 63;
	public static final byte UC_BUSY_EXT_REQ = 64;
	public static final byte UC_BUSY_EXT_RES = 65;
	public static final byte UC_INFO_SRV_REQ = 66;
	public static final byte UC_INFO_SRV_RES = 67;
	public static final byte UC_SET_SRV_REQ = 68;
	public static final byte UC_SET_SRV_RES = 69;
	public static final byte UC_CLEAR_SRV_REQ = 70;
	public static final byte UC_CLEAR_SRV_RES = 71;
	public static final byte UC_CALL_STATE_REQ = 72;
	public static final byte UC_CALL_STATE_RES = 73;
	public static final byte UC_MAKE_CALL_REQ = 74;
	public static final byte UC_MAKE_CALL_RES = 75;
	public static final byte UC_DROP_CALL_REQ = 76;
	public static final byte UC_DROP_CALL_RES = 77;
	public static final byte UC_ANSWER_CALL_REQ = 78;
	public static final byte UC_ANSWER_CALL_RES = 79;
	public static final byte UC_PICKUP_CALL_REQ = 80;
	public static final byte UC_PICKUP_CALL_RES = 81;
	public static final byte UC_HOLD_CALL_REQ = 82;
	public static final byte UC_HOLD_CALL_RES = 83;
	public static final byte UC_ACTIVE_CALL_REQ = 84;
	public static final byte UC_ACTIVE_CALL_RES = 85;
	public static final byte UC_TRANSFER_CALL_REQ = 86;
	public static final byte UC_TRANSFER_CALL_RES = 87;
	public static final byte UC_PICKUP_TRANSFER_REQ = 88;
	public static final byte UC_PICKUP_TRANSFER_RES = 89;
	public static final byte UC_SEND_INPUT_DATA_REQ = 90;
	public static final byte UC_SEND_INPUT_DATA_RES = 91;
	public static final byte UC_INPUT_DATA_RESULT_REQ = 92;
	public static final byte UC_INPUT_DATA_RESULT_RES = 93;
	public static final byte UC_MANAGER_EXT_REQ = 94;
	public static final byte UC_MANAGER_EXT_RES = 95;
	public static final byte UC_CALLCENTER_GROUP_REQ = 96;
	public static final byte UC_CALLCENTER_GROUP_RES = 97;
	public static final byte UC_LISTEN_TO_CALL_REQ = 98;
	public static final byte UC_LISTEN_TO_CALL_RES = 99;
	
	public static final byte UC_CLEAR_EXT_STATE_REQ = 100;
	public static final byte UC_CLEAR_EXT_STATE_RES = 101;
	public static final byte UC_REPORT_EXT_STATE = 102;
	public static final byte UC_REPORT_SRV_STATE = 103;
	public static final byte UC_REPORT_WAITING_COUNT = 104;
	
	public static final byte UC_SMS_SEND_REQ = 120;
	public static final byte UC_SMS_SEND_RES = 121;
	public static final byte UC_SMS_INFO_REQ = 122;
	public static final byte UC_SMS_INFO_RES = 123;
	public static final byte UC_SMS_RECV_REQ = 124;
	public static final byte UC_SMS_RECV_RES = 125;
	public static final byte UC_SMS_RESERV_SEND_REQ = 126;
	public static final byte UC_SMS_RESERV_SEND_RES = 127;
	public static final byte UC_SMS_RESERV_CANCEL_REQ = (byte)128;
	public static final byte UC_SMS_RESERV_CANCEL_RES = (byte)129;
	public static final byte UC_SMS_TRANSFER_REQ = (byte)130;
	public static final byte UC_SMS_TRANSFER_RES = (byte)131;
	public static final byte UC_SMS_TRANSFER_CANCEL_REQ = (byte)132;
	public static final byte UC_SMS_TRANSFER_CANCEL_RES = (byte)133;
	
	
	public static final byte UC_APP_AUTH_REQ = (byte)198;
	public static final byte UC_APP_AUTH_RES = (byte)199;

	// DIRECT
	public static final byte UC_DIRECT_NONE = 10;
	public static final byte UC_DIRECT_OUTGOING = 11;
	public static final byte UC_DIRECT_INCOMING = 12;
	
	// TYPE
	public static final byte UC_TYPE_COUPLEPHONE = 11;
	public static final byte UC_TYPE_GROUPWARE = 12;
	public static final byte UC_TYPE_IPPHONE = 13;
	
	// STATUS
	public static final byte UC_STATUS_SUCCESS = 100;
	public static final byte UC_STATUS_FAIL = 101;
	public static final byte UC_CALL_STATE_UNREG = 110;
	public static final byte UC_CALL_STATE_IDLE = 111;
	public static final byte UC_CALL_STATE_INVITING = 112;
	public static final byte UC_CALL_STATE_RINGING = 113;
	public static final byte UC_CALL_STATE_BUSY = 114;
	public static final byte UC_CALL_STATE_NOT_FOUND = 115;
	public static final byte UC_CALL_STATE_CALLER_BUSY = 116;
	public static final byte UC_CALL_STATE_CALLER_NOANSWER = 117;
	public static final byte UC_CALL_STATE_CALLER_MOVE = 118;
	public static final byte UC_CALL_STATE_RINGBACK = 119;
	public static final byte UC_CALL_STATE_CALLEE_BUSY = 120;
	public static final byte UC_CALL_STATE_CALLEE_NOANSER = 121;
	public static final byte UC_CALL_STATE_CALLEE_MOVE = 122;
	
	// ResponseCode
	public static final int UC_SRV_UNCONDITIONAL = 1;
	public static final int UC_SRV_NOANSWER = 2;
	public static final int UC_SRV_BUSY = 3;
	public static final int UC_SRV_DND = 4;
	
	// DND
	public static final byte UC_DND_SET = 1;
	public static final byte UC_DND_CLEAR = 0;
	
	// etc command
	public static final int  WS_REQ_EXTENSION_STATE = 10001;
	public static final int  WS_RES_EXTENSION_STATE = 10002;
	public static final int  WS_REQ_SET_EXTENSION_STATE = 10003;
	public static final int  WS_RES_SET_EXTENSION_STATE = 10004;
	public static final int  WS_REQ_RELOAD_USER = 10005;
	public static final int  WS_RES_RELOAD_USER = 10006;
	
	// etc state
	/*
	public static final int WS_VALUE_EXTENSION_STATE_ONLINE = 1001;				// 대기 (온라인)
	public static final int WS_VALUE_EXTENSION_STATE_LEFT = 1002;				// 후처리
	public static final int WS_VALUE_EXTENSION_STATE_DND = 1003;				// 이석
	public static final int WS_VALUE_EXTENSION_STATE_REDIRECTED = 1004;			// 휴식
	public static final int WS_VALUE_EXTENSION_STATE_REDIRECTED = 1005;			// 교육
	*/
	
	
	public static final int WS_REQ_CHANGE_EXTENSION_STATE = 1001;				// 상태 변경 명령
	public static final int WS_RES_CHANGE_EXTENSION_STATE = 1002;				// 상태 변경 명령 결과
	
	public static final int WS_VALUE_EXTENSION_STATE_READY = 1011;				// 대기 (온라인)
	public static final int WS_VALUE_EXTENSION_STATE_AFTER = 1012;				// 후처리 (자리비움)
	public static final int WS_VALUE_EXTENSION_STATE_LEFT = 1013;				// 이석 (자리비움)
	public static final int WS_VALUE_EXTENSION_STATE_REST = 1014;				// 휴식 (자리비움)
	public static final int WS_VALUE_EXTENSION_STATE_EDU = 1015;				// 교육 (자리비움)
	public static final int WS_VALUE_EXTENSION_STATE_BUSY = 1016;				// 통화중
	public static final int WS_VALUE_EXTENSION_STATE_LOGEDOUT = 1017;			// 로그아웃
	public static final int WS_VALUE_EXTENSION_STATE_LOGEDON = 1018;			// 로그온
	public static final int WS_VALUE_EXTENSION_STATE_SAMEASNOW = 1019;			// 같은 상태 요청함
	public static final int WS_VALUE_EXTENSION_STATE_WRONGREQ = 1020;			// 잘못 된 요청
	
	// uc websocket message
	public static final int WS_STATUS_ING_NOTFOUND = 210001;
	public static final int WS_STATUS_ING_UNSUPPORTED = 210002;
	// public static final int WS_STATUS_ING_NOTFOUND = 210002;
	
	
	// for CDR //
	public static final int CDR_SAVE_REQ = 1;										// CDR 저장 요청
	public static final int CDR_SAVE_RES = 2;										// CDR 저장 답변
	public static final int CDR_GET_LIST_REQ = 6;									// CDR 전송 요청
	public static final int CDR_GET_LIST_RES = 7;									// CDR 결과
	public static final int CDR_RESULT_SUCCESS = 0;									// CDR 전송 상태 success
}
