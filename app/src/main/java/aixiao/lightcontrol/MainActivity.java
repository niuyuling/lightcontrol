package aixiao.lightcontrol;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    /**
     *  ./c 192.168.1.102 "/root/gpio/info_light 5 1"
     *  ./c 192.168.1.102 "/root/gpio/info_light 5 0"
     */

    private EditText ed_start, ed_stop;
    private CardView start;
    private CardView stop;
    private EditText ed_start_2, ed_stop_2;
    private CardView start_2;
    private CardView stop_2;
    private Context context = MainActivity.this;
    private ProgressBar progressBar;
    private String commonPath;
    //private String startPath = "data/data/aixiao.lightcontrol/c 192.168.1.102 \"/root/gpio/info_light 5 1";
    AlertDialog dialog;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressBar.setVisibility(View.GONE);
                    dialog = new AlertDialog.Builder(context)
                            .setTitle("执行结果")
                            .setMessage((String) msg.obj)
                            .setPositiveButton("确定", null)
                            .show();
                    break;

                case 0:
                    progressBar.setVisibility(View.GONE);
                    dialog = new AlertDialog.Builder(context)
                            .setTitle("执行结果")
                            .setMessage((String) msg.obj)
                            .setPositiveButton("确定", null)
                            .show();
                    break;
            }
            return false;
        }
    });
    private String TAG = MainActivity.class.getSimpleName();
    private View view;
    private String startPath, stopPath;
    private String startPath2, stopPath2;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = context.getSharedPreferences("data", MODE_PRIVATE);

        startPath = sp.getString("start", " 192.168.1.102 /root/gpio/info_light 5 1");
        stopPath = sp.getString("stop", " 192.168.1.102 /root/gpio/info_light 5 0");

        startPath2 = sp.getString("start2", " 192.168.1.103 /root/gpio/info_light 5 1");
        stopPath2 = sp.getString("stop2", " 192.168.1.103 /root/gpio/info_light 5 0");

        initView();
        initBinaryFile();
    }

    /**
     *  检查二进制文件是否存在，不存在则重新创建
     */
    private void initBinaryFile() {
        try {
            String absolutePath = context.getFilesDir().getAbsoluteFile().getAbsolutePath();

            File file = new File(absolutePath + "/c");

            commonPath = file.getAbsolutePath() + " ";

            System.out.println(commonPath);

            if (!file.exists()) {

                InputStream c = context.getAssets().open("c");
                FileUtils.copyInputStreamToFile(c, file);

                file.setExecutable(true);
                file.setReadable(true);
                file.setWritable(true);

                Toast.makeText(context, "初始化二进制文件成功.", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(context, "二进制文件已存在.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(context, "初始化二进制文件失败.", Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        progressBar = findViewById(R.id.pbar);
        start = (CardView) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ShellUtilss.CommandResult commandResult = ShellUtilss.execCommand(commonPath + startPath, true, true);
                            Message message = new Message();
                            message.obj = commandResult.successMsg;
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }).start();
                } else {
                    //还在执行..
                    Toast.makeText(context, "执行中...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop = (CardView) findViewById(R.id.stop);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ShellUtilss.CommandResult commandResult = ShellUtilss.execCommand(commonPath + stopPath, true, true);
                            Message message = new Message();
                            message.obj = commandResult.successMsg;
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }).start();
                } else {
                    //还在执行..
                    Toast.makeText(context, "执行中...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        start_2 = (CardView) findViewById(R.id.start2);
        start_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ShellUtilss.CommandResult commandResult = ShellUtilss.execCommand(commonPath + startPath2, true, true);
                            Message message = new Message();
                            message.obj = commandResult.successMsg;
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }).start();
                } else {
                    //还在执行..
                    Toast.makeText(context, "执行中...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        stop_2 = (CardView) findViewById(R.id.stop2);

        stop_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ShellUtilss.CommandResult commandResult = ShellUtilss.execCommand(commonPath + stopPath2, true, true);
                            Message message = new Message();
                            message.obj = commandResult.successMsg;
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }).start();
                } else {
                    //还在执行..
                    Toast.makeText(context, "执行中...", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void ExecShell(final String shell) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShellUtilss.CommandResult commandResult = ShellUtilss.execCommand(shell, true);
                Message message = new Message();
                message.obj = commandResult.successMsg;
                message.what = 111;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 设置路径响应事件
     *
     * @param item
     * @return
     */
    @SuppressLint({"InflateParams", "ResourceType"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                view = LayoutInflater.from(context).inflate(R.layout.layout, null, false);
                ed_start = view.findViewById(R.id.startPath);
                ed_stop = view.findViewById(R.id.stopPath);
                ed_start.setText(startPath);
                ed_stop.setText(stopPath);

                ed_start_2 = view.findViewById(R.id.startPath2);
                ed_stop_2 = view.findViewById(R.id.stopPath2);
                ed_start_2.setText(startPath2);
                ed_stop_2.setText(stopPath2);

                new AlertDialog.Builder(context)
                        .setTitle("设置")
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startPath = ed_start.getText().toString();
                                stopPath = ed_stop.getText().toString();
                                startPath2 = ed_start_2.getText().toString();
                                stopPath2 = ed_stop_2.getText().toString();
                                sp.edit().putString("start", ed_start.getText().toString()).apply();
                                sp.edit().putString("stop", ed_stop.getText().toString()).apply();
                                sp.edit().putString("start2", ed_start_2.getText().toString()).apply();
                                sp.edit().putString("stop2", ed_stop_2.getText().toString()).apply();
                            }

                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println(sp.getString("start", "start is null"));
                                System.out.println(sp.getString("stop", "stop is null"));
                                System.out.println(sp.getString("start2", "start2 is null"));
                                System.out.println(sp.getString("stop2", "stop2 is null"));
                            }
                        })
                        .show();
                break;
            case R.id.about_fill:
                view = LayoutInflater.from(context).inflate(R.layout.layout_about, null, false);
                StringBuffer sbf = new StringBuffer();
                sbf.append("贡献者：\n");
                sbf.append("\t\taixiao.me@qq.com\n");
                sbf.append("\t\t1214585092@qq.com\n");
                sbf.append("\t\t1225803134@qq.com\n");

                sbf.append("");
                new AlertDialog.Builder(context)
                        .setTitle("关于LightControl")
                        .setView(null)
                        .setMessage(sbf.toString())
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
