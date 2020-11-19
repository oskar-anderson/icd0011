package test;

import conf.security.TokenInfo;
import conf.security.jwt.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class JwtHelperTest {

    private JwtHelper jwt = new JwtHelper(
            "****************** random key ******************" +
            "kr6m4GNX6voKiPh3pfCaWkQoG8d1E756i6m4GNX6voKiP2hp");

    @SuppressWarnings("PMD")
    @Test
    public void canEncodeAndDecode() {

        var tokenInfo = new TokenInfo("user", List.of("user", "admin"));

        String tokenAsString = jwt.encode(tokenInfo);

        var decoded = jwt.decode(tokenAsString);

        assertThat(decoded.getUserName(), is(tokenInfo.getUserName()));
        assertThat(decoded.getRoles(), is(tokenInfo.getRoles()));
    }

    @Test(expected = ExpiredJwtException.class)
    public void failsOnExpiredToken() {

        String tokenAsString = jwt.encode(
                new TokenInfo("user", ""), LocalDateTime.now().minusMinutes(1));

        jwt.decode(tokenAsString);
    }

    @Test(expected = SignatureException.class)
    public void canNotTamperData() {

        String tokenAsString = jwt.encode(new TokenInfo("user", ""));

        tokenAsString = tokenAsString.replaceFirst("\\.[^0]", ".0");

        jwt.decode(tokenAsString);
    }
}
