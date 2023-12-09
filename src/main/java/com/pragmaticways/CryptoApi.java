package com.pragmaticways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragmaticways.model.CoinData;
import com.pragmaticways.model.CoinDataList;

import java.net.URI;
import java.net.URISyntaxException;
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

  static boolean isCoinTypeValid(String input) {
    return ALL_COIN_TYPES.contains(input);
  }

  public String getCurrentCoinPrice(String coinType) {
    try {
      // Create the coincap API URL
      URI uri = initCoinCapAssetsApi(coinType);

      // Create the HTTP request
      HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

      // Send the request and get the HTTP response
      HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

      // Get the JSON response body
      String responseBody = response.body();

      // Get the first coin from the JSON response
      CoinData firstCoin = getCoinDataFromJsonResponse(responseBody);

      // Format the price and return
      return formatCoinPriceUsd(firstCoin);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String formatCoinPriceUsd(CoinData firstCoin) {
    double priceUsd = Double.parseDouble(firstCoin.getPriceUsd());
    return PRICE_FORMAT.format(priceUsd);
  }

  private static CoinData getCoinDataFromJsonResponse(String responseBody) throws JsonProcessingException {
    // Parse the JSON into a CoinDataList object.
    CoinDataList coinDataList = OBJECT_MAPPER.readValue(responseBody, CoinDataList.class);

    // Get the first (and only, since we're using limit=1 in the request) coin data from the data list.
    return coinDataList.getData().get(0);
  }

  private static URI initCoinCapAssetsApi(String coinType) throws URISyntaxException {
    // Build the query params string
    String searchQueryParam = "search=" + coinType;
    String limitQueryParam = "limit=1";
    String queryParams = String.join("&", Arrays.asList(searchQueryParam, limitQueryParam));

    // Init a new URI with the API and query params.
    return new URI(COINCAP_ASSETS_BASE_API + queryParams);
  }
}
