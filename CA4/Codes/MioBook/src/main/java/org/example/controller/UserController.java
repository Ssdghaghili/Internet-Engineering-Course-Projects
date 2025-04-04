package org.example.controller;

import org.example.exception.BadRequestException;
import org.example.exception.ForbiddenException;
import org.example.exception.NotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.model.CartItem;
import org.example.model.PurchaseReceipt;
import org.example.model.PurchaseRecord;
import org.example.response.Response;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/addCart")
    public Response<Object> addCart(@RequestParam String title)
            throws ForbiddenException, UnauthorizedException, NotFoundException, BadRequestException {
        userService.addCart(title);
        return Response.ok("Added book to cart");
    }

    @PostMapping("/removeCart")
    public Response<Object> removeCart(@RequestParam String title)
            throws ForbiddenException, UnauthorizedException, NotFoundException, BadRequestException {
        userService.removeCart(title);
        return Response.ok("Removed book from cart");
    }

    @PostMapping("/addCredit")
    public Response<Object> addCredit(@RequestParam int amount)
            throws ForbiddenException, UnauthorizedException, BadRequestException {
        userService.addCredit(amount);
        return Response.ok("Credit added successfully");
    }

    @PostMapping("/purchase")
    public Response<PurchaseReceipt> Purchase()
            throws ForbiddenException, UnauthorizedException, BadRequestException {
        PurchaseReceipt purchaseReceipt = userService.purchaseCart();
        return Response.ok("Purchase completed successfully", purchaseReceipt);
    }

    @PostMapping("/borrow")
    public Response<Object> borrow(@RequestParam String title, @RequestParam Integer days)
            throws ForbiddenException, UnauthorizedException, NotFoundException, BadRequestException {
        userService.borrowBook(title, days);
        return Response.ok("Added borrowed book to cart");
    }

    @GetMapping("/cart")
    public Response<Object> showCart() throws ForbiddenException, UnauthorizedException {
        Map<String, Object> cart = userService.showCart();
        return Response.ok("Cart retrieved successfully", cart);
    }

    @GetMapping("/purchase-history")
    public Response<List<PurchaseRecord>> showPurchaseHistory() throws ForbiddenException, UnauthorizedException {
        List<PurchaseRecord> purchaseHistory = userService.showPurchaseHistory();
        return Response.ok("Purchase history retrieved successfully", purchaseHistory);
    }

    @GetMapping("/purchased-books")
    public Response<List<CartItem>> showPurchasedBooks() throws ForbiddenException, UnauthorizedException {
        List<CartItem> purchaseBooks = userService.showPurchasedBooks();
        return Response.ok("Purchase books retrieved successfully", purchaseBooks);
    }
}
