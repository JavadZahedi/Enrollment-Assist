package ir.proprog.enrollassist.domain.enrollmentList;

import ir.proprog.enrollassist.domain.EnrollmentRules.EnrollmentRuleViolation;
import ir.proprog.enrollassist.domain.EnrollmentRules.ExamTimeCollision;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.major.Major;
import ir.proprog.enrollassist.domain.program.Program;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

import org.junit.*;
import org.mockito.configuration.IMockitoConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnrollmentListTest {
    public static Section mockSection1;
    public static Section mockSection2;
    public static Section mockSection3;
    public static Section mockSection4;

    public static void setUp() throws Exception {
        mockSection1 = mock(Section.class);
        when(mockSection1.getExamTime()).thenReturn(new ExamTime("2022-11-10T09:00", "2022-11-10T11:00"));
        mockSection2 = mock(Section.class);
        when(mockSection2.getExamTime()).thenReturn(new ExamTime("2022-11-10T09:00", "2022-11-10T11:00"));
        mockSection3 = mock(Section.class);
        when(mockSection3.getExamTime()).thenReturn(new ExamTime("2022-11-11T09:00", "2022-11-11T11:00"));
        mockSection4 = mock(Section.class);
        when(mockSection4.getExamTime()).thenReturn(null);
    }

    static {
        try{
            setUp();
        }catch (Exception e){}
    }

    @Test
    public void ifOneSectionIsNullAndOtherSectionHaveNotConflictCheckExamTimeShouldNotHasConflictError() {
        // Arrange
        Student mockStudent = mock(Student.class);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        enrollmentList.addSections(mockSection2);
        enrollmentList.addSections(mockSection3);
        enrollmentList.addSections(mockSection4);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkExamTimeConflicts();

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    public void ifSectionHaveExamConflictCheckExamTimeShouldHasConflictError() {
        // Arrange
        Student mockStudent = mock(Student.class);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        enrollmentList.addSections(mockSection1);
        enrollmentList.addSections(mockSection2);
        enrollmentList.addSections(mockSection3);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkExamTimeConflicts();

        // Assert
        assertEquals(1, violations.size());
        assertEquals(new ExamTimeCollision(mockSection1, mockSection2), violations.get(0));
    }
}