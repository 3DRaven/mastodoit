package com.anyqn.mastodon.common.models.twitter;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.values.twitter.TwitterConsumerKey;
import com.anyqn.mastodon.common.models.values.twitter.TwitterConsumerSecret;
import com.anyqn.mastodon.common.util.HeaderBuilder;
import com.anyqn.mastodon.common.util.ParametersBuilder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Value
@Slf4j
public class TwitterRequestData {
    TwitterConsumerKey oauthTwitterConsumerKey;
    TwitterConsumerSecret oauthTwitterConsumerSecret;

    TwitterCallDescriptor twitterCallDescriptor;//For signed header generation
    String oauthNonce = Base64.getEncoder()
            .encodeToString(
                    UUID.randomUUID()
                            .toString()
                            .getBytes(StandardCharsets.UTF_8))
            .chars()
            .filter(Character::isLetterOrDigit)
            .mapToObj(Character::toString)
            .limit(Constants.TWITTER_NONCE_SIZE)
            .collect(Collectors.joining())
            .toLowerCase();
    String oauthSignatureMethod = Constants.TWITTER_SIGNATURE_ALGORITHM;
    Instant oauthTimestamp = Instant.now();
    String oauthVersion = Constants.TWITTER_OAUTH_VERSION;

    public String getAuthorizationHeader() {
        ParametersBuilder parametersCoder = new ParametersBuilder();

        twitterCallDescriptor.getCallParameters().forEach(parametersCoder::appendKeyValue);

        parametersCoder.appendKeyValue(Constants.TWITTER_QUERY_OAUTH_CONSUMER_KEY, oauthTwitterConsumerKey.getValue())
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_NONCE, oauthNonce)
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_SIGNATURE_METHOD, oauthSignatureMethod)
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_TIMESTAMP, String.valueOf(oauthTimestamp.getEpochSecond()));

        Optional.ofNullable(twitterCallDescriptor.getAccessToken()).ifPresent(it -> parametersCoder
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_TOKEN, it.getValue()));

        parametersCoder.appendKeyValue(Constants.TWITTER_QUERY_OAUTH_VERSION, oauthVersion);

        String encodedData =
                twitterCallDescriptor.getCallMethod().name().toUpperCase() + "&" + URLEncoder.encode(twitterCallDescriptor.getCalledUrl().toString(),
                        StandardCharsets.UTF_8) + "&" + URLEncoder.encode(parametersCoder.build(), StandardCharsets.UTF_8);

        log.trace("Encoded data: {}", encodedData);

        String signingKey = Optional.ofNullable(twitterCallDescriptor.getAccessTokenSecret()).map(it ->
                        oauthTwitterConsumerSecret.encode() + "&" +
                                it.encode())
                .orElse(oauthTwitterConsumerSecret.encode() + "&");

        log.trace("Signing key: {}", signingKey);

        String signature = Base64.getEncoder().encodeToString(new HmacUtils(HmacAlgorithms.HMAC_SHA_1, signingKey).hmac(encodedData));

        log.trace("Signature: {}", signature);

        Optional.ofNullable(twitterCallDescriptor.getAccessToken())
                .ifPresent(it -> parametersCoder.appendKeyValue(Constants.TWITTER_QUERY_OAUTH_TOKEN,
                        it.toString()));

        HeaderBuilder headerBuilder = new HeaderBuilder();

        headerBuilder
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_CONSUMER_KEY, oauthTwitterConsumerKey.getValue())
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_NONCE, oauthNonce)
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_SIGNATURE, signature)
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_SIGNATURE_METHOD, Constants.TWITTER_SIGNATURE_ALGORITHM)
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_TIMESTAMP, String.valueOf(oauthTimestamp.getEpochSecond()));

        Optional.ofNullable(twitterCallDescriptor.getAccessToken()).ifPresent(it -> headerBuilder
                .appendKeyValue(Constants.TWITTER_QUERY_OAUTH_TOKEN, it.getValue()));

        headerBuilder.appendKeyValue(Constants.TWITTER_QUERY_OAUTH_VERSION, Constants.TWITTER_OAUTH_VERSION);

        String header = headerBuilder.build();

        log.trace("Header: {}", header);

        return header;
    }

    @Override
    public String toString() {
        return "TwitterRequestData{" +
                "twitterCallData" + twitterCallDescriptor +
                "oauthConsumerKey='" + oauthTwitterConsumerKey + '\'' +
                ", oauthConsumerSecret='***'" +
                ", oauthNonce='" + oauthNonce + '\'' +
                ", oauthSignatureMethod='" + oauthSignatureMethod + '\'' +
                ", oauthTimestamp=" + oauthTimestamp +
                ", oauthVersion='" + oauthVersion + '\'' +
                ", twitterAccessToken=" + twitterCallDescriptor +
                '}';
    }
}
