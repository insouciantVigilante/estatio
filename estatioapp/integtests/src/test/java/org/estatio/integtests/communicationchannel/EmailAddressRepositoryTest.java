/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
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
package org.estatio.integtests.communicationchannel;

import java.util.Iterator;
import java.util.SortedSet;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.module.communications.dom.impl.commchannel.CommunicationChannel;
import org.incode.module.communications.dom.impl.commchannel.CommunicationChannelRepository;
import org.incode.module.communications.dom.impl.commchannel.CommunicationChannelType;
import org.incode.module.communications.dom.impl.commchannel.EmailAddress;
import org.incode.module.communications.dom.impl.commchannel.EmailAddressRepository;
import org.estatio.dom.party.Party;
import org.estatio.dom.party.PartyRepository;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.fixture.party.OrganisationForTopModelGb;
import org.estatio.integtests.EstatioIntegrationTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class EmailAddressRepositoryTest extends EstatioIntegrationTest {

    @Before
    public void setupData() {
        runFixtureScript(new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                executionContext.executeChild(this, new EstatioBaseLineFixture());
                executionContext.executeChild(this, new OrganisationForTopModelGb());
            }
        });
    }

    @Inject
    EmailAddressRepository emailAddressRepository;

    @Inject
    CommunicationChannelRepository communicationChannelRepository;

    @Inject
    PartyRepository partyRepository;

    Party party;

    CommunicationChannel communicationChannel;

    EmailAddress emailAddress;

    @Before
    public void setUp() throws Exception {
        party = partyRepository.findPartyByReference(OrganisationForTopModelGb.REF);
        SortedSet<CommunicationChannel> results = communicationChannelRepository.findByOwner(party);
        Iterator<CommunicationChannel> it = results.iterator();
        while (it.hasNext()) {
            CommunicationChannel next = it.next();
            if (next.getType() == CommunicationChannelType.EMAIL_ADDRESS) {
                emailAddress = (EmailAddress) next;
            }
        }

        assertThat(emailAddress.getEmailAddress(), is("info@topmodel.example.com"));
    }

    public static class FindByEmailAddress extends EmailAddressRepositoryTest {

        @Test
        public void happyCase() throws Exception {
            // when
            EmailAddress email = emailAddressRepository.findByEmailAddress(party, emailAddress.getEmailAddress());

            // then
            assertThat(email, is(emailAddress));
        }
    }
}