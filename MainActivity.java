package com.bharattrade;
import android.app.*; import android.os.*; import android.widget.*; import java.io.*; import java.net.*; import java.util.concurrent.*;
public class MainActivity extends Activity {
 TextView status,signal,details; EditText symbol; ExecutorService pool=Executors.newSingleThreadExecutor();
 public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_main);
 status=findViewById(R.id.status);signal=findViewById(R.id.signal);details=findViewById(R.id.details);symbol=findViewById(R.id.symbol);
 findViewById(R.id.refresh).setOnClickListener(v->fetch());
 findViewById(R.id.kill).setOnClickListener(v->{signal.setText("KILL SWITCH: ENABLED");details.setText("Paper/live execution blocked.");});
 }
 void fetch(){String s=symbol.getText().toString().trim();if(s.isEmpty())s="NIFTY";final String u="http://10.0.2.2:8000/signal/"+s;
 status.setText("REQUESTING...");
 pool.submit(()->{try{HttpURLConnection c=(HttpURLConnection)new URL(u).openConnection();c.setConnectTimeout(5000);c.setReadTimeout(7000);
 BufferedReader r=new BufferedReader(new InputStreamReader(c.getInputStream()));StringBuilder x=new StringBuilder();String l;while((l=r.readLine())!=null)x.append(l);
 String j=x.toString();runOnUiThread(()->{status.setText("PAPER MODE");signal.setText(j.contains(""action":"BUY"")?"SIGNAL: BUY":j.contains(""action":"SELL"")?"SIGNAL: SELL":"SIGNAL: NO TRADE");details.setText(j);});
 }catch(Exception e){runOnUiThread(()->{status.setText("BACKEND OFFLINE");details.setText("Start the v4 backend and configure its address.");});}});
 }
}
