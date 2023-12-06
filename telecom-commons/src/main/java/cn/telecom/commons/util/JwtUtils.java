package cn.telecom.commons.util;

import cn.telecom.commons.data.JwtCreateOptions;
import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Optional;
import java.util.function.Function;


public abstract class JwtUtils {

    private JwtUtils() {
    }

    private static <T> JwtBuilder trySet(JwtBuilder builder, T o, Function<T, JwtBuilder> function) {
        if (o == null || (o instanceof String s && Strings.isNullOrEmpty(s))) {
            return builder;
        }
        return function.apply(o);
    }

    public static String encrypt(JwtCreateOptions options) {
        return Optional.of(Jwts.builder())
                .map(builder -> trySet(builder, options.getId(), builder::id))
                .map(builder -> trySet(builder, options.getSecret(), input -> builder.signWith(Keys.hmacShaKeyFor(input.getBytes()))))
                .map(builder -> trySet(builder, options.getExpiration(), builder::expiration))
                .map(builder -> trySet(builder, options.getIssuedAt(), builder::issuedAt))
                .map(builder -> trySet(builder, options.getContent(), builder::content))
                .map(builder -> trySet(builder, options.getSubject(), builder::subject))
                .map(builder -> trySet(builder, options.getClaims(), builder::claims))
                .map(JwtBuilder::compact)
                .orElseThrow(() -> new IllegalArgumentException("never happened"));

    }

    public static Claims decrypt(String s) {
        return decrypt(s, null);
    }

    public static Claims decrypt(String s, String key) {
        JwtParserBuilder builder = Jwts.parser();
        if (!Strings.isNullOrEmpty(key)) {
            builder.decryptWith(Keys.hmacShaKeyFor(key.getBytes()));
        } else {
            builder.unsecured();
        }
        JwtParser parser = builder.build();
        Jwt<?, ?> parse = parser.parse(s);
        return (Claims) parse.getPayload();
    }

    public static long getExp(Claims claims) {
        Object o = claims.get("exp");
        if (o == null) {
            throw new NullPointerException();
        }
        return ((long) o) * 1000;
    }

    public static long getIat(Claims claims) {
        Object o = claims.get("iat");
        if (o == null) {
            throw new NullPointerException();
        }
        return ((long) o) * 1000;
    }

}
