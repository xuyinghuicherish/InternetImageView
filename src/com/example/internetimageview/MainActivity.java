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

	 //1:��ʼ����
	/*public void viewImage(View view)
	{
		String path=etImageUrl.getText().toString();//��ͼƬ·��ת�����ַ���
		if(TextUtils.isEmpty(path))
		{
			/*
			 * question:����һ��UI�����У����ж��û��Ƿ������û���������ʱ�����ǳ���TextUtils.isEmpty()�������жϣ�����ʱҲ���������equals()���������������ж�EditText���Ƿ�Ϊ�գ�����ʱ�ܾ��ᣬ��֪�������ַ������ĸ��ȽϺã�Ϊʲô��
			   answer:��ϸ���ٷ���API��  Returns true if the string is null or 0-length.  ��Ϊ���EditText���ص���һ����������������������Ϊnullֵ����ô�������equals������Ҫ����ġ�������������TextUtils.isEmpty() �����������Ϊ��������ȥ��
			       ֻҪ�������Ϊ�ջ���Ϊ""�����᷵���档���ԣ��ùٷ����ĸ����Ͻ���
			      ����,Ҳʮ�ַ��㡣��Ϊ�㵥��ȥ�ж��㻹����Ҫдһ��if����жϡ����صĻ���һ��booleanֵ
			
			Toast.makeText(this, R.string.NOnull, Toast.LENGTH_LONG).show();//�������·��Ϊ�գ��͵���Toast
		}else{
			//��Ϊ�գ����ӷ�������������ͼƬ
			try{
			URL url=new URL(path);
			//����http����
			HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");//�����ύ��ʽ
			//�������ӳ�ʱʱ��
			httpURLConnection.setConnectTimeout(5000);//��ʱ����������Ϊ��ʱʱ��Ϊ5�룬���5���ڲ������Ӿͱ���Ϊ���д�����.
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
			Toast.makeText(this, "ͼƬ·������Ϊ��", Toast.LENGTH_LONG).show();
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
						    	Toast.makeText(MainActivity.this, "��ʾͼƬʧ��", Toast.LENGTH_LONG).show();
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

