package br.com.pagamento.account.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomAvailableLimitDeserialize extends StdDeserializer<Double> {

	private static final long serialVersionUID = 782461302877003515L;

    public CustomAvailableLimitDeserialize() { 
        this(null); 
    } 
 
    public CustomAvailableLimitDeserialize(Class<?> vc) { 
        super(vc); 
    }

    @Override
	public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
       return new ObjectMapper().readValue(p, AvailableLimit.class).getAmount();
	}    
}