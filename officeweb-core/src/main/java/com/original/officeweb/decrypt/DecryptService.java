package com.original.officeweb.decrypt;

public interface DecryptService {
    String decrypt(String data) throws Exception;

    DecryptModel decryptModel();
}
