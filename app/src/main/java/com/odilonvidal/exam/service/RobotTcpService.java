package com.odilonvidal.exam.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotTcpService {
  private static final String TAG = "RobotTcpService";
  private static RobotTcpService instance;
  private Handler handler;
  private final ExecutorService executorService;
  private boolean isConnected = false;

  public static final int MESSAGE_CONNECTED = 1;
  public static final int MESSAGE_CONNECTION_FAILED = -1;
  public static final int MESSAGE_COMMAND_SUCCESS = 2;
  public static final int MESSAGE_COMMAND_ERROR = -2;

  private RobotTcpService() {
    executorService = Executors.newSingleThreadExecutor();
  }

  public static RobotTcpService getInstance() {
    if (instance == null) {
      instance = new RobotTcpService();
    }
    return instance;
  }

  public void setHandler(Handler handler) {
    this.handler = handler;
  }

  public void connect() {
    executorService.execute(() -> {
      try {
        Thread.sleep(1000);

        isConnected = true;
        if (handler != null) {
          Message message = Message.obtain();
          message.what = MESSAGE_CONNECTED;
          message.obj = "Connecté au robot";
          handler.sendMessage(message);
        }
      } catch (InterruptedException e) {
        Log.e(TAG, "Erreur lors de la connexion", e);
        if (handler != null) {
          Message message = Message.obtain();
          message.what = MESSAGE_CONNECTION_FAILED;
          message.obj = "Échec de la connexion";
          handler.sendMessage(message);
        }
      }
    });
  }

  public void disconnect() {
    isConnected = false;
  }

  public void sendCommand(String command) {
    if (!isConnected) {
      if (handler != null) {
        Message message = Message.obtain();
        message.what = MESSAGE_COMMAND_ERROR;
        message.obj = "Non connecté au robot";
        handler.sendMessage(message);
      }
      return;
    }

    executorService.execute(() -> {
      try {
        Thread.sleep(500);

        String response;
        switch (command) {
          case "START":
            response = "START OK";
            break;
          case "STOP":
            response = "STOP OK";
            break;
          case "DIRECT_LEFT":
            response = "GAUCHE OK";
            break;
          case "DIRECT_FRONT":
            response = "AVANCER OK";
            break;
          case "DIRECT_RIGHT":
            response = "DROITE OK";
            break;
          default:
            response = "Commande inconnue";
        }

        if (handler != null) {
          Message message = Message.obtain();
          message.what = MESSAGE_COMMAND_SUCCESS;
          message.obj = response;
          handler.sendMessage(message);
        }
      } catch (InterruptedException e) {
        Log.e(TAG, "Erreur lors de l'envoi de la commande", e);
        if (handler != null) {
          Message message = Message.obtain();
          message.what = MESSAGE_COMMAND_ERROR;
          message.obj = "Erreur d'envoi: " + e.getMessage();
          handler.sendMessage(message);
        }
      }
    });
  }
}