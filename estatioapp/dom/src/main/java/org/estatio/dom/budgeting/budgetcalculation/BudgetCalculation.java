/*
 *
 *  Copyright 2012-2015 Eurocommercial Properties NV
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
package org.estatio.dom.budgeting.budgetcalculation;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.timestamp.Timestampable;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import org.incode.module.base.dom.utils.TitleBuilder;

import org.estatio.dom.UdoDomainObject2;
import org.estatio.dom.apptenancy.WithApplicationTenancyProperty;
import org.estatio.dom.budgeting.allocation.BudgetItemAllocation;
import org.estatio.dom.budgeting.budget.Budget;
import org.estatio.dom.budgeting.budgetitem.BudgetItem;
import org.estatio.dom.budgeting.keyitem.KeyItem;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
        ,schema = "dbo" // Isis' ObjectSpecId inferred from @DomainObject#objectType
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@javax.jdo.annotations.Queries({
        @Query(
                name = "findUnique", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.budgetcalculation.BudgetCalculation " +
                        "WHERE budgetItemAllocation == :budgetItemAllocation " +
                        "&& keyItem == :keyItem " +
                        "&& status == :status " +
                        "&& calculationType == :calculationType"),
        @Query(
                name = "findByBudgetItemAllocationAndCalculationType", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.budgetcalculation.BudgetCalculation " +
                        "WHERE budgetItemAllocation == :budgetItemAllocation " +
                        "&& calculationType == :calculationType"),
        @Query(
                name = "findByBudgetItemAllocationAndStatus", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.budgetcalculation.BudgetCalculation " +
                        "WHERE budgetItemAllocation == :budgetItemAllocation " +
                        "&& status == :status"),
        @Query(
                name = "findByBudgetItemAllocationAndStatusAndCalculationType", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.budgetcalculation.BudgetCalculation " +
                        "WHERE budgetItemAllocation == :budgetItemAllocation " +
                        "&& status == :status && calculationType == :calculationType"),
        @Query(
                name = "findByBudgetItemAllocation", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.budgetcalculation.BudgetCalculation " +
                        "WHERE budgetItemAllocation == :budgetItemAllocation")
})
@Unique(name = "BudgetCalculation_budgetItemAllocation_keyItem_calculationType_status_UNQ", members = {"budgetItemAllocation", "keyItem", "calculationType", "status"})
@DomainObject(
        auditing = Auditing.DISABLED,
        publishing = Publishing.DISABLED,
        objectType = "org.estatio.dom.budgeting.budgetcalculation.BudgetCalculation"
)
public class BudgetCalculation extends UdoDomainObject2<BudgetCalculation>
        implements WithApplicationTenancyProperty, Timestampable {

    public BudgetCalculation() {
        super("budgetItemAllocation, keyItem");
    }

    public String title(){
        return TitleBuilder
                .start()
                .withName("Calculation - ")
                .withName(getValue())
                .toString();
    }

    @Getter @Setter
    @Column(allowsNull = "false", scale = 2)
    private BigDecimal value;

    @Getter @Setter
    @Column(allowsNull = "false", name="budgetItemAllocationId")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    private BudgetItemAllocation budgetItemAllocation;

    @Getter @Setter
    @Column(allowsNull = "false", name="keyItemId")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    private KeyItem keyItem;

    @Getter @Setter
    @Column(allowsNull = "false")
    private BudgetCalculationType calculationType;

    @Getter @Setter
    @Column(allowsNull = "false")
    private BudgetCalculationStatus status;

    @Override
    @PropertyLayout(hidden = Where.EVERYWHERE)
    public ApplicationTenancy getApplicationTenancy() {
        return getBudgetItemAllocation().getApplicationTenancy();
    }

    @Getter @Setter
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.ALL_TABLES)
    private Timestamp updatedAt;

    @Getter @Setter
    @PropertyLayout(hidden = Where.ALL_TABLES)
    @Column(allowsNull = "true")
    private String updatedBy;

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION, hidden = Where.ALL_TABLES)
    public Budget getBudget(){
        return getBudgetItemAllocation().getBudget();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION, hidden = Where.ALL_TABLES)
    public BudgetItem getBudgetItem(){
        return getBudgetItemAllocation().getBudgetItem();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION, hidden = Where.ALL_TABLES)
    public BigDecimal getValueForBudgetPeriod() {
        return getValue().multiply(getAnnualFactor());
    }

    @Programmatic
    public void remove(){
        getContainer().remove(this);
    }

    @Programmatic
    public BigDecimal getAnnualFactor(){

        return getBudget().getAnnualFactor();

    }

}
