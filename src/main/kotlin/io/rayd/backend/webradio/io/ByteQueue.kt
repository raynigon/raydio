package io.rayd.backend.webradio.io

import java.nio.BufferOverflowException
import kotlin.math.min

/**
 * ByteQueue implementations queues bytes
 *
 * This implementation assumes that the read operation is more time critical than the write operation.
 * Therefore the internal buffer is shrunk on write instead of read.
 *
 * Default Size: 16KB
 * */
class ByteQueue(size: Int = 16 * 1024) {

    private var writePtr = 0
    private var readPtr = 0
    private var buffer = ByteArray(size)

    @Synchronized
    fun write(data: ByteArray, offset: Int, length: Int) {
        // Move existing Buffer, so the readPtr is zero
        val remaining = writePtr - readPtr
        System.arraycopy(buffer, readPtr, buffer, 0, remaining)
        writePtr = remaining
        readPtr = 0
        // Copy the new content
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
        return size
    }

    fun available(): Int {
        return writePtr - readPtr
    }
}
