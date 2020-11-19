package conf.security;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor
public class TokenInfo {

    private String userName;
    private List<String> roles;

    public TokenInfo(String userName, String roles) {
        this.userName = userName;
        this.roles = List.of(roles.split(", "));
    }

    public String getRolesAsString() {
        return roles.stream().collect(Collectors.joining(", "));
    }

}
