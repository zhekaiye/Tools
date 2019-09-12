package com.getsystemlog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetSystemLog {

    private static final String SYSTEM_LOG_TAG = "ActivityManager";

    public static void main(String[] args) {

    }

    private void printSystemLog() {
        Process logcatProcess = null;
        BufferedReader bufReader = null;
        DataOutputStream dataOutputStream = null;
        try {
            if (VersionUtils.isJellyBean()) {
                logcatProcess = Runtime.getRuntime().exec("su");
                dataOutputStream = new DataOutputStream(logcatProcess.getOutputStream());
                dataOutputStream.writeBytes("logcat " + SYSTEM_LOG_TAG + " *:S" + "\n");
                dataOutputStream.flush();
            } else {
                logcatProcess = Runtime.getRuntime().exec("logcat " + SYSTEM_LOG_TAG + " *:S");
                dataOutputStream = new DataOutputStream(logcatProcess.getOutputStream());
            }
            bufReader = new BufferedReader(new InputStreamReader(logcatProcess.getInputStream()));
            String line;
            while ((line = bufReader.readLine()) != null) {
                if (line.indexOf(BaseApplicationImpl.processName) >= 0) {
                    QLog.d(TAG, QLog.USR, "printSystemLog: "  + line);
                }
            }
            dataOutputStream.writeBytes("logcat -c\n");
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            if (VersionUtils.isJellyBean()) {
                logcatProcess.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}