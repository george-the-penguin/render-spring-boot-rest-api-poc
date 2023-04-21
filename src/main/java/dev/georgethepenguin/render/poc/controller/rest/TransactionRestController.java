/*
 * MIT License
 *
 * Copyright (c) 2023 Jorge Garcia - George the Penguin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package dev.georgethepenguin.render.poc.controller.rest;

import dev.georgethepenguin.render.poc.controller.advice.ErrorResponse;
import dev.georgethepenguin.render.poc.model.entity.Transaction;
import dev.georgethepenguin.render.poc.model.exception.TransactionException;
import dev.georgethepenguin.render.poc.model.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * The transaction REST controller.
 *
 * @author Jorge Garcia - George the Penguin
 * @version 1.0.0
 * @since 17
 */
@RestController
@RequestMapping("/api/transaction")
@Tag(name = "Transaction", description = "The transaction API")
public class TransactionRestController {

    private final TransactionService transactionService;

    /**
     * Constructor.
     *
     * @param transactionService the transaction service
     */
    @Autowired
    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Find all the transactions.
     *
     * @return the response entity with the list of transactions.
     */
    @Operation(
            summary = "Find all the transactions",
            description = "Find all the transactions",
            tags = {"Transaction"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class)))})
    @GetMapping
    public ResponseEntity<Iterable<Transaction>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    /**
     * Find a transaction by id.
     *
     * @param id the id
     * @return the response entity with the transaction.
     */
    @Operation(
            summary = "Find a transaction by id",
            description = "Find a transaction by id",
            tags = {"Transaction"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable("id") String id) {
        final var optionalTransaction = transactionService.findById(UUID.fromString(id));
        return optionalTransaction.isEmpty() ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(optionalTransaction.get());
    }

    /**
     * Create a transaction.
     *
     * @param transaction the transaction
     * @return the response entity with the transaction.
     * @throws TransactionException the transaction exception
     */
    @Operation(
            summary = "Create a transaction",
            description = "Create a transaction",
            tags = {"Transaction"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction) throws TransactionException {
        return ResponseEntity.ok(transactionService.create(transaction));
    }

    /**
     * Update a transaction.
     *
     * @param transaction the transaction
     * @return the response entity with the transaction.
     * @throws TransactionException the transaction exception
     */
    @Operation(
            summary = "Update a transaction",
            description = "Update a transaction",
            tags = {"Transaction"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping
    public ResponseEntity<Transaction> update(@RequestBody Transaction transaction) throws TransactionException {
        return ResponseEntity.ok(transactionService.update(transaction));
    }

    /**
     * Delete a transaction.
     *
     * @param id the id
     * @return the response entity with the transaction.
     * @throws TransactionException the transaction exception
     */
    @Operation(
            summary = "Delete a transaction",
            description = "Delete a transaction",
            tags = {"Transaction"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) throws TransactionException {
        transactionService.deleteById(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    /**
     * Get the current balance.
     *
     * @return the response entity with the current balance.
     */
    @Operation(
            summary = "Get the current balance",
            description = "Get the current balance",
            tags = {"Transaction"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class)))})
    @GetMapping("/current-balance")
    public ResponseEntity<Map<String, Object>> getCurrentBalance() {
        return ResponseEntity.ok(Map.of(
                "dateTime", LocalDateTime.now(),
                "balance", transactionService.getCurrentBalance()
        ));
    }
}
