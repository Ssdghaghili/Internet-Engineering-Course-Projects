package org.example.model.serializer;
import org.example.model.Book;
import org.example.model.CartItem;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CartItemSerializer extends JsonSerializer<CartItem> {
    @Override
    public void serialize(CartItem cartItem, JsonGenerator jsonGen, SerializerProvider serializers) throws IOException {
        Book book = cartItem.getBook();

        jsonGen.writeStartObject();

        jsonGen.writeStringField("title", book.getTitle());
        jsonGen.writeStringField("author", book.getAuthor());
        jsonGen.writeStringField("publisher", book.getPublisher());

        jsonGen.writeArrayFieldStart("genres");
        for (String genre : book.getGenres()) {
            jsonGen.writeString(genre);
        }
        jsonGen.writeEndArray();

        jsonGen.writeNumberField("year", book.getYear());
        jsonGen.writeNumberField("price", book.getPrice());

        jsonGen.writeBooleanField("isBorrowed", cartItem.isBorrowed());
        jsonGen.writeNumberField("finalPrice", cartItem.getFinalPrice());
        if (cartItem.isBorrowed())
            jsonGen.writeNumberField("borrowDays", cartItem.getBorrowDays());

        jsonGen.writeEndObject();
    }
}
