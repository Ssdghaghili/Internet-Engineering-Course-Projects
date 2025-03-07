package org.example.model.serializer;
import org.example.model.User;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {
    @Override
    public void serialize(User user, JsonGenerator jsonGen, SerializerProvider serializers) throws IOException {
        jsonGen.writeStartObject();

        jsonGen.writeStringField("username", user.getUsername());
        jsonGen.writeStringField("role", user.getRole().name());
        jsonGen.writeStringField("email", user.getEmail());
        jsonGen.writeObjectFieldStart("address");
        jsonGen.writeStringField("country", user.getAddress().getCountry());
        jsonGen.writeStringField("city", user.getAddress().getCity());
        jsonGen.writeEndObject();
        if (user.getRole() == User.Role.customer)
            jsonGen.writeNumberField("balance", user.getBalance());

        jsonGen.writeEndObject();
    }
}
