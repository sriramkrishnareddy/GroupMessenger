package edu.buffalo.cse.cse486586.groupmessenger1;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 *
 * @author sriram
 *
 */
public class GroupMessengerActivity extends Activity{
    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    static final int SERVER_PORT = 10000;
    Socket sock;
    InputStreamReader in;
    BufferedReader br;
    String mess = "";
    PrintWriter pw;
    Socket clientsocket;
    SQLiteDatabase db;
    databaseclass dbc;
    SimpleCursorAdapter adapter;
    int msgno=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);
        dbc = new databaseclass(this);
        db = dbc.getWritableDatabase();

        final ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);


        } catch (IOException e) {
            Log.e(TAG, "Can't create ServerSocket");
            System.out.println(e);
            return;
        }
        /*
         * TODO: Use the TextView to display your messages. Though there is no grading component
         * on how you display the messages, if you implement it, it'll make your debugging easier.
         */
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
        final EditText editText = (EditText) findViewById(R.id.editText1);
        /*
         * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));

        Button send = (Button)findViewById(R.id.button4);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg =editText.getText().toString() + "\n";
                editText.setText("");

                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, "11108","11112","11116","11120","11124");
            }

        });



        /*
         * TODO: You need to register and implement an OnClickListener for the "Send" button.
         * In your implementation you need to get the message from the input box (EditText)
         * and send it to other AVDs.
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }

    public class ServerTask extends AsyncTask<ServerSocket, String, Void> {
        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            final ServerSocket serverSocket = sockets[0];
            try {
                while(true) {
                sock= serverSocket.accept();
                in= new InputStreamReader(sock.getInputStream());
                br = new BufferedReader(in);


                    mess = br.readLine();
                    publishProgress(mess);

                    if(mess.equals("quit")){
                        break;
                    }
                }
                //System.out.println("While loop exited");



                //in.close();
                //sock.close();
            }
            catch (IOException ex) {
                System.out.println(ex);
            }

            return null;
        }

        protected void onProgressUpdate(String...strings) {

            String strReceived = strings[0].trim();
            ContentValues cv = new ContentValues();
            cv.put(databaseclass.key,msgno);
            cv.put(databaseclass.value,strReceived);
            getContentResolver().insert(GroupMessengerProvider.GMP_URI,cv);
            TextView tv = (TextView) findViewById(R.id.textView1);
            tv.append(strReceived+"\n");
            msgno++;
        }
    }


    public class ClientTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... msgs) {
            String msgToSend = msgs[0];
            try {


                String remotePort;
                for(int j=1;j<6;j++) {
                    remotePort = msgs[j];

                    clientsocket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(remotePort));


                    pw = new PrintWriter(clientsocket.getOutputStream());
                    pw.write(msgToSend);
                    pw.flush();

                }
            }
            catch (UnknownHostException e) {
                Log.e(TAG, "ClientTask UnknownHostException");
            } catch (IOException e) {
                Log.e(TAG, "ClientTask socket IOException");
                System.out.println("Error" + e);
            }
            return null;
        }

    }


}