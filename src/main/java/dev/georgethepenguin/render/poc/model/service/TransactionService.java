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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * The business service for the Transaction entity.
 *
 * @author Jorge Garcia - George the Penguin
 * @version 1.0.0
 * @since 17
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Constructor.
     *
     * @param transactionRepository the transaction repository
     */
    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Create a transaction.
     *
     * @param transaction the transaction
     * @return the created transaction
     * @throws TransactionException if the transaction is null or the id is not null or the description is blank.
     */
    public Transaction create(final Transaction transaction) throws TransactionException {
        if (transaction == null) {
            throw new TransactionException("The transaction is null");
        }

        if (transaction.getId() != null) {
            throw new TransactionException("The transaction id is not null");
        }

        if (StringUtils.isBlank(transaction.getDescription())) {
            throw new TransactionException("The transaction description is blank");
        }

        transaction.setCreatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    /**
     * Update a transaction.
     *
     * @param transaction the transaction
     * @return the updated transaction
     * @throws TransactionException if the transaction is null or the id is null or the description is blank or the id
     *                              does not exist.
     */
    public Transaction update(final Transaction transaction) throws TransactionException {
        if (transaction == null) {
            throw new TransactionException("The transaction is null");
        }

        if (transaction.getId() == null) {
            throw new TransactionException("The transaction id is null");
        }

        if (StringUtils.isBlank(transaction.getDescription())) {
            throw new TransactionException("The transaction description is blank");
        }

        if (!transactionRepository.existsById(transaction.getId())) {
            throw new TransactionException("The transaction id does not exist: " + transaction.getId());
        }

        transaction.setCreatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    /**
     * Find a transaction by id.
     *
     * @param uuid the id
     * @return the transaction
     */
    public Optional<Transaction> findById(final UUID uuid) {
        return transactionRepository.findById(uuid);
    }

    /**
     * Find all the transactions.
     *
     * @return the list of transactions
     */
    public Iterable<Transaction> findAll() {
        return transactionRepository.findAllOrderByCreatedAtDesc();
    }

    /**
     * Delete a transaction by id.
     *
     * @param uuid the id
     * @throws TransactionException if the id is null or the id does not exist.
     */
    public void deleteById(final UUID uuid) throws TransactionException {
        if (uuid == null) {
            throw new TransactionException("The transaction is null");
        }

        if (!transactionRepository.existsById(uuid)) {
            throw new TransactionException("The transaction id does not exist: " + uuid);
        }

        transactionRepository.deleteById(uuid);
    }

    /**
     * Get the current balance.
     *
     * @return the current balance
     */
    public Double getCurrentBalance() {
        return transactionRepository.sumByAmount();
    }
}
