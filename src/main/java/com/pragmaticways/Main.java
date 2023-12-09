package com.pragmaticways;

import java.util.Locale;
import java.util.Scanner;

public class Main {

  private static final Scanner SCANNER_IN = new Scanner(System.in);
  private static boolean isApplicationRunning = true;
  private static final CryptoApi cryptoApi = new CryptoApi();

  public static void main(String[] args) {


    // print price for user to see

    try {
      while (isApplicationRunning) {
        startMenu();
      }
    } catch (UserRequestsExitException e) {
      System.out.println("Goodbye!");
      // do normal shutdown
    }

  }

  private static void startMenu() {
    System.out.println("\n--------------------------------------");
    System.out.println("What coin price would you like to see?\n");
    for (String coinType : CryptoApi.ALL_COIN_TYPES) {
      System.out.println("\t" + coinType);
    }
    System.out.println("(type exit to quit)");
    System.out.print("> ");

    String input = SCANNER_IN.nextLine().toLowerCase(Locale.ROOT);

    if (input.equals("exit")) {
      isApplicationRunning = false;
      throw new UserRequestsExitException();
    }

    if (isCoinTypeValid(input)) {
      String price = cryptoApi.getCurrentCoinPrice(input);
      System.out.println("The current price of " + input + " is " + price);
    } else {
      System.out.println("That coin type is not supported. Please try again");
      startMenu();
    }
  }

  private static boolean isCoinTypeValid(String input) {
    return CryptoApi.ALL_COIN_TYPES.contains(input);
  }
}