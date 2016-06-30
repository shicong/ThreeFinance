/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.guysshare.publics;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * I/O工具类
 * Created by chengyuan02 on 7/13 0013.
 * @author chengyuan02
 */
public class IOUtils {

    private static final String TAG = "IOUtils";
    private static final boolean IS_DEBUG = false;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;

    private static final int EOF = -1;

    public interface IOReadBytesCallback {
        void onCallback(byte[] buffer, int offset, int length) throws IOException;
    }

    public interface IOReadCharsCallback {
        void onCallback(char[] buffer, int offset, int length) throws IOException;
    }

    public interface IOReadLinesCallback {
        void onCallback(String line) throws IOException;
    }

    private static final class IOCopyBytesCallback implements IOReadBytesCallback {

        private OutputStream mOut;

        private IOCopyBytesCallback(OutputStream out) {
            if (out == null) {
                throw new NullPointerException("Out must not be null");
            }
            mOut = out;
        }

        @Override
        public void onCallback(byte[] buffer, int offset, int length) throws IOException {
            mOut.write(buffer, offset, length);
        }
    }

    private static final class IOCopyLinesCallback implements IOReadLinesCallback {

        private BufferedWriter mOut;

        private IOCopyLinesCallback(Writer out) {
            if (out == null) {
                throw new NullPointerException("Out must not be null");
            }
            mOut = toBufferedWriter(out);
        }

        @Override
        public void onCallback(String line) throws IOException {
            mOut.write(line);
            mOut.newLine();
        }
    }

    private static final class IOReadBytesFullyCallback implements IOReadBytesCallback {

        private ByteArrayOutputStream mOut = new ByteArrayOutputStream();

        @Override
        public void onCallback(byte[] buffer, int offset, int length) throws IOException {
            mOut.write(buffer, offset, length);
        }

        public byte[] toByteArray() throws IOException {
            byte[] bytes = null;
            try {
                mOut.flush();
                bytes = mOut.toByteArray();
            } catch (IOException e) {
                throw e;
            } finally {
                closeQuietly(mOut);
            }
            return bytes;
        }
    }

    private static final class IOReadLinesFullyCallback implements IOReadLinesCallback {

        private List<String> mList = new ArrayList<String>();

        @Override
        public void onCallback(String line) throws IOException {
            mList.add(line);
        }

        public List<String> toList() {
            return mList;
        }
    }

    private static final class CloseTracker {

        private volatile RuntimeException exception;

        private CloseTracker() {
        }

        public static CloseTracker get() {
            return new CloseTracker();
        }

        public void open(String closer) {
            exception = new RuntimeException(closer + "'s close method not called");
        }

        public void close() {
            exception = null;
        }

        public void warnIfOpen() {
            if (exception == null) {
                return;
            }
            if (IS_DEBUG) {
                throw exception;
            }
        }
    }

    /**
     * 可自动关闭的输入字节流，当读至流末尾或者操作发生异常或者{@link #finalize()}方法被调用时，自动调用close方法
     */
    public static class AutoCloseInputStream extends InputStream {

        private final CloseTracker tracker = CloseTracker.get();
        private final InputStream in;

        private volatile boolean shouldClose;

        public AutoCloseInputStream(InputStream in) {
            this.in = in;
            this.shouldClose = true;
            if (IS_DEBUG) {
                tracker.open(in.getClass().getName());
            }
        }

        @Override
        public int available() throws IOException {
            try {
                return in.available();
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public synchronized void mark(int readlimit) {
            if (in.markSupported()) {
                in.mark(readlimit);
            }
        }

        @Override
        public boolean markSupported() {
            return in.markSupported();
        }

        @Override
        public int read() throws IOException {
            try {
                final int bytesRead = in.read();
                if (bytesRead == -1) {
                    close();
                }
                return bytesRead;
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public int read(byte[] buffer) throws IOException {
            return read(buffer, 0, buffer.length);
        }

        @Override
        public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            try {
                final int bytesRead = in.read(buffer, byteOffset, byteCount);
                if (bytesRead == -1) {
                    close();
                }
                return bytesRead;
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public synchronized void reset() throws IOException {
            try {
                in.reset();
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public long skip(long byteCount) throws IOException {
            try {
                return in.skip(byteCount);
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public void close() throws IOException {
            if (shouldClose) {
                in.close();
                shouldClose = false;
                if (IS_DEBUG) {
                    tracker.close();
                }
            }
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                if (IS_DEBUG) {
                    if (shouldClose && tracker != null) {
                        tracker.warnIfOpen();
                    }
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    /**
     * 可自动关闭的输出字节流，当操作发生异常或者{@link #finalize()}方法被调用时，自动调用close方法
     */
    public static class AutoCloseOutputStream extends OutputStream {

        private final CloseTracker tracker = CloseTracker.get();
        private final OutputStream out;

        private volatile boolean shouldClose;

        public AutoCloseOutputStream(OutputStream out) {
            this.out = out;
            this.shouldClose = true;
            if (IS_DEBUG) {
                tracker.open(out.getClass().getName());
            }
        }

        @Override
        public void write(int oneByte) throws IOException {
            try {
                out.write(oneByte);
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            try {
                out.write(buffer, offset, count);
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public void flush() throws IOException {
            try {
                out.flush();
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public void close() throws IOException {
            if (shouldClose) {
                try {
                    out.flush();
                } finally {
                    out.close();
                    shouldClose = false;
                    if (IS_DEBUG) {
                        tracker.close();
                    }
                }
            }
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                if (IS_DEBUG) {
                    if (shouldClose && tracker != null) {
                        tracker.warnIfOpen();
                    }
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    /**
     * 可自动关闭的输入字符流，当读至流末尾或者操作发生异常或者{@link #finalize()}方法被调用时，自动调用close方法
     */
    public static class AutoCloseReader extends Reader {
        private final CloseTracker tracker = CloseTracker.get();
        private final Reader in;

        private volatile boolean shouldClose;

        public AutoCloseReader(Reader in) {
            super(in);
            this.in = in;
            this.shouldClose = true;
            if (IS_DEBUG) {
                tracker.open(in.getClass().getName());
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (lock) {
                if (shouldClose) {
                    in.close();
                    shouldClose = false;
                    if (IS_DEBUG) {
                        tracker.close();
                    }
                }
            }
        }

        @Override
        public void mark(int readLimit) throws IOException {
            try {
                synchronized (lock) {
                    if (in.markSupported()) {
                        in.mark(readLimit);
                    }
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public boolean markSupported() {
            synchronized (lock) {
                return in.markSupported();
            }
        }

        @Override
        public int read() throws IOException {
            try {
                synchronized (lock) {
                    final int readCount = in.read();
                    if (readCount == -1) {
                        close();
                    }
                    return readCount;
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public int read(char[] buffer, int offset, int count) throws IOException {
            try {
                synchronized (lock) {
                    final int readCount = in.read(buffer, offset, count);
                    if (readCount == -1) {
                        close();
                    }
                    return readCount;
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public int read(CharBuffer target) throws IOException {
            try {
                synchronized (lock) {
                    final int readCount = in.read(target);
                    if (readCount == -1) {
                        close();
                    }
                    return readCount;
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public boolean ready() throws IOException {
            try {
                synchronized (lock) {
                    return in.ready();
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public void reset() throws IOException {
            try {
                synchronized (lock) {
                    in.reset();
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public long skip(long charCount) throws IOException {
            try {
                synchronized (lock) {
                    return in.skip(charCount);
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                if (IS_DEBUG) {
                    if (shouldClose && tracker != null) {
                        tracker.warnIfOpen();
                    }
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    /**
     * 可自动关闭的输出字符流，当操作发生异常或者{@link #finalize()}方法被调用时，自动调用close方法
     */
    public static class AutoCloseWriter extends Writer {
        private final CloseTracker tracker = CloseTracker.get();
        private final Writer out;

        private volatile boolean shouldClose;

        public AutoCloseWriter(Writer out) {
            super(out);
            this.out = out;
            this.shouldClose = true;
            if (IS_DEBUG) {
                tracker.open(out.getClass().getName());
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (lock) {
                if (shouldClose) {
                    try {
                        out.flush();
                    } finally {
                        out.close();
                        shouldClose = false;
                        if (IS_DEBUG) {
                            tracker.close();
                        }
                    }
                }
            }
        }

        @Override
        public void flush() throws IOException {
            try {
                synchronized (lock) {
                    out.flush();
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public void write(char[] buf, int offset, int count) throws IOException {
            try {
                synchronized (lock) {
                    out.write(buf, offset, count);
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public void write(int oneChar) throws IOException {
            try {
                synchronized (lock) {
                    out.write(oneChar);
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        public void write(String str, int offset, int count) throws IOException {
            try {
                synchronized (lock) {
                    out.write(str, offset, count);
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                if (IS_DEBUG) {
                    if (shouldClose && tracker != null) {
                        tracker.warnIfOpen();
                    }
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    /**
     * 逐行遍历字符流的迭代器
     */
    public static class LineIterator implements Iterator<String> {

        private final CloseTracker tracker = CloseTracker.get();
        private final BufferedReader bufferedReader;

        private String cachedLine;
        private boolean finished = false;
        private boolean errored = false;
        private boolean shouldClose;

        public LineIterator(final Reader reader) throws IllegalArgumentException {
            if (reader == null) {
                throw new IllegalArgumentException("Reader must not be null");
            }
            bufferedReader = toBufferedReader(reader);
            shouldClose = true;
            if (IS_DEBUG) {
                tracker.open(reader.getClass().getName());
            }
        }

        public boolean hasError() {
            return errored;
        }

        @Override
        public boolean hasNext() {
            if (cachedLine != null) {
                return true;
            } else if (finished) {
                return false;
            } else {
                try {
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            finished = true;
                            return false;
                        } else if (isValidLine(line)) {
                            cachedLine = line;
                            return true;
                        }
                    }
                } catch (IOException ioe) {
                    errored = true;
                    close();
                    return false;
                }
            }
        }

        protected boolean isValidLine(String line) {
            return true;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more lines");
            }
            String currentLine = cachedLine;
            cachedLine = null;
            return currentLine;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove unsupported on LineIterator");
        }

        public void close() {
            if (shouldClose) {
                finished = true;
                closeQuietly(bufferedReader);
                cachedLine = null;
                shouldClose = false;
                if (IS_DEBUG) {
                    tracker.close();
                }
            }
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                if (IS_DEBUG) {
                    if (shouldClose && tracker != null) {
                        tracker.warnIfOpen();
                    }
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    /**
     * 静默关闭流，不抛异常
     *
     * @param closeable
     * @return 是否正常关闭
     */
    public static boolean closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static BufferedInputStream toBufferedInputStream(InputStream input) {
        return input instanceof BufferedInputStream ? (BufferedInputStream) input : new BufferedInputStream(input,
                DEFAULT_BUFFER_SIZE);
    }

    public static BufferedInputStream toBufferedInputStream(InputStream input, int size) {
        return input instanceof BufferedInputStream ? (BufferedInputStream) input
                : new BufferedInputStream(input, size);
    }

    public static BufferedOutputStream toBufferedOutputStream(OutputStream output) {
        return output instanceof BufferedOutputStream ? (BufferedOutputStream) output : new BufferedOutputStream(
                output, DEFAULT_BUFFER_SIZE);
    }

    public static BufferedOutputStream toBufferedOutputStream(OutputStream output, int size) {
        return output instanceof BufferedOutputStream ? (BufferedOutputStream) output : new BufferedOutputStream(
                output, size);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader,
                DEFAULT_BUFFER_SIZE);
    }

    public static BufferedReader toBufferedReader(Reader reader, int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }

    public static BufferedWriter toBufferedWriter(Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer,
                DEFAULT_BUFFER_SIZE);
    }

    public static BufferedWriter toBufferedWriter(Writer writer, int size) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer, size);
    }

    /**
     * 一次性向输出流写入字节数组
     *
     * @param buffer 要写入数据缓存
     * @param offset 读取缓存数据的起始位置
     * @param count  从缓存中读取的数据
     * @param output 输出流
     * @throws IOException
     */
    public static void write(byte[] buffer, int offset, int count, OutputStream output) throws IOException {
        try {
            if (buffer != null) {
                output.write(buffer, offset, count);
            }
            output.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(output);
        }
    }

    /**
     * 一次性向输出流写入字符数组
     *
     * @param buffer 要写入数据缓存
     * @param offset 读取缓存数据的起始位置
     * @param count  从缓存中读取的数据
     * @param output 输出流
     * @throws IOException
     */
    public static void write(char[] buffer, int offset, int count, Writer output) throws IOException {
        try {
            if (buffer != null) {
                output.write(buffer, offset, count);
            }
            output.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(output);
        }
    }

    /**
     * 一次性向输出流中写入多行数据
     *
     * @param lines  要写入数据集合
     * @param output 输出流
     * @return 实际写入的行数
     * @throws IOException
     */
    public static int writeLines(Collection<?> lines, Writer output) throws IOException {
        int totalWrite = 0;
        BufferedWriter writer = toBufferedWriter(output);
        try {
            if (lines == null) {
                return 0;
            }
            for (Object line : lines) {
                if (line != null) {
                    writer.write(line.toString());
                }
                writer.newLine();
                totalWrite++;
            }
            writer.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(writer);
        }

        return totalWrite;
    }

    /**
     * 一次性以字节流方式读取数据
     *
     * @param input        输入流
     * @return 读到的数据
     * @throws Exception
     */
    public static byte[] read(InputStream input) throws IOException {
        return read(input, 0, Integer.MAX_VALUE, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 一次性以字节流方式读取数据
     *
     * @param input        输入流
     * @param toSkip       要跳过的字节数
     * @param length       要读取的字节数
     * @param bufferLength 缓冲区长度
     * @return 读到的数据
     * @throws IOException
     */
    public static byte[] read(InputStream input, int toSkip, int length, int bufferLength) throws IOException {
        byte[] bytes = null;
        IOReadBytesFullyCallback callback = null;
        try {
            callback = new IOReadBytesFullyCallback();
            read(input, callback, toSkip, length, bufferLength);
        } catch (IOException e) {
            throw e;
        } finally {
            if (callback != null) {
                bytes = callback.toByteArray();
            }
        }

        return bytes;
    }

    public static int read(InputStream input, IOReadBytesCallback callback) throws IOException {
        return read(input, callback, 0, Integer.MAX_VALUE, DEFAULT_BUFFER_SIZE);
    }

    public static int read(InputStream input, IOReadBytesCallback callback, int toSkip, int length) throws IOException {
        return read(input, callback, toSkip, length, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 一次性以字节流方式读取数据
     *
     * @param input        输入流
     * @param callback     读数据流的回调
     * @param toSkip       要跳过的字节数
     * @param length       要读取的字节数
     * @param bufferLength 缓冲区长度
     * @return 实际读取的字节数
     * @throws IOException
     */
    public static int read(InputStream input, IOReadBytesCallback callback, int toSkip, int length, int bufferLength)
            throws IOException {
        int totalRead = 0;
        try {
            if (callback == null) {
                if (IS_DEBUG) {
                    throw new NullPointerException("Callback must not be null");
                }
                return 0;
            }
            if (length <= 0) {
                if (IS_DEBUG) {
                    throw new IllegalArgumentException("Length must be positive: " + length);
                }
                return 0;
            }
            if (toSkip > 0 && input.skip(toSkip) != toSkip) {
                return 0;
            }
            byte[] buffer = new byte[bufferLength];
            int read;
            int toRead = bufferLength;
            if (length > 0 && length < bufferLength) {
                toRead = length;
            }
            while (totalRead < length) {
                read = input.read(buffer, 0, toRead);
                if (read == EOF) {
                    break;
                }
                callback.onCallback(buffer, 0, read);
                totalRead += read;
                toRead = Math.min(length - totalRead, bufferLength);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(input);
        }

        return totalRead;
    }

    /**
     * 一次性以字符流方式读取数据
     *
     * @param input       输入流
     * @param callback        读取数据流回调接口
     * @param toSkip          要跳过的字节数
     * @param length          读取总字节数
     * @param bufferLength    缓冲区大小
     * @return 实际读取的字节数
     * @throws IOException
     */
    public static int read(Reader input, IOReadCharsCallback callback, int toSkip, int length, int bufferLength)
            throws IOException {
        int totalRead = 0;
        try {
            if (callback == null) {
                if (IS_DEBUG) {
                    throw new NullPointerException("Callback must not be null");
                }
                return 0;
            }
            if (length <= 0) {
                if (IS_DEBUG) {
                    throw new IllegalArgumentException("Length must be positive: " + length);
                }
                return 0;
            }
            if (toSkip > 0 && input.skip(toSkip) != toSkip) {
                return 0;
            }
            char[] buffer = new char[bufferLength];
            int read;
            int toRead = bufferLength;
            if (length > 0 && length < bufferLength) {
                toRead = length;
            }
            while (totalRead < length) {
                read = input.read(buffer, 0, toRead);
                if (read == EOF) {
                    break;
                }
                callback.onCallback(buffer, 0, read);
                totalRead += read;
                toRead = Math.min(length - totalRead, bufferLength);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(input);
        }

        return totalRead;
    }

    /**
     * 一次性以字符流方式逐行读取数据
     *
     * @param input  输入流
     * @param toSkip 要跳过的行数
     * @param length 要读取的行数
     * @return 读到的数据
     * @throws IOException
     */
    public static List<String> readLines(Reader input, int toSkip, int length) throws IOException {
        List<String> list = null;
        IOReadLinesFullyCallback callback = null;
        try {
            callback = new IOReadLinesFullyCallback();
            readLines(input, callback, toSkip, length);
        } catch (IOException e) {
            throw e;
        } finally {
            if (callback != null) {
                list = callback.toList();
            }
        }
        return list;
    }

    public static int readLines(Reader input, IOReadLinesCallback callback) throws IOException {
        return readLines(input, callback, 0, Integer.MAX_VALUE);
    }

    /**
     * 一次性以字符流方式逐行读取数据
     *
     * @param input    输入流
     * @param callback 逐行读字符流的回调
     * @param toSkip   要跳过的行数
     * @param length   要读取的行数
     * @return 实际读取的行数
     * @throws IOException
     */
    public static int readLines(Reader input, IOReadLinesCallback callback, int toSkip, int length) throws IOException {
        int totalRead = 0;
        try {
            if (callback == null) {
                if (IS_DEBUG) {
                    throw new NullPointerException("Callback must not be null");
                }
                return 0;
            }
            if (length <= 0) {
                if (IS_DEBUG) {
                    throw new IllegalArgumentException("Length must not be positive: " + length);
                }
            }
            BufferedReader reader = toBufferedReader(input);
            if (toSkip > 0 && skipLines(reader, toSkip) != toSkip) {
                return 0;
            }
            while (totalRead < length) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                callback.onCallback(line);
                totalRead++;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(input);
        }
        return totalRead;
    }

    /**
     * 一次性将数据以字节流的形式拷贝
     *
     * @param input      输入流
     * @param output     输出流
     * @param toSkip     要跳过的字节数
     * @param length     要读取的字节数
     * @param bufferSize 缓冲区大小
     *
     * @return 实际拷贝的字节数
     *
     * @throws IOException
     */
    public static int copy(InputStream input, OutputStream output, int toSkip, int length, int bufferSize)
            throws IOException {
        int count = 0;
        try {
            IOCopyBytesCallback callback = new IOCopyBytesCallback(output);
            count = read(input, callback, toSkip, length, bufferSize);
            output.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(output);
        }
        return count;
    }

    /**
     * 一次性将数据以字符流的形式逐行拷贝
     *
     * @param reader reader输入流
     * @param writer writer输出流
     * @param toSkip 要跳过的字节数
     * @param length 需要拷贝的字节数
     *
     * @return 实际拷贝的 字节数
     *
     * @throws IOException
     */
    public static int copyLines(Reader reader, Writer writer, int toSkip, int length) throws IOException {
        int count = 0;
        IOCopyLinesCallback callback = null;
        try {
            callback = new IOCopyLinesCallback(writer);
            count = readLines(reader, callback, toSkip, length);
            writer.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(writer);
        }
        return count;
    }

    /**
     * 跳过输入字符流的前几行
     *
     * @param input  输入流
     * @param toSkip 要跳过的行数
     * @return 实际跳过的行数
     * @throws IOException
     */
    public static int skipLines(Reader input, int toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        int remain = toSkip;
        BufferedReader reader = toBufferedReader(input);
        String line;
        while (remain > 0) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            remain--;
        }
        return toSkip - remain;
    }
}
