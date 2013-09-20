/*
 *
 *  Copyright 2012-2013 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.dom.charge;

import java.util.List;

import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;

import org.estatio.dom.EstatioDomainService;
import org.estatio.dom.utils.StringUtils;
import org.estatio.dom.utils.ValueUtils;

public class ChargeGroups extends EstatioDomainService<ChargeGroup> {

    public ChargeGroups() {
        super(ChargeGroups.class, ChargeGroup.class);
    }

    // //////////////////////////////////////

    @ActionSemantics(Of.SAFE)
    @MemberOrder(name="Other", sequence = "chargeAndChargeGroups.1.1")
    public List<ChargeGroup> allChargeGroups() {
        return allInstances();
    }

    @ActionSemantics(Of.NON_IDEMPOTENT)
    @MemberOrder(name="Other", sequence = "chargeAndChargeGroups.1.2")
    public List<ChargeGroup> newChargeGroup(
            final @Named("Reference") String reference, 
            final @Named("Description") String description) {
        createChargeGroup(reference, description);
        return allChargeGroups();
    }
    
    // //////////////////////////////////////

    @Programmatic
    public ChargeGroup createChargeGroup(final String reference, final String description) {
        final ChargeGroup chargeGroup = newTransientInstance();
        chargeGroup.setReference(reference);
        chargeGroup.setDescription(ValueUtils.coalesce(description, reference));
        persist(chargeGroup);
        return chargeGroup;
    }
    
    @Programmatic
    public ChargeGroup findChargeGroup(
            final String reference) {
        String regex = StringUtils.wildcardToRegex(reference);
        return firstMatch("findByReference", "reference", regex);
    }


}
