package com.man.net;

import org.json.JSONArray;

import android.util.Log;

public class GameClientListener implements GameClient.Listener {

	protected String tag = this.getClass().getSimpleName();
	
	@Override
	public void onConnect() {
		Log.w(tag, "onConnect:");
	}
	
	@Override
	public void onMessage(String event, JSONArray arguments) {
		Log.w(tag, "onMessage:" + arguments.toString());
	}
	
	@Override
	public void onDisconnect(int code, String reason) {
		Log.w(tag, "onDisconnect:" + code);
	}
	
	@Override
	public void onError(Exception error) {
		Log.w(tag, "onError:" + error.getMessage());
	}
	
	@Override
	public void onLogin(String event, JSONArray arguments) {
		Log.w(tag, "onConnect:" + arguments.toString());
	}
	
	@Override
	public void onJoinRoom(String event, JSONArray arguments) {
		Log.w(tag, "onJoinRoom:" + arguments.toString());
	}
	
	@Override
	public void onLeaveRoom(String event, JSONArray arguments) {
		Log.w(tag, "onLeaveRoom:" + arguments.toString());
	}
	
	@Override
	public void onSendRoomMsg(String event, JSONArray arguments) {
		Log.w(tag, "onSendRoomMsg:" + arguments.toString());
	}
	
	@Override
	public void onUpdateRooms(String event, JSONArray arguments) {
		Log.w(tag, "onUpdateRooms:" + arguments.toString());
	}
	
	@Override
	public void onGetRoomUsers(String event, JSONArray arguments) {
		Log.w(tag, "onGetRoomUsers:" + arguments.toString());
	}
	
	@Override
	public void onUpdateRoomStatus(String event, JSONArray arguments) {
		Log.w(tag, "onUpdateRoomStatus:" + arguments.toString());
	}

	@Override
	public void onGetUserStatus(String event, JSONArray arguments) {
		Log.w(tag, "onGetUserStatus:" + arguments.toString());
	}
}