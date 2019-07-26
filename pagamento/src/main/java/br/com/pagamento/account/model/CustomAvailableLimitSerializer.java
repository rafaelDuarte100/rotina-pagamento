package br.com.pagamento.account.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * CustomAvailableLimitSerializer
 */
public class CustomAvailableLimitSerializer extends StdSerializer<Double> {

	private static final long serialVersionUID = 7289816460621166003L;

    public CustomAvailableLimitSerializer() { 
        this(null); 
    } 
 
    public CustomAvailableLimitSerializer(Class<Double> t) {
        super(t); 
    }

    @Override
	public void serialize(Double value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(new AvailableLimit(value));
	}    
}