package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("customer")
public class Customer extends User {

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Balance balance = new Balance(this);

    @OneToMany(mappedBy="customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "cart_order")
    private List<CartItem> cart = new ArrayList<>();

    @OneToMany(mappedBy="customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "purchase_order")
    private List<PurchaseRecord> purchaseHistory = new ArrayList<>();

    public Customer() {
        super();
    }

    public Customer(String username, String password, String email, Address address) {
        super(username, password, email, address);
    }

    public int getBalance() { return balance.getAmount(); }
    public List<CartItem> getCart() { return cart; }
    public List<PurchaseRecord> getPurchaseHistory() { return purchaseHistory; }

    public void increaseAmount(int amount) {
        balance.increaseAmount(amount);
    }

    public void addCart(Book book) {
        cart.add(new CartItem(this, book, false, 0));
    }

    public void borrowBook(Book book, int days) {
        cart.add(new CartItem(this, book, true, days));
    }

    public boolean removeBookFromCart(Book book) {
        return cart.removeIf(c -> c.getBook().equals(book));
    }

    public boolean hasBookInCart(Book book) {
        return cart.stream().anyMatch(c -> c.getBook().equals(book));
    }

    public boolean hasEnoughCreditForCart() {
        return balance.getAmount() >= calculateCartCost();
    }

    public PurchaseReceipt purchaseCart() {
        int cartCost = calculateCartCost();
        PurchaseReceipt purchaseReceipt = new PurchaseReceipt(cart.size(), cartCost, LocalDateTime.now());
        PurchaseRecord record = new PurchaseRecord(this, LocalDateTime.now(), cartCost);
        for (CartItem cartItem : cart) {
            record.addItem(new PurchaseItem(record, cartItem));
        }
        purchaseHistory.add(record);
        balance.decreaseAmount(cartCost);
        cart.clear();

        return purchaseReceipt;
    }

    public boolean isBookPurchased(Book book) {
        for (PurchaseRecord r : purchaseHistory) {
            for (PurchaseItem p : r.getItems()) {
                if (p.getBook().equals(book)) {
                    if (!p.isBorrowed())
                        return true;
                    else if (r.getPurchaseDate().plusDays(p.getBorrowDays()).isAfter(LocalDateTime.now()))
                        return true;
                }
            }
        }
        return false;
    }

    public List<PurchaseItem> getPurchasedBooks() {
        List<PurchaseItem> purchasedBooks = new ArrayList<>();
        for (PurchaseRecord r : purchaseHistory) {
            for (PurchaseItem p : r.getItems()) {
                if (!p.isBorrowed())
                    purchasedBooks.add(p);
                else if (r.getPurchaseDate().plusDays(p.getBorrowDays()).isAfter(LocalDateTime.now()))
                    purchasedBooks.add(p);
            }
        }
        return purchasedBooks;
    }

    public int calculateCartCost() {
        int totalCost = 0;
        for (CartItem cartItem : cart) {
            totalCost += cartItem.getFinalPrice();
        }
        return totalCost;
    }
}
