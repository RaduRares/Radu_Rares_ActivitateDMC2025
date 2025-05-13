package com.example.app_bun;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class DamAppendLaFile extends ObjectOutputStream {
    public DamAppendLaFile(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {

        reset();
    }
}
