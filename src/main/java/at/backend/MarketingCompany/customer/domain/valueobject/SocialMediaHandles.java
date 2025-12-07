package at.backend.MarketingCompany.customer.domain.valueobject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SocialMediaHandles {
  private final Map<String, String> handles;

  private SocialMediaHandles(Map<String, String> handles) {
    this.handles = new HashMap<>(handles);
  }

  public static SocialMediaHandles create(Map<String, String> handles) {
    return new SocialMediaHandles(handles != null ? handles : Map.of());
  }

  public static SocialMediaHandles empty() {
    return new SocialMediaHandles(Map.of());
  }

  public static SocialMediaHandles fromString(String serialized) {
    if (serialized == null || serialized.isBlank()) {
      return empty();
    }

    Map<String, String> handles = new HashMap<>();
    String[] pairs = serialized.split(";");
    for (String pair : pairs) {
      String[] keyValue = pair.split(":");
      if (keyValue.length == 2) {
        handles.put(keyValue[0].trim(), keyValue[1].trim());
      }
    }
    return new SocialMediaHandles(handles);
  }

  public String toString() {
    return handles.entrySet().stream()
        .map(entry -> entry.getKey() + ":" + entry.getValue())
        .reduce((a, b) -> a + ";" + b)
        .orElse("");
  }

  public Map<String, String> getHandles() {
    return Collections.unmodifiableMap(handles);
  }

  public boolean hasHandles() {
    return !handles.isEmpty();
  }

  public String getHandle(String platform) {
    return handles.get(platform);
  }

  public SocialMediaHandles addHandle(String platform, String handle) {
    Map<String, String> newHandles = new HashMap<>(handles);
    newHandles.put(platform, handle);
    return new SocialMediaHandles(newHandles);
  }
}
