package com.metanonia.wsclient;

import com.metanonia.wsclient.model.Depth;
import com.metanonia.wsclient.model.DepthId;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;

@Slf4j
public class BinanceWebsocketClient extends WebSocketClient {
    String symbol;
    EntityManagerFactory emf;
    EntityManager em;
    EntityTransaction tx;

    public BinanceWebsocketClient(String symbol, URI serverUri) {
        super(serverUri);
        this.symbol = symbol;
        this.emf = Persistence.createEntityManagerFactory("my-persistence");
        this.em = emf.createEntityManager();
        this.tx = em.getTransaction();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("Websocket open!!");
    }

    @Override
    public void onMessage(String message) {
        try {
            tx.begin();
            JSONObject jsonObject = new JSONObject(message);
            JSONArray jBids = jsonObject.getJSONArray("bids");  // 매수
            JSONArray jAsks = jsonObject.getJSONArray("asks");  // 매도
            Integer idx = 0;
            Date now = new Date();
            for(Object bid:jBids) {
                JSONArray items = (JSONArray)bid;

                DepthId depthId = new DepthId(symbol.toUpperCase(), idx, "bid");
                Depth depth = new Depth();
                depth.setDepthId(depthId);
                depth.setPrice(items.getBigDecimal(0));
                depth.setAmt(items.getBigDecimal(1));
                depth.setTs(new Timestamp(now.getTime()));
                Depth find = em.find(Depth.class, depthId);
                if (find == null) em.persist(depth);
                else {
                    find.setPrice(depth.getPrice());
                    find.setAmt(depth.getAmt());
                    find.setTs(depth.getTs());
                    //em.flush();
                }
                idx++;
            }
            idx=0;
            for(Object ask:jAsks) {
                JSONArray items = (JSONArray)ask;

                DepthId depthId = new DepthId(symbol.toUpperCase(), idx, "ask");
                Depth depth = new Depth();
                depth.setDepthId(depthId);
                depth.setPrice(items.getBigDecimal(0));
                depth.setAmt(items.getBigDecimal(1));
                depth.setTs(new Timestamp(now.getTime()));
                Depth find = em.find(Depth.class, depthId);
                if (find == null) em.persist(depth);
                else {
                    find.setPrice(depth.getPrice());
                    find.setAmt(depth.getAmt());
                    find.setTs(depth.getTs());
                    //em.flush();
                }
                idx++;
            }
            tx.commit();
        }
        catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            log.info(message);
            e.printStackTrace();
        //log.info(e.toString());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("Close code=" + String.valueOf(code) +">> " + reason) ;
        System.exit(0);
    }

    @Override
    public void onError(Exception ex) {
        log.info(ex.toString());
    }
}
