/**
 * Copyright (C) 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youkol.support.shiro.io;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.util.ByteSource;

/**
 * Source code copy from {@link org.apache.shiro.util.SimpleByteSource SimpleByteSource} and implementing {@link java.io.Serializable Serializable}.
 * <p>When use spring-data-redis for shiro cache, it will throw the following exception:
 * {@code java.io.NotSerializableException: org.apache.shiro.util.SimpleByteSource }
 *
 * @author jackiea
 * @see org.apache.shiro.util.SimpleByteSource
 */
public class CustomByteSource implements ByteSource, Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    public CustomByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public CustomByteSource(char[] chars) {
        this.bytes = CodecSupport.toBytes(chars);
    }

    public CustomByteSource(String string) {
        this.bytes = CodecSupport.toBytes(string);
    }

    public CustomByteSource(ByteSource source) {
        this.bytes = source.getBytes();
    }

    public CustomByteSource(File file) {
        this.bytes = new BytesHelper().getBytes(file);
    }

    public CustomByteSource(InputStream stream) {
        this.bytes = new BytesHelper().getBytes(stream);
    }

    public static boolean isCompatible(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String ||
                o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    public String toHex() {
        if ( this.cachedHex == null ) {
            this.cachedHex = Hex.encodeToString(getBytes());
        }
        return this.cachedHex;
    }

    public String toBase64() {
        if ( this.cachedBase64 == null ) {
            this.cachedBase64 = Base64.encodeToString(getBytes());
        }
        return this.cachedBase64;
    }

    public String toString() {
        return toBase64();
    }

    public int hashCode() {
        if (this.bytes == null || this.bytes.length == 0) {
            return 0;
        }
        return Arrays.hashCode(this.bytes);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource) o;
            return Arrays.equals(getBytes(), bs.getBytes());
        }
        return false;
    }

    private static final class BytesHelper extends CodecSupport {
        public byte[] getBytes(File file) {
            return toBytes(file);
        }

        public byte[] getBytes(InputStream stream) {
            return toBytes(stream);
        }
    }
}
