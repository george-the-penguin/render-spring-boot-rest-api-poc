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

package dev.georgethepenguin.render.poc.model.service;

import dev.georgethepenguin.render.poc.model.entity.Transaction;
import dev.georgethepenguin.render.poc.model.exception.TransactionException;
import dev.georgethepenguin.render.poc.model.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The unit tests for the transaction service.
 *
 * @author Jorge Garcia - George the Penguin
 * @version 1.0.0
 * @since 17
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    /**
     * GIVEN: a null transaction
     * WHEN: create
     * THEN: TransactionException
     */
    @Test
    void givenNullTransactionWhenCreateThenTransactionException() {
        assertThrows(TransactionException.class, () -> transactionService.create(null));
    }

    /**
     * GIVEN: a transaction with id
     * WHEN: create
     * THEN: TransactionException
     */
    @Test
    void givenIdWhenCreateThenTransactionException() {
        final var transaction = new Transaction();
        transaction.setId(UUID.randomUUID());

        assertThrows(TransactionException.class, () -> transactionService.create(transaction));
    }

    /**
     * GIVEN: a transaction with blank description
     * WHEN: create
     * THEN: TransactionException
     */
    @Test
    void givenBlankDescriptionWhenCreateThenTransactionException() {
        final var transaction = new Transaction();
        transaction.setDescription("");

        assertThrows(TransactionException.class, () -> transactionService.create(transaction));
    }

    /**
     * GIVEN: a valid transaction
     * WHEN: create
     * THEN: successful
     *
     * @throws TransactionException if an error occurs
     */
    @Test
    void givenValidTransactionWhenCreateThenSuccessful() throws TransactionException {
        doAnswer(invocation -> {
            final var transaction = invocation.getArgument(0, Transaction.class);
            transaction.setId(UUID.randomUUID());
            return transaction;
        }).when(transactionRepository).save(Mockito.any(Transaction.class));

        final var transaction = new Transaction();
        transaction.setAmount(20.50);
        transaction.setDescription("description");

        final var result = transactionService.create(transaction);

        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getDescription(), result.getDescription());

        verify(transactionRepository).save(Mockito.any(Transaction.class));
    }

    /**
     * GIVEN: a null transaction
     * WHEN: update
     * THEN: TransactionException
     */
    @Test
    void givenNullTransactionWhenUpdateThenTransactionException() {
        assertThrows(TransactionException.class, () -> transactionService.update(null));
    }

    /**
     * GIVEN: a transaction with null id
     * WHEN: update
     * THEN: TransactionException
     */
    @Test
    void givenNullIdWhenUpdateThenTransactionException() {
        assertThrows(TransactionException.class, () -> transactionService.update(new Transaction()));
    }

    /**
     * GIVEN: a transaction with blank description
     * WHEN: update
     * THEN: TransactionException
     */
    @Test
    void givenBlankDescriptionWhenUpdateThenTransactionException() {
        final var transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setDescription("");

        assertThrows(TransactionException.class, () -> transactionService.update(transaction));
    }

    /**
     * GIVEN: a transaction with no existing id
     * WHEN: update
     * THEN: TransactionException
     */
    @Test
    void givenNoExistingIdWhenUpdateThenTransactionException() {
        doReturn(false).when(transactionRepository).existsById(Mockito.any(UUID.class));

        final var transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setDescription("description");

        assertThrows(TransactionException.class, () -> transactionService.update(transaction));

        verify(transactionRepository).existsById(Mockito.any(UUID.class));
    }

    /**
     * GIVEN: a valid transaction
     * WHEN: update
     * THEN: successful
     *
     * @throws TransactionException if an error occurs
     */
    @Test
    void givenValidTransactionWhenUpdateThenSuccessful() throws TransactionException {
        doReturn(true).when(transactionRepository).existsById(Mockito.any(UUID.class));
        doAnswer(invocation -> invocation.getArgument(0, Transaction.class))
                .when(transactionRepository).save(Mockito.any(Transaction.class));

        final var transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAmount(20.50);
        transaction.setDescription("description");

        final var result = transactionService.update(transaction);

        assertEquals(transaction.getId(), result.getId());
        assertNotNull(result.getCreatedAt());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getDescription(), result.getDescription());

        verify(transactionRepository).existsById(Mockito.any(UUID.class));
        verify(transactionRepository).save(Mockito.any(Transaction.class));
    }

    /**
     * GIVEN: a valid id
     * WHEN: findById
     * THEN: successful
     */
    @Test
    void givenIdWhenFindByIdThenSuccessful() {
        doAnswer(invocation -> Optional.of(new Transaction(invocation.getArgument(0, UUID.class),
                LocalDateTime.now(), 20.50, "description")))
                .when(transactionRepository).findById(Mockito.any(UUID.class));

        final var uuid = UUID.randomUUID();
        final var result = transactionService.findById(uuid);

        assertTrue(result.isPresent());
        assertEquals(uuid, result.get().getId());
        assertNotNull(result.get().getCreatedAt());
        assertEquals(20.50, result.get().getAmount());
        assertEquals("description", result.get().getDescription());

        verify(transactionRepository).findById(Mockito.any(UUID.class));
    }

    /**
     * GIVEN: only the method call
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

        doReturn(transactions).when(transactionRepository).findAllOrderByCreatedAtDesc();

        final var result = transactionService.findAll();

        assertIterableEquals(transactions, result);

        verify(transactionRepository).findAllOrderByCreatedAtDesc();
    }

    /**
     * GIVEN: a null id
     * WHEN: deleteById
     * THEN: TransactionException
     */
    @Test
    void givenNullIdWhenDeleteByIdThenTransactionException() {
        assertThrows(TransactionException.class, () -> transactionService.deleteById(null));
    }

    /**
     * GIVEN: a transaction with no existing id
     * WHEN: deleteById
     * THEN: TransactionException
     */
    @Test
    void givenNoExistingIdWhenDeleteByIdThenTransactionException() {
        doReturn(false).when(transactionRepository).existsById(Mockito.any(UUID.class));

        assertThrows(TransactionException.class, () -> transactionService.deleteById(UUID.randomUUID()));

        verify(transactionRepository).existsById(Mockito.any(UUID.class));
    }

    /**
     * GIVEN: a valid id
     * WHEN: deleteById
     * THEN: successful
     *
     * @throws TransactionException if an error occurs
     */
    @Test
    void givenIdWhenDeleteByIdThenSuccessful() throws TransactionException {
        doReturn(true).when(transactionRepository).existsById(Mockito.any(UUID.class));
        doNothing().when(transactionRepository).deleteById(Mockito.any(UUID.class));

        transactionService.deleteById(UUID.randomUUID());

        verify(transactionRepository).existsById(Mockito.any(UUID.class));
        verify(transactionRepository).deleteById(Mockito.any(UUID.class));
    }

    /**
     * GIVEN: only the method call
     * WHEN: getCurrentBalance
     * THEN: successful
     */
    @Test
    void givenMethodCallWhenGetCurrentBalanceThenSuccessful() {
        doReturn(45.3).when(transactionRepository).sumByAmount();

        final var result = transactionService.getCurrentBalance();

        assertEquals(45.3, result);

        verify(transactionRepository).sumByAmount();
    }
}