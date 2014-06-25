package com.man.net;

import java.io.IOException;
import java.net.URI;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GameClient {

	private String TAG = this.getClass().getSimpleName();
	
	public static interface Listener {
		public void onConnect();
		public void onMessage(String event, JSONArray arguments);
		public void onDisconnect(int code, String reason);
		public void onError(Exception error);
		public void onLogin(String event, JSONArray arguments);
		public void onJoinRoom(String event, JSONArray arguments);
		public void onLeaveRoom(String event, JSONArray arguments);
		public void onSendRoomMsg(String event, JSONArray arguments);
		public void onUpdateRooms(String event, JSONArray arguments);
		public void onGetRoomUsers(String event, JSONArray arguments);
		public void onUpdateRoomStatus(String event, JSONArray arguments);
		public void onGetUserStatus(String event, JSONArray arguments);
	}
	
	private static String clientId = null;
	private static SocketIOClient client = null;
	private static volatile boolean isConnected = false;
	
	private GameClientHandler mHandler;
	private static Listener mListener;
	private String mServer;
	
//	private String defaultServer = "http://10.240.53.23:8080";
	private String defaultServer = "http://115.182.82.189";
	
	public GameClient() {
		mServer = defaultServer;
		this.connect();
	}
	
	public GameClient(String server) {
		mServer = server;
		this.connect();
	}
	
	public void setListener(Listener listener) {
		mHandler = new GameClientHandler();
		mListener = listener;
	}
	
	public void removeListener() {
		mHandler = null;
		mListener = null;
	}
	
	public String getClientId() {
		return clientId;
	}
	
	private void setClientId(int n) {
		String rnd = "1";
		for (int i=0; i<n; i++) {
			rnd += (int) Math.floor(Math.random()*10);
		}
		clientId = rnd;
	}
	
	private void handle (int what, Bundle data) {
		if (mListener != null && mHandler != null) {
			Message m = new Message();
			m.what = what;
			m.setData(data);
			mHandler.sendMessage(m);
		}
	}
	
	private void connect() {
		// connect or reconnect 
		if (client == null || !isConnected()) {
			Log.w(TAG, "connecting ...");
			client = new SocketIOClient(URI.create(mServer), new SocketIOHandler());
			client.connect();
			setClientId(4);
		// do after connect
		} else {
			client.setHandler(new SocketIOHandler());
		}
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public void disconnect(){
		// remove client
		if (client != null && isConnected()) {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			client = null;
		}
		// avoid memory leak
		this.removeListener();
	}
	
	public void login(String id) {
		if (client != null && isConnected()) {
			try {
				client.emit("login", id);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void joinRoom(String room) {
		if (client != null && isConnected()) {
			try {
				client.emit("joinRoom", room);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void leaveRoom() {
		if (client != null && isConnected()) {
			try {
				client.emit("leaveRoom");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendRoomMsg(String msg) {
		if (client != null && isConnected()) {
			try {
				client.emit("sendRoomMsg", msg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateRooms() {
		if (client != null && isConnected()) {
			try {
				client.emit("updateRooms");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	// get room status
	public void updateRoomStatus() {
		if (client != null && isConnected()) {
			try {
				client.emit("updateRoomStatus");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	// set and get room status
	public void updateRoomStatus(String status) {
		if (client != null && isConnected()) {
			try {
				client.emit("updateRoomStatus", status);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getRoomUsers(String roomId) {
		if (client != null && isConnected()) {
			try {
				client.emit("getRoomUsers", roomId);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateUserStatus(String status) {
		if (client != null && isConnected()) {
			try {
				client.emit("updateUserStatus", status);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getUserStatus(JSONArray userIds) {
		if (client != null && isConnected()) {
			try {
				client.emit("getUserStatus", userIds.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void emit(String event, String ...args) {
		if (client != null && isConnected()) {
			try {
				client.emit(event, args);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class GameClientTask {
		final public static int TASK_CONNECT = 1;
		final public static int TASK_MESSAGE = 2;
		final public static int TASK_DISCONNECT = 3;
		final public static int TASK_ERROR = 4;
		final public static int TASK_LOGIN = 5;
		final public static int TASK_JOINROOM = 6;
		final public static int TASK_LEAVEROOM = 7;
		final public static int TASK_SENDROOMMSG = 8;
		final public static int TASK_UPDATEROOMS = 9;
		final public static int TASK_GETROOMUSERS = 10;
		final public static int TASK_UPDATEROOMSTATUS = 11;
		final public static int TASK_GETUSERSTATUS = 12;
	}
	
	private class SocketIOHandler implements SocketIOClient.Handler{
		@Override
		public void onConnect() {
			isConnected = true; // set before all
			handle(GameClientTask.TASK_CONNECT, null);
		}
		@Override
		public void on(String event, JSONArray args) {
			Bundle data = new Bundle();
			data.putString("event", event);
			data.putString("args", args.toString());
			if ("message".equals(event)) {
				handle(GameClientTask.TASK_MESSAGE, data);
			}
			if ("login".equals(event)) {
				handle(GameClientTask.TASK_LOGIN, data);
			}
			if ("joinRoom".equals(event)) {
				handle(GameClientTask.TASK_JOINROOM, data);
			}
			if ("leaveRoom".equals(event)) {
				handle(GameClientTask.TASK_LEAVEROOM, data);
			}
			if ("sendRoomMsg".equals(event)) {
				handle(GameClientTask.TASK_SENDROOMMSG, data);
			}
			if ("updateRooms".equals(event)) {
				handle(GameClientTask.TASK_UPDATEROOMS, data);
			}
			if ("getRoomUsers".equals(event)) {
				handle(GameClientTask.TASK_GETROOMUSERS, data);
			}
			if ("updateRoomStatus".equals(event)) {
				handle(GameClientTask.TASK_UPDATEROOMSTATUS, data);
			}
			if ("updateRoomStatus".equals(event)) {
				handle(GameClientTask.TASK_UPDATEROOMSTATUS, data);
			}
			if ("getUserStatus".equals(event)) {
				handle(GameClientTask.TASK_GETUSERSTATUS, data);
			}
		}
		@Override
		public void onDisconnect(int code, String reason) {
			disconnect(); // stop all
			Bundle data = new Bundle();
			data.putInt("code", code);
			data.putString("reason", reason);
			handle(GameClientTask.TASK_DISCONNECT, data);
			isConnected = false;
		}
		@Override
		public void onError(Exception error) {
			disconnect(); // stop all
			Bundle data = new Bundle();
			data.putString("error", error.getMessage());
			handle(GameClientTask.TASK_ERROR, data);
			isConnected = false;
		}
	}
	
	private static class GameClientHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			try {
				Bundle data = msg.getData();
				switch (msg.what) {
					case GameClientTask.TASK_CONNECT:
						mListener.onConnect();
						break;
					case GameClientTask.TASK_MESSAGE:
						mListener.onMessage(
								data.getString("event"), 
								new JSONArray(data.getString("args")));
						break;
					case GameClientTask.TASK_DISCONNECT:
						mListener.onDisconnect(
								data.getInt("code"), 
								data.getString("reason"));
						break;
					case GameClientTask.TASK_ERROR:
						mListener.onError(new Exception(
								data.getString("error")));
						break;
					case GameClientTask.TASK_LOGIN:
						mListener.onLogin(
								data.getString("event"), 
								new JSONArray(data.getString("args")));
						break;
					case GameClientTask.TASK_JOINROOM:
						mListener.onJoinRoom(
								data.getString("event"), 
								new JSONArray(data.getString("args")));
						break;
					case GameClientTask.TASK_LEAVEROOM:
						mListener.onLeaveRoom(
								data.getString("event"), 
								new JSONArray(data.getString("args")));
						break;
					case GameClientTask.TASK_SENDROOMMSG:
						mListener.onSendRoomMsg(
								data.getString("event"), 
								new JSONArray(data.getString("args")));
						break;
					case GameClientTask.TASK_UPDATEROOMS:
						mListener.onUpdateRooms(
								data.getString("event"), 
								new JSONArray(data.getString("args")));
						break;
					case GameClientTask.TASK_GETROOMUSERS:
						mListener.onGetRoomUsers(
								data.getString("event"), 
								new JSONArray(data.getString("args")));
						break;
					case GameClientTask.TASK_UPDATEROOMSTATUS:
						mListener.onUpdateRoomStatus(
								data.getString("event"), 
								new JSONArray(data.getString("args")));
						break;
					case GameClientTask.TASK_GETUSERSTATUS:
						mListener.onGetUserStatus(
								data.getString("event"), 
								new JSONArray(data.getString("args")));
						break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}