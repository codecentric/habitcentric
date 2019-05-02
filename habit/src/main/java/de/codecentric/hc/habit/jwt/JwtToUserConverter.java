package de.codecentric.hc.habit.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

/**
 * An implementation of a Spring converter that can be used to obtain encoded user information of a JSON Web Token
 * (JWT) inside an HTTP Authorization header of type Bearer.
 */
@Component
public class JwtToUserConverter implements Converter<String, User> {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public User convert(String authHeader) {
        String token = extractTokenFrom(authHeader);

        if (token == null) {
            return null;
        }

        Jwt decodedJwt = jwtDecoder.decode(token);
        String userId = getSubjectOf(decodedJwt);
        return new User(userId);
    }

    private String extractTokenFrom(String authHeader) throws ConversionException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7);
    }

    private String getSubjectOf(Jwt decodedJwt) {
        return decodedJwt.getClaimAsString("sub");
    }
}
