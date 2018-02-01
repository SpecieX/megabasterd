package com.tonikelope.megabasterd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import static java.lang.String.valueOf;

/**
 *
 * @author tonikelope
 */
public final class Chunk {

    private final long _id;
    private final long _offset;
    private final long _size;
    private final ByteArrayOutputStream _data_os;
    private final String _url;
    private final int _size_multi;

    public Chunk(long id, long file_size, String file_url) throws ChunkInvalidException {
        _size_multi = 1;

        _id = id;

        _offset = calculateOffset();

        if (file_size > 0) {
            if (_offset >= file_size) {
                throw new ChunkInvalidException(valueOf(id));
            }

        } else {

            if (id > 1) {

                throw new ChunkInvalidException(valueOf(id));
            }
        }

        _size = calculateSize(file_size);

        _url = file_url != null ? file_url + "/" + _offset + "-" + (_offset + _size - 1) : null;

        _data_os = new ByteArrayOutputStream((int) _size);
    }

    public Chunk(long id, long file_size, String file_url, int size_multi) throws ChunkInvalidException {
        _size_multi = size_multi;

        _id = id;

        _offset = calculateOffset();

        if (file_size > 0) {
            if (_offset >= file_size) {
                throw new ChunkInvalidException(valueOf(id));
            }

        } else {

            if (id > 1) {

                throw new ChunkInvalidException(valueOf(id));
            }
        }

        _size = calculateSize(file_size);

        _url = file_url != null ? file_url + "/" + _offset + "-" + (_offset + _size - 1) : null;

        _data_os = new ByteArrayOutputStream((int) _size);
    }

    public int getSize_multi() {
        return _size_multi;
    }

    public long getOffset() {
        return _offset;
    }

    public ByteArrayOutputStream getOutputStream() {
        return _data_os;
    }

    public long getId() {
        return _id;
    }

    public long getSize() {
        return _size;
    }

    public String getUrl() {
        return _url;
    }

    public ByteArrayInputStream getInputStream() {
        return new ByteArrayInputStream(_data_os.toByteArray());
    }

    private long calculateSize(long file_size) {
        long chunk_size = (_id >= 1 && _id <= 7) ? _id * 128 * 1024 : 1024 * 1024 * _size_multi;

        if (_offset + chunk_size > file_size) {
            chunk_size = file_size - _offset;
        }

        return chunk_size;
    }

    private long calculateOffset() {
        long[] offs = {0, 128, 384, 768, 1280, 1920, 2688};

        return (_id <= 7 ? offs[(int) _id - 1] : (3584 + (_id - 8) * 1024 * _size_multi)) * 1024;
    }

}