/* Copyright (c) 2012 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.csw.store.simple;

import java.util.Iterator;
import java.util.List;

import org.geoserver.csw.feature.AbstractFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;

/**
 * Very basic retyper, can only shave off root attributes and does not really reduce
 * the feature type, but only the attributes in the returned features. 
 * 
 * @author Andrea Aime - GeoSolutions
 */
class RetypingFeatureCollection extends AbstractFeatureCollection<FeatureType, Feature> {
    
    FeatureCollection delegate;
    List<PropertyName> properties;
    
    public RetypingFeatureCollection(FeatureCollection delegate, List<PropertyName> properties) {
        super(delegate.getSchema());
        this.delegate = delegate;
        this.properties = properties;
    }


    @Override
    public FeatureCollection<FeatureType, Feature> subCollection(Filter filter) {
        FeatureCollection subCollection = delegate.subCollection(filter);
        return new RetypingFeatureCollection(subCollection, properties);
    }

    @Override
    public FeatureCollection<FeatureType, Feature> sort(SortBy order) {
        FeatureCollection sorted = delegate.sort(order);
        return new RetypingFeatureCollection(sorted, properties);
    }

    @Override
    protected Iterator<Feature> openIterator() {
        return new RetypingIterator(delegate.iterator(), schema, properties);
    }

    @Override
    protected void closeIterator(Iterator<Feature> close) {
        if(close instanceof RetypingIterator) {
            delegate.close(((RetypingIterator) close).delegate);
        }
    }
}
