package org.example.model.serializer;
import org.example.model.Book;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.example.model.Genre;

import java.io.IOException;

public class BookSerializer extends JsonSerializer<Book> {
    @Override
    public void serialize(Book book, JsonGenerator jsonGen, SerializerProvider serializers) throws IOException {
        jsonGen.writeStartObject();

        jsonGen.writeStringField("title", book.getTitle());
        jsonGen.writeStringField("author", book.getAuthor().getName());
        jsonGen.writeStringField("publisher", book.getPublisher());

        jsonGen.writeArrayFieldStart("genres");
        for (Genre genre : book.getGenres()) {
            jsonGen.writeString(genre.getName());
        }
        jsonGen.writeEndArray();

        jsonGen.writeNumberField("year", book.getYear());
        jsonGen.writeNumberField("price", book.getPrice());
        jsonGen.writeStringField("synopsis", book.getSynopsis());
        jsonGen.writeNumberField("averageRating", book.getAverageRate());
        jsonGen.writeNumberField("totalBuys", book.getTotalBuys());
        jsonGen.writeStringField("imageLink", book.getImageLink());

        jsonGen.writeEndObject();
    }
}