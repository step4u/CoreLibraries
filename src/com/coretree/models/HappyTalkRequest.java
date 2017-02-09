package com.coretree.models;

public class HappyTalkRequest {
	public String msgid;
	public String message_type;
	public String message_group_code;
	public String profile_key;
	public String template_code;
	public String receiver_num;
	public String message;
	public String reserved_time;
	public String sms_only;
	public String sms_message;
	public String sms_title;
	public String sms_kind;
	public String sender_num;
	public String parcel_company;
	public String parcel_invoice;
	public String s_code;
	public String btn_name;
	public String btn_url;
	public String image_url;
	public String image_link;
	
	public transient int count = 1;
	
	/*
	public String getMsgid() { return this.msgid; }
	public void setMsgid(String msgid) { this.msgid = msgid; }
	
	public String getMsgType() { return this.message_type; }
	public void setMsgType(String message_type) { this.message_type = message_type; }
	
	public String getMessageGroupCode() { return this.message_group_code; }
	public void setMessageGroupCode(String message_group_code) { this.message_group_code = message_group_code; }
	
	public String getProfileKey() { return this.profile_key; }
	public void setProfileKey(String profile_key) { this.profile_key = profile_key; }
	
	public String getTemplateCode() { return this.template_code; }
	public void setTemplateCode(String template_code) { this.template_code = template_code; }
	
	public String getReceiverNum() { return this.receiver_num; }
	public void setReceiverNum(String receiver_num) { this.receiver_num = receiver_num; }
	
	public String getMessage() { return this.message; }
	public void setMessage(String message) { this.message = message; }
	
	public String getMessage() { return this.message; }
	public void setMessage(String message) { this.message = message; }
	*/
	
	@Override
	public String toString() {
		return "HappyTalkRequest [msgid=" + msgid + ", message_type=" + message_type + ", message_group_code=" + message_group_code
				+ ", profile_key=" + profile_key + ", template_code=" + template_code + ", receiver_num=" + receiver_num
				+ ", message=" + message + ", reserved_time=" + reserved_time + ", sms_only=" + sms_only
				+ ", sms_message=" + sms_message + ", sms_title=" + sms_title + ", sms_kind=" + sms_kind + ", sender_num=" + sender_num
				+ ", parcel_company=" + parcel_company + ", parcel_invoice=" + parcel_invoice + ", s_code=" + s_code
				+ ", btn_name=" + btn_name + ", btn_url=" + btn_url + ", image_url=" + image_url + ", image_link=" + image_link + "]";
	}
}
