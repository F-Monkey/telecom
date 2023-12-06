package cn.telecom.commons.data;

import lombok.Getter;

import java.util.*;

@Getter
public class JwtCreateOptions {

    private String id;

    private String subject;

    private Date issuedAt;

    private Date expiration;

    private String secret;

    private Map<String, Object> claims;


    private String content;

    public static Builder builder() {
        return new Builder();
    }

    private JwtCreateOptions() {
    }

    public static class Builder {
        private final JwtCreateOptions options;

        private Builder() {
            options = new JwtCreateOptions();
        }

        public Builder id(String id) {
            this.options.id = id;
            return this;
        }

        public Builder subject(String subject) {
            this.options.subject = subject;
            return this;
        }

        public Builder issuedAt(Date issuedAt) {
            this.options.issuedAt = issuedAt;
            return this;
        }

        public Builder expiration(Date expiration) {
            this.options.expiration = expiration;
            return this;
        }

        public Builder secret(String secret) {
            this.options.secret = secret;
            return this;
        }

        public Builder claims(Map<String, Object> claims) {
            if (this.options.claims == null) {
                this.options.claims = new HashMap<>();
            }
            this.options.claims.putAll(claims);
            return this;
        }

        public Builder addClaim(KVPair<String, Object> e) {
            if (this.options.claims == null) {
                this.options.claims = new HashMap<>();
            }
            String k = e.getK();
            if (this.options.claims.containsKey(k)) {
                throw new IllegalArgumentException("claim k: " + k + " is already exists");
            }
            this.options.claims.put(k, e.getV());
            return this;
        }

        public Builder content(String content) {
            this.options.content = content;
            return this;
        }

        public JwtCreateOptions build() {
            return this.options;
        }
    }
}
