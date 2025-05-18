package org.example.controller;

import org.example.dto.*;
import org.example.exception.BadRequestException;
import org.example.exception.ForbiddenException;
import org.example.exception.NotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.model.*;
import org.example.response.Response;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/addCart")
    public Response<Object> addCart(@RequestParam String title,
                                    @RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException, NotFoundException, BadRequestException {

        userService.addCart(title, token);
        return Response.ok("Added book to cart");
    }

    @PostMapping("/removeCart")
    public Response<Object> removeCart(@RequestParam String title,
                                       @RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException, NotFoundException, BadRequestException {

        userService.removeCart(title, token);
        return Response.ok("Removed book from cart");
    }

    @PostMapping("/addCredit")
    public Response<UserDTO> addCredit(@RequestParam int amount,
                                       @RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException, BadRequestException {

        User updatedCustomer = userService.addCredit(amount, token);
        return Response.ok("Credit added successfully",
                DtoMapper.userToDTO(updatedCustomer));
    }

    @PostMapping("/purchase")
    public Response<PurchaseReceipt> Purchase(@RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException, BadRequestException {

        PurchaseReceipt purchaseReceipt = userService.purchaseCart(token);
        return Response.ok("Purchase completed successfully", purchaseReceipt);
    }

    @PostMapping("/borrow")
    public Response<Object> borrow(@RequestParam String title, @RequestParam Integer days,
                                   @RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException, NotFoundException, BadRequestException {

        userService.borrowBook(title, days, token);
        return Response.ok("Added borrowed book to cart");
    }

    @GetMapping("/cart")
    public Response<CartDTO> showCart(@RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException {

        Cart cart = userService.showCart(token);
        return Response.ok("Cart retrieved successfully",
                DtoMapper.CartToDTO(cart));
    }

    @GetMapping("/purchase-history")
    public Response<List<PurchaseRecordDTO>> showPurchaseHistory(@RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException {

        List<PurchaseRecord> purchaseHistory = userService.showPurchaseHistory(token);
        return Response.ok("Purchase history retrieved successfully",
                purchaseHistory.stream().map(DtoMapper::purchaseRecordToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/purchased-books")
    public Response<List<ItemDTO>> showPurchasedBooks(@RequestHeader("Authorization") String token)
            throws ForbiddenException, UnauthorizedException {

        List<PurchaseItem> purchaseBooks = userService.showPurchasedBooks(token);
        return Response.ok("Purchase books retrieved successfully",
                purchaseBooks.stream().map(DtoMapper::purchaseItemToDTO).collect(Collectors.toList()));
    }
}
