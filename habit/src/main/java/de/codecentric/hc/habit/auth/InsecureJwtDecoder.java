package de.codecentric.hc.habit.auth;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.*;

import java.text.ParseException;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An implementation of a JwtDecoder that decodes a JSON Web Token (JWT) without verifying its digital signature.
 */
public class InsecureJwtDecoder implements JwtDecoder {
    private static final String DECODING_ERROR_MESSAGE_TEMPLATE =
            "An error occurred while attempting to decode the Jwt: %s";

    private Converter<Map<String, Object>, Map<String, Object>> claimSetConverter =
            MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    @Override
    public Jwt decode(String token) throws JwtException {
        JWT jwt = parse(token);
        return convertToSpringJwt(token, jwt);
    }

    private JWT parse(String token) {
        try {
            return JWTParser.parse(token);
        } catch (Exception ex) {
            throw new JwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
        }
    }

    private Jwt convertToSpringJwt(String token, JWT parsedJwt) {
        Jwt springJwt;

        try {
            Map<String, Object> headers = getHeadersOf(parsedJwt);
            Map<String, Object> claims = getClaimsOf(parsedJwt);

            Instant expiresAt = (Instant) claims.get(JwtClaimNames.EXP);
            Instant issuedAt = (Instant) claims.get(JwtClaimNames.IAT);
            springJwt = new Jwt(token, issuedAt, expiresAt, headers, claims);
        } catch (Exception e) {
            throw new JwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, e.getMessage()), e);
        }

        return springJwt;
    }

    private Map<String, Object> getClaimsOf(JWT parsedJwt) throws ParseException {
        return claimSetConverter.convert(parsedJwt.getJWTClaimsSet().getClaims());
    }

    private Map<String, Object> getHeadersOf(JWT parsedJwt) {
        return new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
    }
}
