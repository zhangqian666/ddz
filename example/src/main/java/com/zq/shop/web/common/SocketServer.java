package com.zq.shop.web.common;

import com.zq.shop.web.mappers.DUserMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/4/5 1:52 PM
 * @Package com.zq.shop.web.common
 **/


@Slf4j
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "zq.socket-server")
@Service("SocketServer")
public class SocketServer {

    /**
     * 要处理客户端发来的对象，并返回一个对象，可实现该接口。
     */
    public interface ObjectAction {
        Object doAction(Object rev, SocketServer server, Socket s);
    }

    public static final class DefaultObjectAction implements ObjectAction {
        public Object doAction(Object rev, SocketServer server, Socket s) {
//            log.info("收到消息 ： %s", rev.toString());
            return rev;
        }
    }


    private int port;
    private volatile boolean running = false;
    private long receiveTimeDelay = 8000;
    private ConcurrentHashMap<Class, ObjectAction> actionMapping = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, Socket> uidAndSocketMap = new ConcurrentHashMap<>();
    private Thread connWatchDog;

    @Autowired
    DUserMapper dUserMapper;


    public void start() {
        if (running) return;
        running = true;
        connWatchDog = new Thread(new ConnWatchDog());
        connWatchDog.start();
    }

    @SuppressWarnings("deprecation")
    public void stop() {
        if (running) running = false;
        if (connWatchDog != null) connWatchDog.stop();
    }

    public void sendMsg(Integer uid, String msg) {
        Socket socket = uidAndSocketMap.get(uid);
        if (socket != null) {
            try {
                sendObject("msg:" + msg, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendObject(Object obj, Socket s) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(obj);
        log.info("发送：" + obj);
        oos.flush();
    }


    public void addActionMap(Class cls, ObjectAction action) {
        actionMapping.put(cls, action);
    }

    class ConnWatchDog implements Runnable {
        public void run() {
            try {
                ServerSocket ss = new ServerSocket(port, 5);
                while (running) {
                    Socket s = ss.accept();
                    new Thread(new SocketAction(s)).start();
                    log.info("收到新的用户连接：" + s.getRemoteSocketAddress());
                }
            } catch (IOException e) {
                e.printStackTrace();
                SocketServer.this.stop();
            }

        }
    }

    class SocketAction implements Runnable {
        Socket s;
        boolean run = true;
        long lastReceiveTime = System.currentTimeMillis();
        int uid = 0;

        public SocketAction(Socket s) {
            this.s = s;
        }

        public void addUid(Object rev) {
            if (rev instanceof String) {
                String str = (String) rev;
                String[] split = str.split(";");
                String[] split0 = split[0].split(":");
                uid = Integer.parseInt(split0[1]);
                Socket socket = SocketServer.uidAndSocketMap.get(uid);
                if (socket == null) {
                    SocketServer.uidAndSocketMap.put(Integer.parseInt(split0[1]), s);

                    dUserMapper.updateOnlineByUid(1, uid);

                }
            }
        }

        public void run() {
            while (running && run) {
                if (System.currentTimeMillis() - lastReceiveTime > receiveTimeDelay) {
                    overThis();
                } else {
                    try {
                        InputStream in = s.getInputStream();
                        if (in.available() > 0) {
                            ObjectInputStream ois = new ObjectInputStream(in);
                            Object obj = ois.readObject();
                            lastReceiveTime = System.currentTimeMillis();
                            //将uid和socket 保存起来
                            addUid(obj);
//                            log.info("接收：\t" + obj);
                            ObjectAction oa = actionMapping.get(obj.getClass());

                            oa = oa == null ? new DefaultObjectAction() : oa;
                            Object out = oa.doAction(obj, SocketServer.this, s);
                            if (out != null) {
                                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                                oos.writeObject(out);
                                oos.flush();
                            }
                        } else {
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        overThis();
                    }
                }
            }
        }

        private void overThis() {
            if (run) run = false;
            if (s != null) {
                try {
                    s.close();
                    uidAndSocketMap.remove(uid);
                    dUserMapper.updateOnlineByUid(0, uid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            assert s != null;
            log.info("关闭：" + s.getRemoteSocketAddress());
        }


    }
}
