package com.man;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.man.net.GameClient;
import com.man.net.GameClientListener;
import com.man.util.GameUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SceneHall extends Activity {
	
	private String TAG = this.getClass().getSimpleName();
	
	private GameClient client = null;
	
	private Button btnCreateRoom = null;
	private TextView textUsername = null;
	private LinearLayout listAllRooms = null;
	
	private LayoutInflater inflater = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scene_hall);
		
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		textUsername = (TextView) this.findViewById(R.id.scene_hall_username);
		listAllRooms = (LinearLayout) this.findViewById(R.id.list_all_rooms);
		
		// init game client
		client = new GameClient();
		client.setListener(new SceneHallListener());
		
		// update room list
		if (client.isConnected()) {
			textUsername.setText("Welcome, User " + client.getClientId());
			client.updateRooms();
			client.updateRoomStatus();
		} else {
			textUsername.setText("Connecting ..."); // set user name after connected
		}
		
		// create host
		btnCreateRoom = (Button) this.findViewById(R.id.btn_create_room);
		btnCreateRoom.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (client.isConnected()) {
					String roomId = client.getClientId();
					client.joinRoom(roomId);
				} else {
					String msg = "Please login first";
					Toast.makeText(SceneHall.this, msg, Toast.LENGTH_SHORT).show();
				}
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
				quitHall();
				break;
			default:
				break;
		}
		return false;
	}
	
	public void enterRoom(String roomId) {
		Bundle params = new Bundle();
		params.putString("userId", client.getClientId());
		params.putString("roomId", roomId);
		GameUtil.forward(this, SceneRoom.class, params);
	}
	
	public void quitHall() {
//		try {
//			Log.w(TAG, "client disconnect");
//			client.disconnect();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		GameUtil.forward(this, SceneMenu.class);
	}
	

	
	@SuppressWarnings("unchecked")
	private void updateRoomList(JSONArray arguments) {
		JSONObject rooms = null;
		try {
			// remove all rooms
			listAllRooms.removeAllViews();
			// add online rooms
			rooms = new JSONObject((String) arguments.get(0));
			Iterator<String> it = rooms.keys();
			while (it.hasNext()) {
				String roomId = it.next();
				JSONArray users = rooms.optJSONArray(roomId);
				String roomText = "Room " + roomId + " (" + users.length() + "/2)";
				// create room
				View roomView = inflater.inflate(R.layout.scene_hall_room, null);
				TextView tv = (TextView) roomView.findViewById(R.id.scene_hall_room_name);
				tv.setText(roomText);
				tv.setTag(roomId);
				// stop join when room full
				if (users.length() < 2) {
					tv.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							String roomId = v.getTag().toString();
							client.joinRoom(roomId);
							enterRoom(roomId);
						}
					});
				}
				listAllRooms.addView(roomView);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void updateRoomStatus (JSONArray arguments) {
		JSONObject roomStatus = null;
		try {
			roomStatus = new JSONObject((String) arguments.get(0));
			if (roomStatus != null) {
				int roomCount = listAllRooms.getChildCount();
				for (int i=0; i<roomCount; i++) {
					View view = listAllRooms.getChildAt(i);
					TextView tv1 = (TextView) view.findViewById(R.id.scene_hall_room_name);
					String roomId = tv1.getTag().toString();
					TextView tv2 = (TextView) view.findViewById(R.id.scene_hall_room_status);
					String status = roomStatus.getString(roomId);
					if (status.equalsIgnoreCase("0")) {
						tv2.setText("waiting");
					} else if (status.equalsIgnoreCase("1")) {
						tv2.setText("playing");
						tv1.setOnClickListener(null); // prevent click
					} else {
						tv2.setText("--");
						tv1.setOnClickListener(null); // prevent click
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private class SceneHallListener extends GameClientListener {
		
		@Override
		public void onConnect() {
			Log.w(TAG, "onConnect:" + client.getClientId());
			client.login(client.getClientId());
			client.updateRooms();
			client.updateRoomStatus();
		}
		
		@Override
		public void onLogin(String event, JSONArray arguments) {
			Log.w(TAG, "onLogin:" + arguments.toString());
			textUsername.setText("Welcome, User " + client.getClientId()); // set default user name
		}
		
		@Override
		public void onJoinRoom(String event, JSONArray arguments) {
			Log.w(TAG, "onJoinRoom:" + arguments.toString());
			client.updateRooms();
			client.updateRoomStatus();
			String userId = null;
			String roomId = null;
			try {
				userId = arguments.getString(0);
				roomId = arguments.getString(1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// create and enter the room
			if (userId != null && roomId != null) {
				if (roomId.equalsIgnoreCase(client.getClientId())) {
					enterRoom(roomId);
				}
			}
		}
		
		@Override
		public void onUpdateRooms(String event, JSONArray arguments) {
			Log.w(TAG, "onUpdateRooms:" + arguments.toString());
			// update room list
			updateRoomList(arguments);
		}
		
		@Override
		public void onUpdateRoomStatus(String event, JSONArray arguments) {
			Log.w(TAG, "onUpdateRooms:" + arguments.toString());
			// update room list status
			updateRoomStatus(arguments);
		}
	}
}