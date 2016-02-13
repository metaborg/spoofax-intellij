/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.intellij.vfs;

import com.intellij.openapi.editor.*;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.*;
import org.apache.commons.vfs2.util.*;
import org.jetbrains.annotations.*;

import java.io.*;

@Deprecated
public class RandomAccessDocument extends InputStream implements RandomAccessContent {

    @Nullable private Document document;
    private long position;

    public RandomAccessDocument(final Document document) {
        this(document, 0);
    }

    private RandomAccessDocument(final Document document, final long position) {
        this.document = document;
        this.position = position;
    }

    /**
     * Asserts that the stream is not closed.
     *
     * @throws IOException
     */
    private void assertStreamNotClosed() throws IOException {
        if (this.document == null)
            throw new IOException("Stream is closed.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFilePointer() throws IOException {
        return this.position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void seek(final long position) throws IOException {
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long length() throws IOException {
        assertStreamNotClosed();
        assert this.document != null;

        return this.document.getTextLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        this.document = null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        assertStreamNotClosed();
        assert this.document != null;

        return new RandomAccessDocument(this.document, this.position);
    }

    @Override
    public void readFully(final byte[] b) throws IOException {

    }

    @Override
    public void readFully(final byte[] b, final int off, final int len)
            throws IOException {

    }

    @Override
    public int skipBytes(final int n)
            throws IOException {
        return 0;
    }

    @Override
    public boolean readBoolean()
            throws IOException {
        return false;
    }

    @Override
    public int read()
            throws IOException {
        return 0;
    }

    @Override
    public byte readByte()
            throws IOException {
        return 0;
    }

    @Override
    public int readUnsignedByte()
            throws IOException {
        return 0;
    }

    @Override
    public short readShort()
            throws IOException {
        return 0;
    }

    @Override
    public int readUnsignedShort()
            throws IOException {
        return 0;
    }

    @Override
    public char readChar()
            throws IOException {
        return 0;
    }

    @Override
    public int readInt()
            throws IOException {
        return 0;
    }

    @Override
    public long readLong()
            throws IOException {
        return 0;
    }

    @Override
    public float readFloat()
            throws IOException {
        return 0;
    }

    @Override
    public double readDouble()
            throws IOException {
        return 0;
    }

    @Override
    public String readLine()
            throws IOException {
        return null;
    }

    @NotNull
    @Override
    public String readUTF()
            throws IOException {
        return null;
    }

    @Override
    public void write(final int b)
            throws IOException {

    }

    @Override
    public void write(final byte[] b)
            throws IOException {

    }

    @Override
    public void write(final byte[] b, final int off, final int len)
            throws IOException {

    }

    @Override
    public void writeBoolean(final boolean v)
            throws IOException {

    }

    @Override
    public void writeByte(final int v)
            throws IOException {

    }

    @Override
    public void writeShort(final int v)
            throws IOException {

    }

    @Override
    public void writeChar(final int v)
            throws IOException {

    }

    @Override
    public void writeInt(final int v)
            throws IOException {

    }

    @Override
    public void writeLong(final long v)
            throws IOException {

    }

    @Override
    public void writeFloat(final float v)
            throws IOException {

    }

    @Override
    public void writeDouble(final double v)
            throws IOException {

    }

    @Override
    public void writeBytes(final String s)
            throws IOException {

    }

    @Override
    public void writeChars(final String s)
            throws IOException {

    }

    @Override
    public void writeUTF(final String s)
            throws IOException {

    }
}
