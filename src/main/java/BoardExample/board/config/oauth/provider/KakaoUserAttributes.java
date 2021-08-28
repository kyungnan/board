package BoardExample.board.config.oauth.provider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserAttributes implements OAuthUserAttributes{
    private Map<String, Object> kakao_account;
    private Map<String, Object> properties;
    private int id;

    public KakaoUserAttributes(Map<String, Object> attributes) {
        this.kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.id = (int) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(id);
    }

    @Override
    public String getName() {
        return (String) properties.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) kakao_account.get("email");
    }
}
