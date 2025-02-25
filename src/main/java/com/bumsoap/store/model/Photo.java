package com.bumsoap.store.model;

import java.sql.Blob;

public class Photo {
    long id;
    String fileType;
    String fileName;
    Blob image;
}
