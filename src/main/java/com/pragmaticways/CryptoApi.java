package com.pragmaticways;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragmaticways.model.CoinData;
import com.pragmaticways.model.CoinDataList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class CryptoApi {

  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String COINCAP_ASSETS_BASE_API = "https://api.coincap.io/v2/assets?";
  private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("$0,000.00");

  public static final List<String> ALL_COIN_TYPES = Arrays.asList(
      "bitcoin",
      "ethereum",
      "dogecoin"
  );

  public String getCurrentCoinPrice(String coinType) {
    // parse the API response, get the price formatted a currency string

    try {
      String searchQueryParam = "search=" + coinType;
      String limitQueryParam = "limit=1";
      URI uri = new URI(COINCAP_ASSETS_BASE_API +
          String.join("&", Arrays.asList(searchQueryParam, limitQueryParam)));
      HttpRequest request = HttpRequest.newBuilder()
          .uri(uri)
          .build();

      HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

      String responseBody = response.body();

      CoinDataList coinDataList = OBJECT_MAPPER.readValue(responseBody, CoinDataList.class);
      CoinData firstCoin = coinDataList.getData().get(0);
      double priceUsd = Double.parseDouble(firstCoin.getPriceUsd());
      return PRICE_FORMAT.format(priceUsd);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
