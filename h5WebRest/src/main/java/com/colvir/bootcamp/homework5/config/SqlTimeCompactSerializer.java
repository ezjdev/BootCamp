package com.colvir.bootcamp.homework5.config;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

import java.sql.Time;

public class SqlTimeCompactSerializer implements CompactSerializer<Time> {

    public static final String TIME = "time";

    @Override
    public Time read(CompactReader reader) {
        return new Time( reader.readInt64(TIME));
    }

    @Override
    public void write(CompactWriter writer, Time time) {
        writer.writeInt64(TIME, time.getTime());
    }

    @Override
    public String getTypeName() {
        return Time.class.getCanonicalName();
    }

    @Override
    public Class<Time> getCompactClass() {
        return Time.class;
    }
}