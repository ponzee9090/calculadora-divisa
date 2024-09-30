
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

// Clase que mapea la respuesta de la API
class ExchangeRateResponse {
    String result;
    String base_code;
    String target_code;
    double conversion_rate;
}

public class App {
   
// Método para obtener el tipo de cambio entre dos monedas
private static double getExchangeRate(String fromCurrency, String toCurrency) {
    String apiKey = "TU_CLAVE_AQUI"; // Reemplaza con tu clave API
    String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + fromCurrency + "/" + toCurrency;

    try {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            connection.disconnect();

            // Convertir el JSON a un objeto Java usando Gson
            Gson gson = new Gson();
            ExchangeRateResponse exchangeRateResponse = gson.fromJson(content.toString(), ExchangeRateResponse.class);

            // Verificar si el resultado fue exitoso
            if ("success".equals(exchangeRateResponse.result)) {
                return exchangeRateResponse.conversion_rate;
            } else {
                System.out.println("Error: " + exchangeRateResponse.result);
            }
        } else {
            System.out.println("Error: no se pudo obtener una respuesta válida de la API. Código de respuesta: " + responseCode);
        }
    } catch (Exception e) {
        System.out.println("Error al obtener el tipo de cambio: " + e.getMessage());
    }
    return 0.0;
}
// Método para convertir una cantidad de una moneda a otra
public static double convertCurrency(String fromCurrency, String toCurrency, double amount) {
    double exchangeRate = getExchangeRate(fromCurrency, toCurrency);
    return amount * exchangeRate;
}
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Conversor de Divisas");
    System.out.println("====================");

    // Obtener la moneda de origen
    System.out.print("Introduce la moneda de origen (por ejemplo, USD): ");
    String fromCurrency = scanner.nextLine().toUpperCase();

    // Obtener la moneda de destino
    System.out.print("Introduce la moneda de destino (por ejemplo, EUR): ");
    String toCurrency = scanner.nextLine().toUpperCase();

    // Obtener la cantidad a convertir
    System.out.print("Introduce la cantidad a convertir: ");
    double amount = scanner.nextDouble();

    // Realizar la conversión
    double convertedAmount = convertCurrency(fromCurrency, toCurrency, amount);

    // Mostrar el resultado
    System.out.println("Cantidad convertida: " + convertedAmount + " " + toCurrency);

    scanner.close();
}
    }

