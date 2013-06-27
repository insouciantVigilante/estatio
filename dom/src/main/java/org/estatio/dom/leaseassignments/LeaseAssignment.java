package org.estatio.dom.leaseassignments;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.MemberOrder;

import org.estatio.dom.EstatioTransactionalObject;
import org.estatio.dom.lease.Lease;

//TODO: is this in scope?
//@javax.jdo.annotations.PersistenceCapable
//@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
public class LeaseAssignment extends EstatioTransactionalObject<LeaseAssignment> {

    
    public LeaseAssignment() {
        // TODO: I made this up...
        super("nextLease,assignmentDate");
    }
    
    // //////////////////////////////////////

    @javax.jdo.annotations.Column(name="PREVIOUSLEASE_ID")
    @javax.jdo.annotations.Persistent(mappedBy="nextLease")
    private Lease previousLease;

    @MemberOrder(sequence = "1")
    public Lease getPreviousLease() {
        return previousLease;
    }

    public void setPreviousLease(final Lease previousLease) {
        this.previousLease = previousLease;
    }

    // //////////////////////////////////////

    @javax.jdo.annotations.Column(name="NEXTLEASE_ID")
    private Lease nextLease;

    @MemberOrder(sequence = "1")
    public Lease getNextLease() {
        return nextLease;
    }

    public void setNextLease(final Lease nextLease) {
        this.nextLease = nextLease;
    }

    // //////////////////////////////////////

    private LocalDate assignmentDate;

    @MemberOrder(sequence = "1")
    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(final LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    // //////////////////////////////////////

    private LeaseAssignmentType assignmentType;

    @MemberOrder(sequence = "1")
    public LeaseAssignmentType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(final LeaseAssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }


}