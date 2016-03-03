package win.lihy.mysocketclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final String IP = "59.66.131.156";
	static final int PORT = 1234;

	EditText editText;
	Button btn,btnshow;
	TextView textView;
	Socket socket;
	BufferedReader bReader;
	BufferedWriter bWriter;
	
	String yinbiaoString = "音标： ";
	String shiyiString = "释义： ";
	String liju = "例句： ";
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editText = (EditText) findViewById(R.id.et);
		btn = (Button) findViewById(R.id.btSend);
		textView = (TextView) findViewById(R.id.tv);

		connect();

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				send();
				Toast.makeText(MainActivity.this, "已发",
						Toast.LENGTH_LONG).show();
			}
		});
		


	}
	public void connect() {
		AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				
				try {
					socket = new Socket(IP, PORT);
					socket.setTcpNoDelay(true);
					bReader = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					bWriter = new BufferedWriter(new OutputStreamWriter(
							socket.getOutputStream()));
					publishProgress("@success");
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					Toast.makeText(MainActivity.this, "无法建立连接",
							Toast.LENGTH_LONG).show();
					e1.printStackTrace();
				} catch (IOException e1) {
					Toast.makeText(MainActivity.this, "无法建立连接",
							Toast.LENGTH_LONG).show();
					e1.printStackTrace();
				}
				
				try {
					String line = "";
//					while ((line = bReader.readLine()) != null) {
//						
//						publishProgress(line);
//					}
					int h;
					int first = 0,cnt = 0;
					while ((h = bReader.read()) > 0) {
						char ch = (char)h;
						line += (char)h;
						if(ch == '('){
							cnt++;
							first = 1;
						}
						else if(ch == ')'){
							cnt--;
						}
						if(first == 1 && cnt == 0){
							publishProgress(line);
							first = 0;cnt = 0;
							line = "";
						}
						
					}
					

					
					
					
				} catch (IOException e) {
					Toast.makeText(MainActivity.this, "无法shou",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				


						
	
				return null;
			}
			

			@Override
			protected void onProgressUpdate(String... values) {
				Log.d("shitou", values[0]);
				if(values[0].equals("@success")){
					Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_LONG).show();
				}
				
				textView.setText("收到如下信息：\n" + values[0]+"\n\n");
				super.onProgressUpdate(values);
			}

		};

		read.execute();
	}

	public void send() {
		try {
			//bWriter.write(editText.getText().toString() + "\n");
			//textView.append(searchWord(editText.getText().toString()) + "\n");
			bWriter.write(searchWord(editText.getText().toString()) + "\n");
			bWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String searchWord(String word){
		String tmp = "BIBI_search(" + word + ")";
		return tmp;
	}
}
