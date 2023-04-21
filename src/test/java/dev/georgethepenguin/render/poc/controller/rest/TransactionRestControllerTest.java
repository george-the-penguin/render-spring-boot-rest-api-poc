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

import dev.georgethepenguin.render.poc.model.entity.Transaction;
import dev.georgethepenguin.render.poc.model.exception.TransactionException;
import dev.georgethepenguin.render.poc.model.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

/**
 * The unit tests for the transaction REST controller.
 *
 * @author Jorge Garcia - George the Penguin
 * @version 1.0.0
 * @since 17
 */
@ExtendWith(MockitoExtension.class)
class TransactionRestControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionRestController transactionRestController;

    /**
     * GIVEN: a method call
     * WHEN: findAll
     * THEN: successful
     */
    @Test
    void givenMethodCallWhenFindAllThenSuccessful() {
        final var transactions = new ArrayList<>(List.of(
                new Transaction(UUID.randomUUID(), LocalDateTime.now(), 20.50, "description 1"),
                new Transaction(UUID.randomUUID(), LocalDateTime.now(), 30.50, "description 2")
        ));

        transactions.sort(Comparator.comparing(Transaction::getCreatedAt).reversed());

        doReturn(transactions).when(transactionService).findAll();

        final var result = transactionRestController.findAll();

        assertEquals(OK, result.getStatusCode());
        assertIterableEquals(transactions, result.getBody());

        verify(transactionService).findAll();
    }

    /**
     * GIVEN: no existing id
     * WHEN: findById
     * THEN: not found status
     */
    @Test
    void givenNoExistingIdWhenFindByIdThenNotFoundStatus() {
        doReturn(Optional.empty()).when(transactionService).findById(any(UUID.class));

        final var result = transactionRestController.findById(UUID.randomUUID().toString());

        assertEquals(NOT_FOUND, result.getStatusCode());

        verify(transactionService).findById(any(UUID.class));
    }

    /**
     * GIVEN: existing id
     * WHEN: findById
     * THEN: successful
     */
    @Test
    void givenExistingIdWhenFindByIdThenSuccessful() {
        doAnswer(invocation -> Optional.of(new Transaction(invocation.getArgument(0, UUID.class), LocalDateTime.now(),
                20.50, "description")))
                .when(transactionService).findById(any(UUID.class));

        final var uuid = UUID.randomUUID();
        final var result = transactionRestController.findById(uuid.toString());

        assertEquals(OK, result.getStatusCode());

        final var body = result.getBody();
        assertNotNull(body);
        assertEquals(uuid, body.getId());
        assertNotNull(body.getCreatedAt());
        assertEquals(20.50, body.getAmount());
        assertEquals("description", body.getDescription());

        verify(transactionService).findById(any(UUID.class));
    }

    /**
     * GIVEN: new transaction
     * WHEN: create
     * THEN: successful
     *
     * @throws TransactionException if an error occurs
     */
    @Test
    void givenNewTransactionWhenCreateThenSuccessful() throws TransactionException {
        doAnswer(invocation -> {
            final var transaction = invocation.getArgument(0, Transaction.class);
            transaction.setId(UUID.randomUUID());
            transaction.setCreatedAt(LocalDateTime.now());
            return transaction;
        }).when(transactionService).create(any(Transaction.class));

        final var transaction = new Transaction();
        transaction.setAmount(20.50);
        transaction.setDescription("description");

        final var result = transactionRestController.create(transaction);

        assertEquals(OK, result.getStatusCode());

        final var body = result.getBody();
        assertNotNull(body);

        assertNotNull(body.getId());
        assertNotNull(body.getCreatedAt());
        assertEquals(transaction.getAmount(), body.getAmount());
        assertEquals(transaction.getDescription(), body.getDescription());

        verify(transactionService).create(any(Transaction.class));
    }

    /**
     * GIVEN: existing transaction
     * WHEN: update
     * THEN: successful
     *
     * @throws TransactionException if an error occurs
     */
    @Test
    void givenExistingTransactionWhenUpdateThenSuccessful() throws TransactionException {
        doAnswer(invocation -> {
            final var transaction = invocation.getArgument(0, Transaction.class);
            transaction.setCreatedAt(LocalDateTime.now());
            return transaction;
        }).when(transactionService).update(any(Transaction.class));

        final var transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAmount(20.50);
        transaction.setDescription("description");

        final var result = transactionRestController.update(transaction);

        assertEquals(OK, result.getStatusCode());

        final var body = result.getBody();
        assertNotNull(body);

        assertEquals(transaction.getId(), body.getId());
        assertNotNull(body.getCreatedAt());
        assertEquals(transaction.getAmount(), body.getAmount());
        assertEquals(transaction.getDescription(), body.getDescription());

        verify(transactionService).update(any(Transaction.class));
    }

    /**
     * GIVEN: existing id
     * WHEN: deleteById
     * THEN: successful
     *
     * @throws TransactionException if an error occurs
     */
    @Test
    void givenExistingIdWhenDeleteByIdThenSuccessful() throws TransactionException {
        doNothing().when(transactionService).deleteById(any(UUID.class));

        final var result = transactionRestController.delete(UUID.randomUUID().toString());

        assertEquals(OK, result.getStatusCode());
        assertNull(result.getBody());

        verify(transactionService).deleteById(any(UUID.class));
    }

    /**
     * GIVEN: a method call
     * WHEN: getCurrentBalance
     * THEN: successful
     */
    @Test
    void givenMethodCallWhenGetCurrentBalanceThenSuccessful() {
        doReturn(20.50).when(transactionService).getCurrentBalance();

        final var result = transactionRestController.getCurrentBalance();

        assertEquals(OK, result.getStatusCode());

        final var body = result.getBody();
        assertNotNull(body);

        assertEquals(20.50, body.get("balance"));

        verify(transactionService).getCurrentBalance();
    }
}