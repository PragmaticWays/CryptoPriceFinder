package com.pragmaticways;

import java.util.Locale;
import java.util.Scanner;

public class Main {

  private static final Scanner SCANNER_IN = new Scanner(System.in);
  private static boolean isApplicationRunning = true;
  private static final CryptoApi cryptoApi = new CryptoApi();

  public static void main(String[] args) {

    while (isApplicationRunning) {
      startMenu();
    }

    shutdown();
  }

  private static void startMenu() {
    printMenu();
    String input = getUserInput();
    validateInputAndTakeAction(input);
  }

  private static void printMenu() {
    System.out.println("\n--------------------------------------");
    System.out.println("What coin price would you like to see?\n");
    for (String coinType : CryptoApi.ALL_COIN_TYPES) {
      System.out.println("\t" + coinType);
    }
    System.out.println("(type exit to quit)");
    System.out.print("> ");
  }

  private static String getUserInput() {
    return SCANNER_IN.nextLine().toLowerCase(Locale.ROOT);
  }

  private static void validateInputAndTakeAction(String input) {
    // Check if input is request to exit.
    if (input.equals("exit")) {
      isApplicationRunning = false;

      // Else check if user input is valid coin type
    } else if (CryptoApi.isCoinTypeValid(input)) {
      String price = cryptoApi.getCurrentCoinPrice(input);
      System.out.println("The current price of " + input + " is " + price);

      // Else, it's invalid. Inform the user and restart the menu.
    } else {
      System.out.println("That coin type is not supported. Please try again");
      startMenu();
    }
  }

  private static void shutdown() {
    System.out.println("Goodbye!");
  }
}