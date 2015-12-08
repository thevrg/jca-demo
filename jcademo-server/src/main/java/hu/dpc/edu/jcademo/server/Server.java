/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.dpc.edu.jcademo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vrg
 */
public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(6789);
        AtomicInteger counter = new AtomicInteger();
        while (true) {
            final Socket socket = ss.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int id = counter.incrementAndGet();
                    System.out.println("New client connection #" + id);
                    try (PrintWriter out = new PrintWriter(socket.getOutputStream());
                         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"))) {
                        String line;
                        while ((line = in.readLine()) != null) {
                            System.out.println("Client #" + id + ": " + line);
                            out.println("Client #" + id + " response: " + line);
                            out.flush();
                        }
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        }
    }
}
