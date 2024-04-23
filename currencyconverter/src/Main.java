import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {
    public static void main(String[] args) {
        currencyConverter();
    }

    public static void currencyConverter() {
        Scanner scanner = new Scanner(System.in);
        boolean proceed = true;

        while (proceed) {
            System.out.println("Bem-vindo(a) ao conversor de moedas");

            System.out.println("**");
            System.out.println("\nDigite sua opção: ");
            System.out.println("1. Dólar => Peso argentino ");
            System.out.println("2. Peso argentino => Dólar ");
            System.out.println("3. Dólar => Real brasileiro");
            System.out.println("4. Real brasileiro => Dólar");
            System.out.println("5. Dólar => Peso colombiano");
            System.out.println("6. Peso colombiano => Dólar");
            System.out.println("7. Sair");
            System.out.println("\n***");

            int choiceNum = scanner.nextInt();

            switch (choiceNum) {
                case 1:
                    convertCurrency("USD", "ARS", scanner);
                    break;
                case 2:
                    convertCurrency("ARS", "USD", scanner);
                    break;
                case 3:
                    convertCurrency("USD", "BRL", scanner);
                    break;
                case 4:
                    convertCurrency("BRL", "USD", scanner);
                    break;
                case 5:
                    convertCurrency("USD", "COP", scanner);
                    break;
                case 6:
                    convertCurrency("COP", "USD", scanner);
                    break;
                case 7:
                    System.out.println("Saindo...");
                    proceed = false;
                    break;
                default:
                    System.out.println("Escolha uma opção válida!");
                    break;
            }
        }
        scanner.close();
    }

    private static void convertCurrency(String fromCurrency, String toCurrency, Scanner scanner) {
        try {
            String apiKey = "87be6ae2ff6b25d9d4052f45";
            String urlStr = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + fromCurrency;

            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader(request.getInputStream()));
            JsonObject jsonObject = root.getAsJsonObject();

            String reqResult = jsonObject.get("result").getAsString();

            if (reqResult.equals("success")) {
                JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");
                double exchangeRate = rates.get(toCurrency).getAsDouble();
                System.out.println("Digite o valor em " + fromCurrency + ": ");
                double amount = scanner.nextDouble();
                double convertedAmount = amount * exchangeRate;
                System.out.println("O valor em " + toCurrency + " é: " + convertedAmount);

                // Perguntar ao usuário se deseja fazer outra conversão
                int choice;
                do {
                    System.out.println("Deseja fazer uma nova conversão? Digite 1 para SIM e 2 para NÃO");
                    choice = scanner.nextInt();
                } while (choice != 1 && choice != 2);

                if (choice == 2) {
                    System.out.println("Saindo...");
                    System.exit(0);
                }
            } else {
                System.out.println("Falha ao obter as taxas de câmbio.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}