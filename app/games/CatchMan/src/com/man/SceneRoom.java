package com.man;

import org.json.JSONArray;
import org.json.JSONException;

import com.man.net.GameClient;
import com.man.net.GameClientListener;
import com.man.plug.AlertLoading;
import com.man.util.GameUtil;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SceneRoom extends Activity {
	
	private String TAG = this.getClass().getSimpleName();
	
	private boolean isReady;
	
	private String userId = null;
	private String roomId = null;
	private String roleId = null;
	private GameClient client = null;
	
	private Button btnStartGame = null;
	private LinearLayout listAllPlayers = null;
	
	private LayoutInflater inflater = null;
	private AlertLoading loadingWindow = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scene_room);
		
		Bundle params = this.getIntent().getExtras();
		userId = params.getString("userId");
		roomId = params.getString("roomId");
		if (userId.equalsIgnoreCase(roomId)) {
			roleId = "0"; // 男人角色
		} else {
			roleId = "1"; // 女人角色
		}
		
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		listAllPlayers = (LinearLayout) this.findViewById(R.id.list_all_players);
		
		// init game client
		client = new GameClient();
		client.setListener(new SceneRoomListener());
		
		// get room users
		if (client.isConnected()) {
			client.getRoomUsers(roomId);
		}
		
		// init loading window
		loadingWindow = new AlertLoading(SceneRoom.this, new AlertLoading.Listener() {
			@Override
			public void onComplete() {
				// change room status to playing
				// status 0:waiting,1:playing
				client.updateRoomStatus("1");
				// enter game together
				client.sendRoomMsg("[0,1]");
				
			}
		});
		
		// start net game
		btnStartGame = (Button) this.findViewById(R.id.btn_start_game);
		btnStartGame.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// when all player ready
				if (!isReady) {
					// toast message
					String msg = "Waiting other players";
					Toast.makeText(SceneRoom.this, msg, Toast.LENGTH_SHORT).show();
				}
				// have myself be ready
				client.updateUserStatus("1");
				// update player list
				client.getRoomUsers(roomId);
			}
		});
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				break;
			case KeyEvent.KEYCODE_BACK:
				quitRoom();
				break;
			default:
				break;
		}
		return false;
	}
	
	public void enterGame() {
		Bundle params = new Bundle();
		params.putString("userId", userId);
		params.putString("roomId", roomId);
		params.putString("roleId", roleId);
		GameUtil.forward(this, SceneNetGame.class, params);
	}
	
	public void quitRoom() {
		client.leaveRoom();
		client.updateUserStatus("0");
		GameUtil.forward(this, SceneHall.class);
	}
	
	private void updatePlayers(JSONArray arguments) {
		JSONArray players = null;
		try {
			// remove all rooms
			listAllPlayers.removeAllViews();
			// add online rooms
			players = new JSONArray((String) arguments.get(0));
			for (int i = 0; i < players.length(); i++) {
				String playerId = players.getString(i);
				String playerName = "User " + playerId;
				// create player
				View playerView = inflater.inflate(R.layout.scene_room_player, null);
				ImageView iv = (ImageView) playerView.findViewById(R.id.scene_room_player_face);
				iv.setImageDrawable(this.getPlayerFace(i));
				TextView tv = (TextView) playerView.findViewById(R.id.scene_room_player_name);
				tv.setText(playerName);
				listAllPlayers.addView(playerView);
			}
			// get & update user status
			client.getUserStatus(players);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void updatePlayerStatus (JSONArray arguments) {
		JSONArray playerStatus = null;
		boolean isWaiting = false;
		try {
			playerStatus = new JSONArray((String) arguments.get(0));
			if (playerStatus != null) {
				int playerCount = listAllPlayers.getChildCount();
				for (int i=0; i<playerCount; i++) {
					View view = listAllPlayers.getChildAt(i);
					TextView tv = (TextView) view.findViewById(R.id.scene_room_player_status);
					String status = playerStatus.getString(i);
					if (status.equalsIgnoreCase("1")) {
						tv.setText("ready");
					} else {
						tv.setText("waiting");
						isWaiting = true;
					}
				}
				if (playerCount < 2) {
					isWaiting = true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			isWaiting = true;
		}
		// update isReady status
		isReady = !isWaiting;
		// auto start together
		if (isReady) {
			loadingWindow.show();
		}
	}
	
	private Drawable getPlayerFace(int id) {
		// role : man
		if (id == 0) {
			return this.getResources().getDrawable(R.drawable.w2);
		}
		// role : catcher
		return this.getResources().getDrawable(R.drawable.w1);
	}
	
	private class SceneRoomListener extends GameClientListener {
		
		@Override
		public void onJoinRoom(String event, JSONArray arguments) {
			Log.w(TAG, "onJoinRoom:" + arguments.toString());
			client.getRoomUsers(roomId);
			
		}
		
		@Override
		public void onLeaveRoom(String event, JSONArray arguments) {
			Log.w(TAG, "onLeaveRoom:" + arguments.toString());
			client.getRoomUsers(roomId);
		}
		
		@Override
		public void onSendRoomMsg(String event, JSONArray arguments) {
			Log.w(TAG, "onSendRoomMsg:" + arguments.toString());
			try {
				JSONArray msg = new JSONArray(arguments.getString(2));
				int action = msg.getInt(0);
				// 操作命令
				if (action == 0) {
					int command = msg.getInt(1);
					// 玩家同时进入游戏
					if (command == 1) {
						enterGame();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onGetRoomUsers(String event, JSONArray arguments) {
			Log.w(TAG, "onGetRoomUsers:" + arguments.toString());
			// update player list
			updatePlayers(arguments);
		}
		
		@Override
		public void onGetUserStatus(String event, JSONArray arguments) {
			Log.w(tag, "onGetUserStatus:" + arguments.toString());
			// update player status
			updatePlayerStatus(arguments);
		}
	}
}