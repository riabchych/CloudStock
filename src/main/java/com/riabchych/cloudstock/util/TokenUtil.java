package com.riabchych.cloudstock.util;

import com.riabchych.cloudstock.entity.Token;
import com.riabchych.cloudstock.entity.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface TokenUtil {

    Token getJWTToken(String username, String[] roles);

    String getUsernameFromToken(String token);

    String getAudienceFromToken(String token);

    Boolean canTokenBeRefreshed(String token, Date lastPasswordReset);

    String refreshToken(String token);

    Boolean validateToken(String token, User user);
}
