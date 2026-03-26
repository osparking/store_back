package com.bumsoap.store.data;

import com.bumsoap.store.util.BsShape;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BsDataSupplier {
    private static final List<String> soapShapeLabels;
    static {
        soapShapeLabels = new ArrayList<>(BsShape.values().length);
        for (BsShape shape : BsShape.values()) {
            soapShapeLabels.add(shape.label);
        }
    }

    public List<String> getSoapShapeLabels() {
        return soapShapeLabels;
    }
}
