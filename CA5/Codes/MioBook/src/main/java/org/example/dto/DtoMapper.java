package org.example.dto;

import org.example.model.*;

import java.util.stream.Collectors;

public class DtoMapper {
    public static ReviewDTO reviewToDTO(Review review) {
        return new ReviewDTO(
                review.getCustomer().getUsername(),
                review.getRate(),
                review.getComment(),
                review.getDateTime()
        );
    }

    public static BookDTO bookToDTO(Book book) {
        return new BookDTO(
                book.getTitle(),
                book.getAuthor().getName(),
                book.getPublisher(),
                book.getGenres().stream().map(Genre::getName).collect(Collectors.toList()),
                book.getYear(),
                book.getPrice(),
                book.getSynopsis(),
                book.getAverageRate(),
                book.getTotalBuys(),
                book.getImageLink()
        );
    }

    public static AuthorDTO authorToDTO(Author author) {
        return new AuthorDTO(
                author.getName(),
                author.getPenName(),
                author.getNationality(),
                author.getBorn(),
                author.getDied(),
                author.getImageLink()
        );
    }

    public static UserDTO userToDTO(User user) {
        if (user instanceof Customer customer) {
            return new CustomerDTO(
                    customer.getUsername(),
                    customer.getEmail(),
                    customer.getAddress().getCountry(),
                    customer.getAddress().getCity(),
                    customer.getRole(),
                    customer.getBalance()
            );
        }
        else {
            return new AdminDTO(
                    user.getUsername(),
                    user.getEmail(),
                    user.getAddress().getCountry(),
                    user.getAddress().getCity(),
                    user.getRole()
            );
        }
    }

    public static CartItemDTO cartItemToDTO(CartItem cartItem) {
        Book book = cartItem.getBook();
        return new CartItemDTO(
                book.getTitle(),
                book.getAuthor().getName(),
                book.getPublisher(),
                book.getGenres().stream().map(Genre::getName).collect(Collectors.toList()),
                book.getYear(),
                book.getPrice(),
                cartItem.isBorrowed(),
                cartItem.getFinalPrice(),
                cartItem.getBorrowDays()
        );
    }

    public static CartDTO CartToDTO(Cart cart) {
        return new CartDTO(
                cart.getTotalCost(),
                cart.getItems().stream().map(DtoMapper::cartItemToDTO).collect(Collectors.toList())
        );
    }
}
