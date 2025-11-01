package com.hotel.hotel_management.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;

    private final HttpClient httpClient;
    private final Gson gson;

    private static final String SYSTEM_PROMPT =
        "You are a helpful hotel concierge assistant for a luxury hotel. " +
        "You should be friendly, professional, and informative. " +
        "Our hotel offers:\n" +
        "- Single rooms: $75/night (20m², single bed, work desk, private bathroom)\n" +
        "- Double rooms: $120/night (30m², queen bed, work desk, seating area)\n" +
        "- Suite rooms: $250/night (45m², king bed, separate living room, mini bar, jacuzzi)\n\n" +
        "Hotel amenities: 24/7 front desk, free Wi-Fi, fitness center, restaurant & bar, room service, laundry, business center, free parking, concierge.\n" +
        "Check-in: 3:00 PM | Check-out: 11:00 AM\n" +
        "Breakfast buffet: 7:00-10:00 AM ($15/person)\n" +
        "Restaurant: 12:00 PM - 10:00 PM\n" +
        "Room service: 24/7\n\n" +
        "Answer guest questions about rooms, amenities, services, policies, and help with room upgrades. " +
        "Keep responses concise but informative. Use emojis when appropriate to be friendly.";

    public OpenAIService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.gson = new Gson();
    }

    public String getChatResponse(String userMessage) {
        try {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", model);

            JsonArray messages = new JsonArray();

            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content", SYSTEM_PROMPT);
            messages.add(systemMessage);

            JsonObject userMsg = new JsonObject();
            userMsg.addProperty("role", "user");
            userMsg.addProperty("content", userMessage);
            messages.add(userMsg);

            requestBody.add("messages", messages);
            requestBody.addProperty("temperature", 0.7);
            requestBody.addProperty("max_tokens", 500);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                JsonArray choices = jsonResponse.getAsJsonArray("choices");
                if (choices != null && choices.size() > 0) {
                    JsonObject choice = choices.get(0).getAsJsonObject();
                    JsonObject message = choice.getAsJsonObject("message");
                    return message.get("content").getAsString();
                }
            } else {
                System.err.println("OpenAI API Error: " + response.statusCode() + " - " + response.body());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error calling OpenAI API: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;
    }
}

