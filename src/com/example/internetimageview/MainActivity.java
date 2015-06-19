package com.example.internetimageview;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText etImageUrl;
    private ImageView ivImage;
    public static final int SHOWIMAGE=1;
    private Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case SHOWIMAGE:
				Bitmap bitmap=(Bitmap) msg.obj;
				ivImage.setImageBitmap(bitmap);
				break;

			default:
				break;
			}
    	};
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
	   etImageUrl=(EditText) findViewById(R.id.etImageUrl);
	   ivImage=(ImageView) findViewById(R.id.ivImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	 //1:初始程序：
	/*public void viewImage(View view)
	{
		String path=etImageUrl.getText().toString();//把图片路径转换成字符串
		if(TextUtils.isEmpty(path))
		{
			/*
			 * question:对于一个UI界面中，当判断用户是否输入用户名或密码时，我们常用TextUtils.isEmpty()方法来判断；但有时也可以用这个equals()方法，都可以来判断EditText中是否为空，但有时很纠结，不知道这两种方法中哪个比较好？为什么？
			   answer:仔细读官方的API：  Returns true if the string is null or 0-length.  因为你从EditText返回的是一个变量。如果这个变量本身为null值，那么你掉它的equals方法是要报错的。但是如果你调用TextUtils.isEmpty() 把这个变量作为参数传进去。
			       只要这个参数为空或者为""，都会返回真。所以，用官方给的更加严谨。
			      而且,也十分方便。因为你单独去判断你还不是要写一个if语句判断。返回的还是一个boolean值
			
			Toast.makeText(this, R.string.NOnull, Toast.LENGTH_LONG).show();//如果输入路径为空，就弹出Toast
		}else{
			//不为空，连接服务器，请求获得图片
			try{
			URL url=new URL(path);
			//发出http请求
			HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");//设置提交方式
			//设置连接超时时间
			httpURLConnection.setConnectTimeout(5000);//这时，我们设置为超时时间为5秒，如果5秒内不能连接就被认为是有错误发生.
			int responsecode=httpURLConnection.getResponseCode();
			if(responsecode==200){
				InputStream inputstream=httpURLConnection.getInputStream();
				Bitmap bitmap=BitmapFactory.decodeStream(inputstream);
				ivImage.setImageBitmap(bitmap);
			}else{
				Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
			}
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException E){
			E.printStackTrace();
		}
		
	}
	*/
	public void viewImage(View view){
		final String imageUrl=etImageUrl.getText().toString();
		if(TextUtils.isEmpty(imageUrl)){
			Toast.makeText(this, "图片路径不能为空", Toast.LENGTH_LONG).show();
		}else{
			new Thread(){
				
					public void run() {
						try {
							URL url=new URL(imageUrl);
							HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
						    httpURLConnection.setRequestMethod("GET");
						    httpURLConnection.setConnectTimeout(5000);
						    int responseCode=httpURLConnection.getResponseCode();
						    if(responseCode==200){
						    	InputStream inputStream=httpURLConnection.getInputStream();
						    	Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
						    	Message message=new Message();
						    	message.what=SHOWIMAGE;
						    	message.obj=bitmap;
						    	//ivImage.setImageBitmap(bitmap);
						    	handler.sendMessage(message);
						    }else{
						    	Toast.makeText(MainActivity.this, "显示图片失败", Toast.LENGTH_LONG).show();
						    }
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					
				}
			}.start();
			
		}
	}

}

