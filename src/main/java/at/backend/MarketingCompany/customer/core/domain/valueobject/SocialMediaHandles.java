package at.backend.MarketingCompany.customer.core.domain.valueobject;

import at.backend.MarketingCompany.customer.core.domain.exceptions.CustomerDomainException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public record SocialMediaHandles(String twitterHandle, String instagramHandle, String facebookUrl, String linkedinUrl,
                                 String youtubeHandle, String tiktokHandle, Map<String, String> otherPlatforms) {

    private static final Pattern TWITTER_PATTERN = Pattern.compile("^@?[A-Za-z0-9_]{1,15}$");
    private static final Pattern INSTAGRAM_PATTERN = Pattern.compile("^@?[A-Za-z0-9._]{1,30}$");
    private static final Pattern FACEBOOK_PATTERN = Pattern.compile("^[a-z\\d.]{5,}$");
    private static final Pattern LINKEDIN_PATTERN = Pattern.compile("^[a-zA-Z0-9-]{3,100}$");
    private static final Pattern YOUTUBE_PATTERN = Pattern.compile("^@[A-Za-z0-9._-]{3,30}$");
    private static final Pattern TIKTOK_PATTERN = Pattern.compile("^@[A-Za-z0-9._]{1,24}$");

    private static final SocialMediaHandles EMPTY = new SocialMediaHandles(
            null, null, null, null, null, null, Collections.emptyMap()
    );

    // Factory methods
    public static SocialMediaHandles empty() {
        return EMPTY;
    }

    public static SocialMediaHandles create(
            String twitterHandle,
            String instagramHandle,
            String facebookUrl,
            String linkedinUrl,
            String youtubeHandle,
            String tiktokHandle) {

        validateTwitterHandle(twitterHandle);
        validateInstagramHandle(instagramHandle);
        validateFacebookUrl(facebookUrl);
        validateLinkedinUrl(linkedinUrl);
        validateYoutubeHandle(youtubeHandle);
        validateTiktokHandle(tiktokHandle);

        return new SocialMediaHandles(
                normalizeTwitterHandle(twitterHandle),
                normalizeInstagramHandle(instagramHandle),
                normalizeFacebookUrl(facebookUrl),
                normalizeLinkedinUrl(linkedinUrl),
                normalizeYoutubeHandle(youtubeHandle),
                normalizeTiktokHandle(tiktokHandle),
                new HashMap<>()
        );
    }

    public SocialMediaHandles(String twitterHandle, String instagramHandle, String facebookUrl,
                              String linkedinUrl, String youtubeHandle, String tiktokHandle,
                              Map<String, String> otherPlatforms) {

        this.twitterHandle = twitterHandle;
        this.instagramHandle = instagramHandle;
        this.facebookUrl = facebookUrl;
        this.linkedinUrl = linkedinUrl;
        this.youtubeHandle = youtubeHandle;
        this.tiktokHandle = tiktokHandle;

        this.otherPlatforms = otherPlatforms != null ?
                Map.copyOf(otherPlatforms) :
                Collections.emptyMap();
    }

    public static SocialMediaHandles fromMap(Map<String, String> handles) {
        if (handles == null || handles.isEmpty()) {
            return empty();
        }

        Map<String, String> normalizedHandles = new HashMap<>();
        Map<String, String> otherPlatforms = new HashMap<>();

        for (Map.Entry<String, String> entry : handles.entrySet()) {
            String platform = entry.getKey().toLowerCase().trim();
            String handle = entry.getValue() != null ? entry.getValue().trim() : null;

            if (handle == null || handle.isBlank()) {
                continue;
            }

            switch (platform) {
                case "twitter":
                    validateTwitterHandle(handle);
                    normalizedHandles.put("twitter", normalizeTwitterHandle(handle));
                    break;
                case "instagram":
                    validateInstagramHandle(handle);
                    normalizedHandles.put("instagram", normalizeInstagramHandle(handle));
                    break;
                case "facebook":
                    validateFacebookUrl(handle);
                    normalizedHandles.put("facebook", normalizeFacebookUrl(handle));
                    break;
                case "linkedin":
                    validateLinkedinUrl(handle);
                    normalizedHandles.put("linkedin", normalizeLinkedinUrl(handle));
                    break;
                case "youtube":
                    validateYoutubeHandle(handle);
                    normalizedHandles.put("youtube", normalizeYoutubeHandle(handle));
                    break;
                case "tiktok":
                    validateTiktokHandle(handle);
                    normalizedHandles.put("tiktok", normalizeTiktokHandle(handle));
                    break;
                default:
                    otherPlatforms.put(platform, handle);
            }
        }

        return new SocialMediaHandles(
                normalizedHandles.get("twitter"),
                normalizedHandles.get("instagram"),
                normalizedHandles.get("facebook"),
                normalizedHandles.get("linkedin"),
                normalizedHandles.get("youtube"),
                normalizedHandles.get("tiktok"),
                Collections.unmodifiableMap(otherPlatforms)
        );
    }

    public static SocialMediaHandles fromString(String socialMediaString) {
        if (socialMediaString == null || socialMediaString.isBlank()) {
            return empty();
        }

        try {
            Map<String, String> handles = new HashMap<>();
            String[] pairs = socialMediaString.split(";");

            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().toLowerCase();
                    String value = keyValue[1].trim();
                    if (!value.isBlank()) {
                        handles.put(key, value);
                    }
                }
            }

            return fromMap(handles);
        } catch (Exception e) {
            throw new CustomerDomainException("Invalid social media string format: " + socialMediaString);
        }
    }

    // Validation methods
    private static void validateTwitterHandle(String handle) {
        if (handle != null && !handle.isBlank() && !TWITTER_PATTERN.matcher(handle).matches()) {
            throw new CustomerDomainException("Invalid Twitter deleteServicePackage: " + handle);
        }
    }

    private static void validateInstagramHandle(String handle) {
        if (handle != null && !handle.isBlank() && !INSTAGRAM_PATTERN.matcher(handle).matches()) {
            throw new CustomerDomainException("Invalid Instagram deleteServicePackage: " + handle);
        }
    }

    private static void validateFacebookUrl(String url) {
        if (url != null && !url.isBlank()) {
            // Validar que sea una URL o un nombre de usuario válido
            if (!url.startsWith("https://www.facebook.com/") && !FACEBOOK_PATTERN.matcher(url).matches()) {
                throw new CustomerDomainException("Invalid Facebook URL or username: " + url);
            }
        }
    }

    private static void validateLinkedinUrl(String url) {
        if (url != null && !url.isBlank()) {
            // Validar que sea una URL de LinkedIn o un nombre de empresa
            if (!url.startsWith("https://www.linkedin.com/company/") &&
                    !url.startsWith("https://www.linkedin.com/in/") &&
                    !LINKEDIN_PATTERN.matcher(url).matches()) {
                throw new CustomerDomainException("Invalid LinkedIn URL: " + url);
            }
        }
    }

    private static void validateYoutubeHandle(String handle) {
        if (handle != null && !handle.isBlank() && !YOUTUBE_PATTERN.matcher(handle).matches()) {
            throw new CustomerDomainException("Invalid YouTube deleteServicePackage: " + handle);
        }
    }

    private static void validateTiktokHandle(String handle) {
        if (handle != null && !handle.isBlank() && !TIKTOK_PATTERN.matcher(handle).matches()) {
            throw new CustomerDomainException("Invalid TikTok deleteServicePackage: " + handle);
        }
    }

    // Normalization methods
    private static String normalizeTwitterHandle(String handle) {
        if (handle == null || handle.isBlank()) return null;
        String normalized = handle.trim();
        return normalized.startsWith("@") ? normalized.substring(1) : normalized;
    }

    private static String normalizeInstagramHandle(String handle) {
        if (handle == null || handle.isBlank()) return null;
        String normalized = handle.trim();
        return normalized.startsWith("@") ? normalized.substring(1) : normalized;
    }

    private static String normalizeFacebookUrl(String url) {
        if (url == null || url.isBlank()) return null;
        String normalized = url.trim();
        if (!normalized.startsWith("http")) {
            normalized = "https://www.facebook.com/" + normalized;
        }
        return normalized;
    }

    private static String normalizeLinkedinUrl(String url) {
        if (url == null || url.isBlank()) return null;
        String normalized = url.trim();
        if (!normalized.startsWith("http")) {
            // Asumir que es un nombre de empresa
            normalized = "https://www.linkedin.com/company/" + normalized;
        }
        return normalized;
    }

    private static String normalizeYoutubeHandle(String handle) {
        if (handle == null || handle.isBlank()) return null;
        String normalized = handle.trim();
        return normalized.startsWith("@") ? normalized : "@" + normalized;
    }

    private static String normalizeTiktokHandle(String handle) {
        if (handle == null || handle.isBlank()) return null;
        String normalized = handle.trim();
        return normalized.startsWith("@") ? normalized : "@" + normalized;
    }

    // Getters with null-safety
    public Optional<String> getTwitterHandle() {
        return Optional.ofNullable(twitterHandle);
    }

    public Optional<String> getInstagramHandle() {
        return Optional.ofNullable(instagramHandle);
    }

    public Optional<String> getFacebookUrl() {
        return Optional.ofNullable(facebookUrl);
    }

    public Optional<String> getLinkedinUrl() {
        return Optional.ofNullable(linkedinUrl);
    }

    public Optional<String> getYoutubeHandle() {
        return Optional.ofNullable(youtubeHandle);
    }

    public Optional<String> getTiktokHandle() {
        return Optional.ofNullable(tiktokHandle);
    }

    // Business methods
    public boolean hasHandles() {
        return twitterHandle != null || instagramHandle != null ||
                facebookUrl != null || linkedinUrl != null ||
                youtubeHandle != null || tiktokHandle != null ||
                !otherPlatforms.isEmpty();
    }

    public boolean hasTwitter() {
        return twitterHandle != null;
    }

    public boolean hasInstagram() {
        return instagramHandle != null;
    }

    public boolean hasFacebook() {
        return facebookUrl != null;
    }

    public boolean hasLinkedin() {
        return linkedinUrl != null;
    }

    public boolean hasYoutube() {
        return youtubeHandle != null;
    }

    public boolean hasTiktok() {
        return tiktokHandle != null;
    }

    public int getHandleCount() {
        int count = 0;
        if (twitterHandle != null) count++;
        if (instagramHandle != null) count++;
        if (facebookUrl != null) count++;
        if (linkedinUrl != null) count++;
        if (youtubeHandle != null) count++;
        if (tiktokHandle != null) count++;
        count += otherPlatforms.size();
        return count;
    }

    public SocialMediaHandles addTwitterHandle(String handle) {
        validateTwitterHandle(handle);
        return new SocialMediaHandles(
                normalizeTwitterHandle(handle),
                instagramHandle,
                facebookUrl,
                linkedinUrl,
                youtubeHandle,
                tiktokHandle,
                otherPlatforms
        );
    }

    public SocialMediaHandles addInstagramHandle(String handle) {
        validateInstagramHandle(handle);
        return new SocialMediaHandles(
                twitterHandle,
                normalizeInstagramHandle(handle),
                facebookUrl,
                linkedinUrl,
                youtubeHandle,
                tiktokHandle,
                otherPlatforms
        );
    }

    public SocialMediaHandles addFacebookUrl(String url) {
        validateFacebookUrl(url);
        return new SocialMediaHandles(
                twitterHandle,
                instagramHandle,
                normalizeFacebookUrl(url),
                linkedinUrl,
                youtubeHandle,
                tiktokHandle,
                otherPlatforms
        );
    }

    public SocialMediaHandles addLinkedinUrl(String url) {
        validateLinkedinUrl(url);
        return new SocialMediaHandles(
                twitterHandle,
                instagramHandle,
                facebookUrl,
                normalizeLinkedinUrl(url),
                youtubeHandle,
                tiktokHandle,
                otherPlatforms
        );
    }

    public SocialMediaHandles addOtherPlatform(String platform, String handle) {
        if (platform == null || platform.isBlank() || handle == null || handle.isBlank()) {
            return this;
        }

        String normalizedPlatform = platform.toLowerCase().trim();
        String normalizedHandle = handle.trim();

        Map<String, String> newOtherPlatforms = new HashMap<>(otherPlatforms);
        newOtherPlatforms.put(normalizedPlatform, normalizedHandle);

        return new SocialMediaHandles(
                twitterHandle,
                instagramHandle,
                facebookUrl,
                linkedinUrl,
                youtubeHandle,
                tiktokHandle,
                Collections.unmodifiableMap(newOtherPlatforms)
        );
    }

    public SocialMediaHandles removePlatform(String platform) {
        if (platform == null || platform.isBlank()) {
            return this;
        }

        String normalizedPlatform = platform.toLowerCase().trim();

        // Verificar si es una plataforma principal
        switch (normalizedPlatform) {
            case "twitter":
                return new SocialMediaHandles(
                        null, instagramHandle, facebookUrl, linkedinUrl, youtubeHandle, tiktokHandle, otherPlatforms
                );
            case "instagram":
                return new SocialMediaHandles(
                        twitterHandle, null, facebookUrl, linkedinUrl, youtubeHandle, tiktokHandle, otherPlatforms
                );
            case "facebook":
                return new SocialMediaHandles(
                        twitterHandle, instagramHandle, null, linkedinUrl, youtubeHandle, tiktokHandle, otherPlatforms
                );
            case "linkedin":
                return new SocialMediaHandles(
                        twitterHandle, instagramHandle, facebookUrl, null, youtubeHandle, tiktokHandle, otherPlatforms
                );
            case "youtube":
                return new SocialMediaHandles(
                        twitterHandle, instagramHandle, facebookUrl, linkedinUrl, null, tiktokHandle, otherPlatforms
                );
            case "tiktok":
                return new SocialMediaHandles(
                        twitterHandle, instagramHandle, facebookUrl, linkedinUrl, youtubeHandle, null, otherPlatforms
                );
            default:
                if (!otherPlatforms.containsKey(normalizedPlatform)) {
                    return this;
                }
                Map<String, String> newOtherPlatforms = new HashMap<>(otherPlatforms);
                newOtherPlatforms.remove(normalizedPlatform);
                return new SocialMediaHandles(
                        twitterHandle, instagramHandle, facebookUrl, linkedinUrl,
                        youtubeHandle, tiktokHandle, Collections.unmodifiableMap(newOtherPlatforms)
                );
        }
    }

    public String getTwitterUrl() {
        return twitterHandle != null ? "https://twitter.com/" + twitterHandle : null;
    }

    public String getInstagramUrl() {
        return instagramHandle != null ? "https://instagram.com/" + instagramHandle : null;
    }

    public String getYoutubeUrl() {
        return youtubeHandle != null ? "https://youtube.com/" + youtubeHandle : null;
    }

    public String getTiktokUrl() {
        return tiktokHandle != null ? "https://tiktok.com/@" + tiktokHandle.substring(1) : null;
    }

    public Map<String, String> getAllHandles() {
        Map<String, String> allHandles = new LinkedHashMap<>();

        if (twitterHandle != null) {
            allHandles.put("twitter", twitterHandle);
        }
        if (instagramHandle != null) {
            allHandles.put("instagram", instagramHandle);
        }
        if (facebookUrl != null) {
            allHandles.put("facebook", facebookUrl);
        }
        if (linkedinUrl != null) {
            allHandles.put("linkedin", linkedinUrl);
        }
        if (youtubeHandle != null) {
            allHandles.put("youtube", youtubeHandle);
        }
        if (tiktokHandle != null) {
            allHandles.put("tiktok", tiktokHandle);
        }

        allHandles.putAll(otherPlatforms);
        return Collections.unmodifiableMap(allHandles);
    }

    public Map<String, String> getAllUrls() {
        Map<String, String> urls = new LinkedHashMap<>();

        getTwitterHandle().ifPresent(handle ->
                urls.put("twitter", "https://twitter.com/" + handle));

        getInstagramHandle().ifPresent(handle ->
                urls.put("instagram", "https://instagram.com/" + handle));

        getFacebookUrl().ifPresent(url ->
                urls.put("facebook", url));

        getLinkedinUrl().ifPresent(url ->
                urls.put("linkedin", url));

        getYoutubeHandle().ifPresent(handle ->
                urls.put("youtube", "https://youtube.com/" + handle));

        getTiktokHandle().ifPresent(handle ->
                urls.put("tiktok", "https://tiktok.com/@" + handle.substring(1)));

        return Collections.unmodifiableMap(urls);
    }

    public String toString() {
        List<String> parts = new ArrayList<>();

        if (twitterHandle != null) {
            parts.add("twitter=@" + twitterHandle);
        }
        if (instagramHandle != null) {
            parts.add("instagram=@" + instagramHandle);
        }
        if (facebookUrl != null) {
            parts.add("facebook=" + facebookUrl);
        }
        if (linkedinUrl != null) {
            parts.add("linkedin=" + linkedinUrl);
        }
        if (youtubeHandle != null) {
            parts.add("youtube=" + youtubeHandle);
        }
        if (tiktokHandle != null) {
            parts.add("tiktok=" + tiktokHandle);
        }

        otherPlatforms.forEach((platform, handle) ->
                parts.add(platform + "=" + handle));

        return String.join(";", parts);
    }

    public String toJsonString() {
        Map<String, String> jsonMap = new LinkedHashMap<>();

        getTwitterHandle().ifPresent(handle -> jsonMap.put("twitter", handle));
        getInstagramHandle().ifPresent(handle -> jsonMap.put("instagram", handle));
        getFacebookUrl().ifPresent(url -> jsonMap.put("facebook", url));
        getLinkedinUrl().ifPresent(url -> jsonMap.put("linkedin", url));
        getYoutubeHandle().ifPresent(handle -> jsonMap.put("youtube", handle));
        getTiktokHandle().ifPresent(handle -> jsonMap.put("tiktok", handle));
        jsonMap.putAll(otherPlatforms);

        return "{" + jsonMap.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(",")) + "}";
    }

}