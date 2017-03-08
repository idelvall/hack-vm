package org.brutusin.nand2tetris.vm2asm.utils;

/*
 * Copyright 2014 brutusin.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Template utility class to process text input streams, line by line.
 *
 * @author Ignacio del Valle Alles idelvall@brutusin.org
 */
public abstract class LineReader {

    public static final String DEFAULT_CHARSET = "UTF-8";
    private InputStream is;
    private boolean exit = false;
    private long lineNumber;
    private String nextLine;
    private String line;
    private final Charset charset;

    /**
     * Creates an instance using {@value #DEFAULT_CHARSET} as the text charset
     * of the input stream
     *
     * @param is Inputstream to process
     */
    public LineReader(InputStream is) {
        this(is, DEFAULT_CHARSET);
    }

    /**
     * Creates an instance using the specified charset
     *
     * @param is Inputstream to process
     * @param charset charset of the input stream
     * @throws UnsupportedCharsetException
     */
    public LineReader(InputStream is, String charset) throws UnsupportedCharsetException {
        this.is = is;
        this.charset = Charset.forName(charset);
    }

    /**
     * Synchronously processes the input stream
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public final void run() throws IOException, InterruptedException {
        InputStreamReader isr = new InputStreamReader(this.is, this.charset);
        BufferedReader br = new BufferedReader(isr);
        this.line = null;
        this.lineNumber = 0;
        try {
            do {
                if (this.exit) {
                    return;
                }
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                this.nextLine = br.readLine();
                try {
                    if (this.line != null) {
                        processLine(this.line);
                        if (this.exit) {
                            return;
                        }
                    }
                } catch (Exception e) {
                    onExceptionFound(e);
                } finally {
                    this.lineNumber++;
                    this.line = this.nextLine;
                }
            } while (this.line != null);
        } finally {
            onFinish();
        }
    }

    /**
     * Stops the processing. To be called by subclasses or clients
     */
    public final void exit() {
        this.exit = true;
    }

    /**
     * Callback method. Guaranteed to be called after processing. Default
     * implementation does nothing
     */
    protected void onFinish() {
    }

    /**
     * Returns the number of the current processing line. 1-based
     *
     * @return the line number
     */
    protected final long getLineNumber() {
        return this.lineNumber;
    }

    /**
     * @return {@code true} if is last line. {@code false} otherwise
     */
    protected final boolean isLastLine() {
        return this.nextLine == null;
    }

    /**
     * Returns current line content
     *
     * @return
     */
    public final String getLine() {
        return line;
    }

    /**
     * The actual processing to perform.
     *
     * @param line current line
     * @throws Exception
     */
    protected abstract void processLine(String line) throws Exception;

    /**
     * Exception handling. Processing continues unless this method throws an
     * uncatched throwable or {@link exit()} is called
     *
     * @param ex current exception
     */
    protected abstract void onExceptionFound(Exception ex);
}
