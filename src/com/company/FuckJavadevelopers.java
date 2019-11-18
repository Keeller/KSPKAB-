package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class FuckJavadevelopers extends BufferedReader {

    int size = 8192;

    public FuckJavadevelopers(Reader in) {
        super(in);
    }

    public FuckJavadevelopers(Reader in, int sz) {
        super(in, sz);
        this.size = sz;
    }

    @Override
    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder(this.size);
        for (Integer read = super.read(); read >= 0 && read != '\n' && read!=null; read = super.read()) {
            sb.append((char) read.intValue());
        }
        // in case you want also to preserve the line feed character
        // sb.append('\n');
        return sb.toString();
    }
}
