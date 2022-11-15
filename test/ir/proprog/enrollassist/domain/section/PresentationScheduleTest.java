package ir.proprog.enrollassist.domain.section;

import ir.proprog.enrollassist.Exception.ExceptionList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;

public class PresentationScheduleTest {

    public PresentationSchedule presentationSchedule;

    @Before
    public void setUp() throws ExceptionList{
        presentationSchedule = new PresentationSchedule("Saturday", "10:00", "11:30");
    }
    @Test
    public void twoEqualPresentationsShouldHaveConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "10:00", "11:30");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertTrue(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void twoPresentationWithDifferentDayOfWeakShouldNotHaveConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Monday", "10:00", "11:30");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifEndOfOtherIsAfterStartBeforeEndOfPresentationShouldHaveConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "09:30", "11:00");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertTrue(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifEndOfOtherIsEqualToStartOfOfPresentationShouldNotHasConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "09:00", "10:00");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifEndOfOtherIsBeforeStartOfPresentationShouldNotHaveConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "08:00", "9:30");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifStartOfOtherAndStartOfPresentationIsEqualButEndsNotEqualShouldHasConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "10:00", "11:00");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertTrue(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifStartAndEndOfOtherIsBetweenStartAndEndOfPresentationShouldHasConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "10:30", "11:00");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertTrue(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifEndOfOtherAndEndOfPresentationIsEqualButStartsNotEqualShouldHasConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "10:30", "11:30");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertTrue(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifStartOfOtherIsBeforeEndAndAfterStartOfPresentationShouldHaveConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "10:30", "12:00");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertTrue(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifStartOfOtherIsEqualToEndOfOfPresentationShouldNotHasConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "11:30", "13:00");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifStartOfOtherIsAfterEndOfPresentationShouldNotHaveConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "12:00", "13:30");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void ifStartAndEndOfPresentationIsBetweenStartAndEndOfOtherShouldHasConflict() throws ExceptionList{
        // Arrange
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "9:30", "12:00");

        // Act
        presentationSchedule.hasConflict(otherPresentationSchedule);

        // Assert
        Assert.assertTrue(presentationSchedule.hasConflict(otherPresentationSchedule));
    }
}