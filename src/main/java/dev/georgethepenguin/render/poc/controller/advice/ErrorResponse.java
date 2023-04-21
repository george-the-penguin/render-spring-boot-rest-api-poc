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

package dev.georgethepenguin.render.poc.controller.advice;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The error response.
 *
 * @param dateTime   the date time
 * @param httpStatus the http status reason
 * @param message    the message
 * @author Jorge Garcia - George the Penguin
 * @version 1.0.0
 * @since 17
 */
public record ErrorResponse(LocalDateTime dateTime,
                            String httpStatus,
                            String message) implements Serializable {

    @Serial
    private static final long serialVersionUID = -8478801551741466606L;

}
