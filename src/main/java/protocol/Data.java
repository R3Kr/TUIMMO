package protocol;

import java.io.IOException;

/**
 * The Data interface represents a data structure that can be written from its implementation state to a byte array
 * and read from a byte array to populate its implementation state.
 */
public interface Data {
    /**
     * Writes the data from the byte array to object state.
     *
     * @param bytes The byte array to which the data will be written from.
     * @throws IOException If an I/O error occurs while writing the data.
     */
    void write(byte[] bytes) throws IOException;

    /**
     * Reads the data from object state and return the byte array.
     *
     * @return The byte array containing the read data.
     * @throws IOException If an I/O error occurs while reading the data.
     */
    byte[] read() throws IOException;

    DataType getDataType();
}