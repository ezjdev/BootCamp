package com.colvir.bootcamp.homework5.config;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

import java.sql.Time;

public class SqlTimeCompactSerializer implements CompactSerializer<Time> {

    @Override
    public Time read(CompactReader reader) {
        long millis = reader.readInt64("time");
        return new Time(millis);
    }

    @Override
    public void write(CompactWriter writer, Time time) {
        writer.writeInt64("time", time.getTime());
    }

    @Override
    public String getTypeName() {
        return "java.sql.Time";
    }

    @Override
    public Class<Time> getCompactClass() {
        return Time.class;
    }
}