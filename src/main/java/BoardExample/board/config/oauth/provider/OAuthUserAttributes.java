package BoardExample.board.config.oauth.provider;

public interface OAuthUserAttributes {

    String getProvider();
    String getProviderId();
    String getName();
    String getEmail();
}
