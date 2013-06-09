package org.estatio.appsettings;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.settings.ApplicationSetting;
import org.apache.isis.applib.services.settings.ApplicationSettingsService;
import org.apache.isis.applib.services.settings.SettingAbstract;
import org.apache.isis.applib.services.settings.SettingType;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.ClassUnderTest;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import org.estatio.services.appsettings.EstatioSettingsService;

public class EstatioSettingsServiceTest {

    public static class EstatioSettingsServiceForTesting extends EstatioSettingsService {
        
        @Override
        public void updateEpochDate(LocalDate epochDate) {
        }
        ApplicationSettingsService getApplicationSettings() {
            return applicationSettings;
        }
    }

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    private ApplicationSettingsService  mockApplicationSettingsService;
    
    @ClassUnderTest
    private EstatioSettingsServiceForTesting estatioSettingsService;

    static class ApplicationSettingForTesting extends SettingAbstract implements ApplicationSetting {
        private String valueRaw;
        private SettingType type;
        public ApplicationSettingForTesting(String valueRaw, SettingType type) {
            this.valueRaw = valueRaw;
            this.type = type;
        }
        public String getKey() {
            return null;
        }
        public String getDescription() {
            return null;
        }
        public SettingType getType() {
            return type;
        }
        public String getValueRaw() {
            return valueRaw;
        }
    }
    
    @Test
    public void happyCase() {
        final LocalDate date = new LocalDate(2013,4,1);
        context.checking(new Expectations() {
            {
                oneOf(mockApplicationSettingsService).find(EstatioSettingsService.EPOCH_DATE_KEY);
                will(returnValue(new ApplicationSettingForTesting(date.toString(SettingAbstract.DATE_FORMATTER), SettingType.LOCAL_DATE)));
            }
        });
        final LocalDate fetchEpochDate = estatioSettingsService.fetchEpochDate();
        assertThat(fetchEpochDate, is(date));
    }

    @Test
    public void whenNull() {
        context.checking(new Expectations() {
            {
                oneOf(mockApplicationSettingsService).find(EstatioSettingsService.EPOCH_DATE_KEY);
                will(returnValue(null));
            }
        });
        final LocalDate fetchEpochDate = estatioSettingsService.fetchEpochDate();
        assertThat(fetchEpochDate, is(nullValue()));
    }

}
