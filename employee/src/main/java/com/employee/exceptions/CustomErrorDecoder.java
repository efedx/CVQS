package com.employee.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;

@Component
@RequiredArgsConstructor
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    private final ObjectMapper objectMapper;
    @Override
    public Exception decode(String methodKey, Response response) {

//        try (InputStream bodyIs = response.body().asInputStream()) {
//            ObjectMapper mapper = new ObjectMapper();
//            securityExceptionResponse = mapper.readValue(bodyIs, SecurityExceptionResponse.class);
//        } catch (IOException e) {
//            return new Exception(e.getMessage());
//        }

        Reader reader = null;

        try {
            reader = response.body().asReader();
            //Easy way to read the stream and get a String object
            String result = CharStreams.toString(reader);
            //use a Jackson ObjectMapper to convert the Json String into a
            //Pojo
            ObjectMapper mapper = new ObjectMapper();
            //just in case you missed an attribute in the Pojo
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            //init the Pojo
            SecurityExceptionResponse securityExceptionResponse = mapper.readValue(result,
                    SecurityExceptionResponse.class);

            String message = securityExceptionResponse.message();
            Integer status = response.status();

        switch (status) {
            case 400:
                throw new CustomSecurityException(message != null ? message : "Bad Request");
            case 404:
                return new NotFoundException(message != null ? message : "Not found");
            case 503:
                return new ServiceUnavailableException();
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
    catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
