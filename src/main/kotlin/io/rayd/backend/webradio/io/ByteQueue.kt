package io.rayd.backend.webradio.io

import java.nio.BufferOverflowException
import kotlin.math.min

class ByteQueue(size: Int = 0) {

    private var writePtr = 0
    private var readPtr = 0
    private var buffer = ByteArray(size)

    @Synchronized
    fun write(data: ByteArray, offset: Int, length: Int) {
        val newWritePtr = writePtr + length
        if (newWritePtr > buffer.size) throw BufferOverflowException()
        System.arraycopy(data, offset, buffer, writePtr, length)
        writePtr = newWritePtr
    }

    @Synchronized
    fun read(data: ByteArray, offset: Int, length: Int): Int {
        val size = min(writePtr - readPtr, length)
        System.arraycopy(buffer, readPtr, data, offset, size)
        readPtr += size
        val remaining = writePtr - readPtr
        System.arraycopy(buffer, readPtr, buffer, 0, remaining)
        writePtr = remaining
        readPtr = 0
        return size
    }

    fun available(): Int {
        return writePtr - readPtr
    }
}
