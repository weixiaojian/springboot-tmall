package com.imwj.tmall.util;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author langao_q
 * @create 2020-03-18 15:40
 * 监听服务端口是否启动（redis、elasticsearh等）
 */
public class PortUtil {
    public static boolean testPort(int port) {
        try {
            ServerSocket ss = new ServerSocket(port);
            ss.close();
            return false;
        } catch (java.net.BindException e) {
            return true;
        } catch (IOException e) {
            return true;
        }
    }

    public static void checkPort(int port, String server, boolean shutdown) {
        if(!testPort(port)) {
            if(shutdown) {
                String message =String.format("在端口 %d 未检查得到 %s 启动%n",port,server);
                JOptionPane.showMessageDialog(null, message);
                System.exit(1);
            }
            else {
                String message =String.format("在端口 %d 未检查得到 %s 启动%n,是否继续?",port,server);
                if(JOptionPane.OK_OPTION !=     JOptionPane.showConfirmDialog(null, message))
                    System.exit(1);

            }
        }
    }
}
