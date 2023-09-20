package top.whiskeyxd.bloglitecommon.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTPayload {

    private String username;

    private String role;
}
