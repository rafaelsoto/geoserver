/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.ppio;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Used to represent geometries in WKT format
 * 
 * @author Andrea Aime - OpenGeo
 */
public class WKTPPIO extends CDataPPIO {

    protected WKTPPIO() {
        super(Geometry.class, Geometry.class, "application/wkt");
    }

    @Override
    public Object decode(InputStream input) throws Exception {
        return new WKTReader().read(new InputStreamReader(input));
    }

    @Override
    public Object decode(String input) throws Exception {
        return new WKTReader().read(new StringReader(input));
    }

    @Override
    public void encode(Object value, OutputStream os) throws IOException {
        Writer w = new OutputStreamWriter(os);
        try {
            new WKTWriter().write((Geometry) value, w);
        } finally {
            w.flush();
        }
    }
    
    @Override
    public String getFileExtension() {
        return "wkt";
    }

}
