import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClentService {
    private final HttpClient client;
    private final Gson gson;
    private final String baseUrl;

    public ClentService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.gson = new Gson();
        this.baseUrl = System.getProperty("hotel.backend.url", "http://localhost:8080");
    }

    public JsonObject employeeLogin(String email, String password) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/auth/employee/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            return JsonParser.parseString(resp.body()).getAsJsonObject();
        }
        return null;
    }

    public JsonObject guestLogin(String email, String password) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/auth/guest/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            return JsonParser.parseString(resp.body()).getAsJsonObject();
        }
        return null;
    }

    public JsonObject getGuestRoom(long guestId) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/guest-portal/" + guestId + "/room"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            return JsonParser.parseString(resp.body()).getAsJsonObject();
        }
        return null;
    }

    public JsonArray getAvailableUpgrades(long guestId) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/guest-portal/available-upgrades?guestId=" + guestId))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            JsonElement e = JsonParser.parseString(resp.body());
            if (e.isJsonArray()) return e.getAsJsonArray();
        }
        return new JsonArray();
    }

    public JsonObject requestUpgrade(long guestId, long requestedRoomId) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("guestId", guestId);
        body.addProperty("requestedRoomId", requestedRoomId);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/guest-portal/upgrade-request"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            return JsonParser.parseString(resp.body()).getAsJsonObject();
        }
        return null;
    }
}
