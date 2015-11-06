package com.bhsc.mobile.dataclass;

public class Data_DB_FeedBack {

	private String mQuestion;
	private String mReply;
	
	private long submitTime;
	private long replyTime;
	private float timeZone;//时区
	
	public Data_DB_FeedBack(){
		mQuestion = "";
		mReply = "";
	}
	
	public String getQuestion(){
		return this.mQuestion;
	}
	
	public void setQuestion(String question){
		this.mQuestion = question;
	}
	
	public String getReply(){
		return this.mReply;
	}
	
	public void setReply(String reply){
		this.mReply = reply;
	}
	
	public long getSubmitTime(){
		return this.submitTime;
	}
	
	public void setSubmitTime(long submitTime){
		this.submitTime = submitTime;
	}
	
	public long getReplyTime(){
		return this.replyTime;
	}
	
	public void setReplyTime(long replyTime){
		this.replyTime = replyTime;
	}
	
	public void setTimezone(float timezone){
		this.timeZone = timezone;
	}
	
	public float getTimezone(){
		return this.timeZone;
	}
}
