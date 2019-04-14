package com.marjina.csvreader.batch;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;

public class CustomLineSeparatorPolicy extends SimpleRecordSeparatorPolicy {
    @Override
    public boolean isEndOfRecord(final String line) {
        return line.trim().length() != 0 && super.isEndOfRecord(line);
    }

    @Override
    public String postProcess(final String record) {
        String[] str=record.split(",");
        if (record == null || record.trim().length() == 0 || str.length>10 ) {
            return null;
        }

        return super.postProcess(record);
    }
}
