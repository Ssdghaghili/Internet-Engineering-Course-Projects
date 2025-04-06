package org.example.model.serializer;
import org.example.model.Review;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ReviewSerializer extends JsonSerializer<Review> {
    @Override
    public void serialize(Review review, JsonGenerator jsonGen, SerializerProvider serializers) throws IOException {
        jsonGen.writeStartObject();

        jsonGen.writeStringField("username", review.getUser().getUsername());
        jsonGen.writeNumberField("rate", review.getRate());
        jsonGen.writeStringField("comment", review.getComment());
        jsonGen.writeStringField("date", review.getDateTime().toString());

        jsonGen.writeEndObject();
    }
}