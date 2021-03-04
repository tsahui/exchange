package com.exchange.restclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


public class BaseRestClientImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRestClientImpl.class);

    @Autowired
    private RestTemplate restTemplate;


    public <T, V> V callClient(String url, HttpMethod httpMethod, T request, Class<V> responseType, Object... uriVariables) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
            HttpEntity<T> headerAndRequest = new HttpEntity<T>(request, headers);

            LOGGER.debug("Send Request with url: [{}] Request [{}]", url, request);
            ResponseEntity<V> responseEntity = restTemplate.exchange(url, httpMethod, headerAndRequest, responseType, uriVariables);
            LOGGER.debug("Returned Response for url: [{}], Response [{}]", url, responseEntity.getBody());
            return responseEntity.getBody();

        } catch (RestClientException rce) {
            this.handleRestServerErrors(url, rce);
        }

        return null;
    }

    private void handleRestServerErrors(String url, RestClientException rce) {
        LOGGER.error("Request to rest-server at url: [{}] failed.", url, rce);

        // split between different kinds of error and return fitting msg
        throw rce;
    }


}
