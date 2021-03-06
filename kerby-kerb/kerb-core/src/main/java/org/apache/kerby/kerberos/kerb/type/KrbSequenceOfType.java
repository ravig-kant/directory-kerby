/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.kerby.kerberos.kerb.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.kerby.asn1.type.Asn1SequenceOf;
import org.apache.kerby.asn1.type.Asn1String;
import org.apache.kerby.asn1.type.Asn1Type;

/**
 * A class that represents a SequenceOf Kerberos elements. 
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 *
 * @param <T> The KrbSequence type
 */
public class KrbSequenceOfType<T extends Asn1Type> extends Asn1SequenceOf<T> {
    /**
     * @return A String List of the Kerberos Elements in this sequenceOf.
     */
    public List<String> getAsStrings() {
        List<T> elements = getElements();
        
        // may be spurious... Careful check of the elements nullity.
        if (elements == null) {
            return new ArrayList<String>();
        }
        
        List<String> results = new ArrayList<>(elements.size());
        
        for (T ele : elements) {
            if (ele instanceof Asn1String) {
                results.add(((Asn1String) ele).getValue());
            } else {
                throw new RuntimeException("The targeted field type isn't of string");
            }
        }
        
        return results;
    }
}
