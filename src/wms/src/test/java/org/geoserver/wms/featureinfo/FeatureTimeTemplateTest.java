/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.featureinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

public class FeatureTimeTemplateTest extends WMSTestSupport {

    static SimpleFeature feature;
   
    
    @Before
    public void findFeature() throws Exception {
           
        SimpleFeatureSource source = getFeatureSource(MockData.PRIMITIVEGEOFEATURE);
        SimpleFeatureCollection features = source.getFeatures();
        FeatureIterator <SimpleFeature> iterator = features.features();
        while( iterator.hasNext() ) {
            SimpleFeature f = iterator.next();
            if ( f.getAttribute("dateProperty") != null ) {
                feature = f;
                break;
            }
        }
        features.close(iterator);
    }
    
    @Test 
    public void testEmpty() throws Exception {
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 0, result.length );
    }
    
    @Test
    public void testTimestamp() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE,"time.ftl","${dateProperty.value}");
        
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 1, result.length );
        assertNotNull( result[0] );
    }
    
    @Test
    public void testTimeSpan() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE,"time.ftl","${dateProperty.value}||${dateProperty.value}");
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 2, result.length );
        assertNotNull( result[0] );
        assertNotNull( result[1] );
    }
    
    @Test
    public void testTimeSpanOpenEndedStart() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE,"time.ftl","||${dateProperty.value}");
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 2, result.length );
        assertNull( result[0] );
        assertNotNull( result[1] );
    }
    
    @Test
    public void testTimeSpanOpenEndedEnd() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE,"time.ftl","${dateProperty.value}||");
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 2, result.length );
        assertNotNull( result[0] );
        assertNull( result[1] );
    }
}
