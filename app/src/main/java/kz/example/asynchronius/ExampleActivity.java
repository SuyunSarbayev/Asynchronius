package kz.example.asynchronius;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ExampleActivity extends Activity implements View.OnClickListener{

    TextView text;
    Button button;;

    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        text = (TextView)findViewById(R.id.text);
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(this);
    }

    public Boolean threadImplementation(int thread) {

        long endTime = System.currentTimeMillis() + 10 * 1000;


        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }

        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("Key", Integer.toString(thread));
        msg.setData(bundle);
        handler.sendMessage(msg);
        return true;
    }

    public Boolean brokenApp(){

        long endTime = System.currentTimeMillis() + 10 * 1000;


        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }

        text.setText("HELLO!");
        return true;
    }

    public Boolean dynamicThread(final int threadNumber) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                threadImplementation(threadNumber);
            }
        });
        return true;
    }

    public Boolean customThread(int threadNumber){
        CustomThread thread = new CustomThread("name", threadNumber);
        thread.start();

        return true;
    }

    public Boolean asyncTaskMethod(int threadNumber){
        Task myTask = new Task(threadNumber);
        myTask.execute();

        return true;
    }

    @Override
    public void onClick(View v) {
        brokenApp();
        //dynamicThread(i);
        //customThread(i);

        //Task task = new Task(i);
        //task.execute();
        i++;
    }



    // SEPARATE THREAD EXAMPLE

    class CustomThread extends Thread{

        int threadNumber = 0;

        public CustomThread(String customthread, int i){
            super(customthread);
            this.threadNumber = i;
        }

        @Override
        public void run() {
            super.run();
            threadImplementation(threadNumber);
        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            String data = bundle.getString("Key");

            text.setText(data);
        }
    };

    ///////////////////////////////////////////


    //ASYNC TASK EXAMPLE

    // input, progress, result
    class Task extends AsyncTask<Integer, Void, Boolean>{

        int number = 0;

        public Task(int number){
            this.number = number;
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            //initializeData(integers);
            long endTime = System.currentTimeMillis() + 10 * 1000;


            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            text.setText("Начинаем");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            text.setText(Integer.toString(number));
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
