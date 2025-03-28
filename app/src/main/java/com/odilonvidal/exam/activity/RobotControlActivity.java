package com.odilonvidal.exam.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.odilonvidal.exam.R;
import com.odilonvidal.exam.service.RobotTcpService;

public class RobotControlActivity extends AppCompatActivity {

  private Button buttonStartStop;
  private Button buttonLeft;
  private Button buttonFront;
  private Button buttonRight;
  private TextView statusTextView;
  private boolean isStarted = false;

  private RobotTcpService robotClient;

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_robot_control);

    buttonStartStop = findViewById(R.id.buttonStartStop);
    buttonLeft = findViewById(R.id.buttonLeft);
    buttonFront = findViewById(R.id.buttonFront);
    buttonRight = findViewById(R.id.buttonRight);
    statusTextView = findViewById(R.id.statusTextView);

    setAllButtonsEnabled(false);
    statusTextView.setText("Connexion au robot en cours...");

    Handler handler = new Handler(Looper.getMainLooper()) {
      @Override
      public void handleMessage(Message msg) {
        String messageText = msg.obj != null ? msg.obj.toString() : "";

        switch (msg.what) {
          case RobotTcpService.MESSAGE_CONNECTED:
            Toast.makeText(RobotControlActivity.this, messageText, Toast.LENGTH_SHORT).show();
            statusTextView.setText("Connecté au robot");
            buttonStartStop.setEnabled(true);
            break;

          case RobotTcpService.MESSAGE_CONNECTION_FAILED:
            Toast.makeText(RobotControlActivity.this, messageText, Toast.LENGTH_SHORT).show();
            statusTextView.setText("Erreur: " + messageText);
            setAllButtonsEnabled(false);
            break;

          case RobotTcpService.MESSAGE_COMMAND_SUCCESS:
            statusTextView.setText(messageText);
            if (messageText.contains("START")) {
              isStarted = true;
              buttonStartStop.setText("ARRÊTER");
              buttonStartStop.setBackgroundTintList(getColorStateList(android.R.color.holo_red_light));
              setDirectionButtonsEnabled(true);
            } else if (messageText.contains("STOP")) {
              isStarted = false;
              buttonStartStop.setText("DÉMARRER");
              buttonStartStop.setBackgroundTintList(getColorStateList(android.R.color.holo_green_light));
              setDirectionButtonsEnabled(false);
            }
            break;

          case RobotTcpService.MESSAGE_COMMAND_ERROR:
            Toast.makeText(RobotControlActivity.this, messageText, Toast.LENGTH_SHORT).show();
            statusTextView.setText("Erreur: " + messageText);
            break;
        }
      }
    };

    robotClient = RobotTcpService.getInstance();
    robotClient.setHandler(handler);
    robotClient.connect();

    buttonStartStop.setOnClickListener(v -> {
      String command = isStarted ? "STOP" : "START";
      robotClient.sendCommand(command);
    });

    buttonLeft.setOnClickListener(v -> robotClient.sendCommand("DIRECT_LEFT"));
    buttonFront.setOnClickListener(v -> robotClient.sendCommand("DIRECT_FRONT"));
    buttonRight.setOnClickListener(v -> robotClient.sendCommand("DIRECT_RIGHT"));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (robotClient != null) {
      robotClient.disconnect();
    }
  }

  private void setDirectionButtonsEnabled(boolean enabled) {
    buttonLeft.setEnabled(enabled);
    buttonFront.setEnabled(enabled);
    buttonRight.setEnabled(enabled);
  }

  private void setAllButtonsEnabled(boolean enabled) {
    buttonStartStop.setEnabled(enabled);
    setDirectionButtonsEnabled(enabled);
  }
}