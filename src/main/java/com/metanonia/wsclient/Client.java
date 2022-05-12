package com.metanonia.wsclient;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class Client {
    public static void main(String[] args) throws URISyntaxException {
        String symbol = "btcusdt";

        String url = "wss://stream.binance.com:9443/ws/"+symbol+"@depth5";
        BinanceWebsocketClient binanceWebsocketClient = new BinanceWebsocketClient(symbol, new URI(url));
        binanceWebsocketClient.connect();

        while (true) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
