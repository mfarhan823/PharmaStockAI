package pharmastock.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChatBot {


    private static final String API_KEY = ConfigLoader.getApiKey();
    private static final String API_URL = ConfigLoader.getApiUrl();
    private static final String MODEL_NAME = ConfigLoader.getModelName();

    private HttpClient client;
    private String inventoryContext;

    public ChatBot() {
        this.client = HttpClient.newHttpClient();
        this.inventoryContext = "";

        if (API_KEY != null && !API_KEY.isEmpty()) {
            System.out.println(" ChatBot initialized with model: " + MODEL_NAME);
        } else {
            System.err.println(" WARNING: API Key not loaded! ChatBot won't work.");
        }
    }

    public void setInventoryContext(String inventoryText) {
        this.inventoryContext = inventoryText;
    }

    public String getResponse(String userMessage) {
        try {
            String systemPrompt =
                    "You are PharmaBot, a medical and pharmacy assistant.\n" +
                    "\n" +
                    "ALLOWED topics (answer these fully):\n" +
                    "- Diseases and medical conditions (cancer, tumor, diabetes, hypertension, infections, etc.)\n" +
                    "- Symptoms, causes, and treatments of any illness\n" +
                    "- Medicines, dosage, side effects, drug interactions\n" +
                    "- OTC medicine suggestions for symptoms\n" +
                    "- Inventory questions (stock, price, availability)\n" +
                    "- General health, nutrition, and medical advice\n" +
                    "\n" +
                    "STRICT RULES:\n" +
                    "1. If user asks ANYTHING unrelated to health/medicine/pharmacy (essays, coding, jokes, math, politics, etc.) reply ONLY: 'I can only help with medical and pharmacy related questions.'\n" +
                    "2. NEVER recommend prescription drugs for self-medication\n" +
                    "3. For serious conditions always add: 'Please consult a doctor.'\n" +
                    "4. Do NOT engage in any off-topic conversation under any circumstances\n" +
                    "\n" +
                    "Current Inventory:\n" + inventoryContext;

            String requestBody = "{"
                    + "\"model\":\"" + MODEL_NAME + "\","
                    + "\"messages\":["
                    + "{\"role\":\"system\",\"content\":" + toJson(systemPrompt) + "},"
                    + "{\"role\":\"user\",\"content\":" + toJson(userMessage) + "}"
                    + "],"
                    + "\"max_tokens\":300"
                    + "}";

            System.out.println("📤 Sending request to: " + MODEL_NAME);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(" Status Code: " + response.statusCode());

            if (response.statusCode() != 200) {
                return " API Error: " + response.statusCode();
            }

            return parseResponse(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return " Error: " + e.getMessage();
        }
    }

    private String parseResponse(String json) {
        try {
            String searchFor = "\"content\":";
            int contentStart = json.indexOf(searchFor);

            if (contentStart == -1) {
                return "Could not find content in response";
            }

            contentStart += searchFor.length();

            while (contentStart < json.length() &&
                    (json.charAt(contentStart) == ' ' || json.charAt(contentStart) == '\t')) {
                contentStart++;
            }

            if (contentStart < json.length() && json.charAt(contentStart) == '"') {
                contentStart++;
            } else {
                return "Invalid content format";
            }

            StringBuilder content = new StringBuilder();
            boolean escaped = false;

            for (int i = contentStart; i < json.length(); i++) {
                char c = json.charAt(i);

                if (escaped) {
                    if (c == 'n') content.append('\n');
                    else if (c == 't') content.append('\t');
                    else if (c == '"') content.append('"');
                    else if (c == '\\') content.append('\\');
                    else content.append(c);
                    escaped = false;
                    continue;
                }

                if (c == '\\') {
                    escaped = true;
                    continue;
                }

                if (c == '"') {
                    break;
                }

                content.append(c);
            }

            String result = content.toString().trim();

            if (result.isEmpty()) {
                int afterColon = json.indexOf(searchFor) + searchFor.length();
                int nextComma = json.indexOf(",", afterColon);
                if (nextComma == -1) nextComma = json.indexOf("}", afterColon);
                result = json.substring(afterColon, nextComma).trim();
                result = result.replaceAll("^\"|\"$", "");
            }

            return result.isEmpty() ? "No response content found." : result;

        } catch (Exception e) {
            return "Error reading response: " + e.getMessage();
        }
    }

    private String toJson(String text) {
        if (text == null) return "\"\"";
        text = text.replace("\\", "\\\\");
        text = text.replace("\"", "\\\"");
        text = text.replace("\n", "\\n");
        text = text.replace("\r", "\\r");
        text = text.replace("\t", "\\t");
        return "\"" + text + "\"";
    }
}