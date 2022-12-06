package ir.proprog.enrollassist.domain.enrollmentList;

import ir.proprog.enrollassist.domain.EnrollmentRules.EnrollmentRuleViolation;
import ir.proprog.enrollassist.domain.EnrollmentRules.ExamTimeCollision;
import ir.proprog.enrollassist.domain.EnrollmentRules.MinCreditsRequiredNotMet;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.major.Major;
import ir.proprog.enrollassist.domain.program.Program;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.domain.studyRecord.Grade;
import org.checkerframework.checker.units.qual.C;
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
    public static Student mockStudent;
    public static Course mockCourse1;
    public static Course mockCourse2;
    public static Course mockCourse3;
    public static Course mockCourse4;

    public static void setUp() throws Exception {
        mockStudent = mock(Student.class);
        mockSection1 = mock(Section.class);
        when(mockSection1.getExamTime()).thenReturn(new ExamTime("2022-11-10T09:00", "2022-11-10T11:00"));
        mockCourse1 = mock(Course.class);
        when(mockSection1.getCourse()).thenReturn(mockCourse1);
        mockSection2 = mock(Section.class);
        when(mockSection2.getExamTime()).thenReturn(new ExamTime("2022-11-10T09:00", "2022-11-10T11:00"));
        mockCourse2 = mock(Course.class);
        when(mockSection2.getCourse()).thenReturn(mockCourse2);
        mockSection3 = mock(Section.class);
        when(mockSection3.getExamTime()).thenReturn(new ExamTime("2022-11-11T09:00", "2022-11-11T11:00"));
        mockCourse3 = mock(Course.class);
        when(mockSection3.getCourse()).thenReturn(mockCourse3);
        mockSection4 = mock(Section.class);
        when(mockSection4.getExamTime()).thenReturn(null);
        mockCourse4 = mock(Course.class);
        when(mockSection4.getCourse()).thenReturn(mockCourse4);
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

    @Test
    public void ifStudentIsMasterAndCreditsSumIsValidCreditCheckValidGPALimitShouldTrue() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(13));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.Masters);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(1);
        enrollmentList.addSections(mockSection3);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(0, violations.size());
    }

    @Test
    public void ifStudentIsMasterAndCreditsSumIsLessThanMinLevelCreditCheckValidGPALimitShouldFalse() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(13));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.Masters);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(1);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(1);
        enrollmentList.addSections(mockSection3);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Minimum number of credits(8) is not met.", violations.get(0).toString());
    }

    @Test
    public void ifStudentIsMasterAndCreditsSumIsMoreThanMaxLevelCreditCheckValidGPALimitShouldFalse() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(13));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.Masters);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(5);
        enrollmentList.addSections(mockSection3);
        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Maximum number of credits(12) exceeded.", violations.get(0).toString());
    }

    @Test
    public void ifStudentIsPHDAndCreditsSumIsValidCreditCheckValidGPALimitShouldTrue() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(13));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.PHD);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(1);
        enrollmentList.addSections(mockSection3);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(0, violations.size());
    }

    @Test
    public void ifStudentIsPHDAndCreditsSumIsLessThanMinLevelCreditCheckValidGPALimitShouldFalse() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(13));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.PHD);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse1.getCredits()).thenReturn(1);
        enrollmentList.addSections(mockSection2);
        when(mockCourse1.getCredits()).thenReturn(1);
        enrollmentList.addSections(mockSection3);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Minimum number of credits(6) is not met.", violations.get(0).toString());
    }

    @Test
    public void ifStudentIsPHDAndCreditsSumIsMoreThanMaxLevelCreditCheckValidGPALimitShouldFalse() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(13));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.PHD);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(5);
        enrollmentList.addSections(mockSection3);
        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Maximum number of credits(12) exceeded.", violations.get(0).toString());
    }

    @Test
    public void ifStudentIsUnderGradeAndCreditsSumIsValidCreditCheckValidGPALimitShouldTrue() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(16));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(5);
        enrollmentList.addSections(mockSection3);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(0, violations.size());
    }

    @Test
    public void ifStudentIsUnderGradeAndCreditsSumIsLessThanMinLevelCreditCheckValidGPALimitShouldFalse() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(13));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse1.getCredits()).thenReturn(1);
        enrollmentList.addSections(mockSection2);
        when(mockCourse1.getCredits()).thenReturn(1);
        enrollmentList.addSections(mockSection3);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Minimum number of credits(12) is not met.", violations.get(0).toString());
    }

    @Test
    public void ifStudentIsUnderGradeAndCreditsSumIsMoreThanMaxLevelCreditCheckValidGPALimitShouldFalse() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(20));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(5);
        enrollmentList.addSections(mockSection3);
        when(mockCourse4.getCredits()).thenReturn(12);
        enrollmentList.addSections(mockSection4);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Maximum number of credits(24) exceeded.", violations.get(0).toString());
    }

    @Test
    public void ifStudentIsUnderGradeAndGPAIsLessThan12AndCreditsSumIsMoreThanMaxValidCreditCheckValidGPALimitShouldFalse() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(11));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection3);
        when(mockCourse4.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection4);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Maximum number of credits(14) exceeded.", violations.get(0).toString());
    }

    @Test
    public void ifStudentIsUnderGradeAndGPAIsLessThan17AndCreditsSumIsMoreThanMaxValidCreditCheckValidGPALimitShouldFalse() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade(16));
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection3);
        when(mockCourse4.getCredits()).thenReturn(9);
        enrollmentList.addSections(mockSection4);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Maximum number of credits(20) exceeded.", violations.get(0).toString());
    }

    @Test
    public void ifStudentIsUnderGradeAndCreditsSumIsMoreThanMaxCreditCheckAndGPAIsZeroValidGPALimitShouldFalse() throws Exception{
        // Arrange
        when(mockStudent.calculateGPA()).thenReturn(new Grade());
        when(mockStudent.getTotalTakenCredits()).thenReturn(0);
        when(mockStudent.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        EnrollmentList enrollmentList = new EnrollmentList("EnrollmentListTest", mockStudent);
        when(mockCourse1.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection1);
        when(mockCourse2.getCredits()).thenReturn(4);
        enrollmentList.addSections(mockSection2);
        when(mockCourse3.getCredits()).thenReturn(5);
        enrollmentList.addSections(mockSection3);
        when(mockCourse4.getCredits()).thenReturn(8);
        enrollmentList.addSections(mockSection4);

        // Act
        List<EnrollmentRuleViolation> violations = enrollmentList.checkValidGPALimit();

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Maximum number of credits(20) exceeded.", violations.get(0).toString());
    }
}