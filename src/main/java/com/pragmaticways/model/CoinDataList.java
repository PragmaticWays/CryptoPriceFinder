package com.pragmaticways.model;

import java.util.ArrayList;

public class CoinDataList {
  private ArrayList<CoinData> data;
  private long timestamp;

  public ArrayList<CoinData> getData() {
    return data;
  }

  public void setData(ArrayList<CoinData> data) {
    this.data = data;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
