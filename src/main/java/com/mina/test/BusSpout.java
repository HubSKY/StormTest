package com.mina.test;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.Map;



/**
 * Created by User on 2017/4/7.
 */
public class BusSpout implements IRichSpout{
    private static final long serialVersionUID = 1L;
    Socket socket = null;
    DataInputStream in;
    SpoutOutputCollector spoutcollector;
    String servip = AppConfig.Default.GetString("data.bus.host");
    int servport = AppConfig.Default.GetInt("data.bus.port");
    String request = AppConfig.Default.GetString("data.bus.command");
    private Logger logger;

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector)
    {
        this.spoutcollector = collector;

        this.logger = new Logger(getClass().getName());

        while ((this.socket == null) || (!this.socket.isConnected()))
            try {
                connectServer(this.servip, this.servport);
            }
            catch (Exception e)
            {
            }
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
        }
    }

    public void activate() {
    }

    public void deactivate() {
    }

    public void ack(Object msgId) {
    }

    public void fail(Object msgId) {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(new String[] { "type", "databuffer" }));
    }

    public Map<String, Object> getComponentConfiguration()
    {
        return null;
    }

    public void connectServer(String ip, int port) throws Exception
    {
        while ((this.socket == null) || (!this.socket.isConnected()))
            try {
                this.socket = new Socket(ip, port);
                this.in = new DataInputStream(this.socket.getInputStream());
                OutputStream out = this.socket.getOutputStream();
                out.write(this.request.getBytes());
                out.flush();
            } catch (Exception e) {
                this.logger.exception(e);
                e.printStackTrace();
            }
    }

    public void nextTuple()
    {
        while (true)
            try
            {
                int try_counter = 0;
                if (this.in.available() < 500)
                {
                    Calendar c = Calendar.getInstance();

                    int hour = c.get(11);

                    if ((23 >= hour) && (hour >= 6))
                    {
                        Thread.sleep(1000L);
                        try_counter++;
                        if (try_counter == 20)
                        {
                            this.in.close();
                            this.socket.close();

                            if ((this.socket.isClosed() == true) || (!this.socket.isConnected())) {
                                try {
                                    if (this.servport == 9999)
                                        this.servport = 55499;
                                    else
                                        this.servport = 9999;
                                    this.socket = new Socket(this.servip, this.servport);
                                    this.in = new DataInputStream(this.socket.getInputStream());

                                    OutputStream out = this.socket.getOutputStream();
                                    out.write(this.request.getBytes());
                                    try_counter = 0;
                                } catch (Exception e) {
                                    Thread.sleep(1000L);
                                }continue;
                            }
                        }

                    }

                }
                else if ((this.in.readByte() == -6) && (this.in.readByte() == -11))
                {
                    this.in.skip(5L);
                    byte msgtype = this.in.readByte();
                    this.in.skip(42L);

                    int type = -1;
                    byte[] databuffer;
                    if (msgtype == 1) {
                         databuffer = new byte[82];
                        type = 1;
                    } else if (msgtype == 2) {
                         databuffer = new byte[82];
                        type = 2;
                    } else {
                        databuffer = new byte[60];
                        type = 0;
                    }

                    this.in.read(databuffer);

                    this.spoutcollector.emit(new Values(new Object[] { Integer.valueOf(type), databuffer }));
                }
            } catch (Exception e) {
                this.logger.exception(e);
                e.printStackTrace();
            }
    }
}
