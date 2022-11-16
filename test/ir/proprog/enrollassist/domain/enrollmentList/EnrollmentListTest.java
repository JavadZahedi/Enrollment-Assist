package ir.proprog.enrollassist.domain.enrollmentList;

import ir.proprog.enrollassist.domain.EnrollmentRules.EnrollmentRuleViolation;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(Parameterized.class)
public class EnrollmentListTest {
    public EnrollmentList enrollmentList;
    public int expectedViolationsSize;
    public String expectedViolationMessage;
    public static Program program;
    public static Course DS;
    public static Course CN;
    public static Course AP;
    public static Course OS;
    public static Course DA;
    public static Course CA;
    public static Course CAD;
    public static Course MATH;
    public static Course FLAT;
    public static Section DsSection;
    public static Section CnSection;
    public static Section ApSection;
    public static Section OsSection;
    public static Section DaSection;
    public static Section CaSection;
    public static Section CadSection;
    public static Section MathSection;
    public static Section FlatSection;
    public static Student student;

    public EnrollmentListTest(EnrollmentList enrollmentList, int expectedViolationsSize, String expectedViolationMessage) {
        this.enrollmentList = enrollmentList;
        this.expectedViolationsSize = expectedViolationsSize;
        this.expectedViolationMessage = expectedViolationMessage;
    }

    public static void setUp() throws Exception {
        program = new Program(new Major("1", "CE", "Engineering"), "Undergraduate", 1, 140, "Major");
        DS = new Course("0000001", "DS", 3, "Undergraduate");
        program.addCourse(DS);
        CN = new Course("0000002", "CN", 3, "Undergraduate");
        program.addCourse(CN);
        AP = new Course("0000004", "AP", 3, "Undergraduate");
        program.addCourse(AP);
        OS = new Course("0000005", "OS", 3, "Undergraduate");
        program.addCourse(OS);
        DA = new Course("0000006", "DA", 3, "Undergraduate");
        program.addCourse(DA);
        CA = new Course("0000007", "CA", 3, "Undergraduate").withPre(CN);
        program.addCourse(CA);
        CAD = new Course("0000008", "CAD", 3, "Undergraduate");
        program.addCourse(CAD);
        MATH = new Course("0000009", "MATH", 3, "Undergraduate");
        program.addCourse(MATH);
        FLAT = new Course("0000010", "FLAT", 3, "Undergraduate");
        program.addCourse(FLAT);
        student = new Student("000000000", "Undergraduate");
        student.addProgram(program);
        student.setGrade("00001", DS, 11);
        DsSection = new Section(DS, "0", new ExamTime("2022-11-10T09:00", "2022-11-10T11:00"),
                Collections.singleton(new PresentationSchedule("Saturday", "9:00", "11:00")));
        CnSection = new Section(CN, "1", new ExamTime("2022-11-11T09:00", "2022-11-11T11:00"),
                Collections.singleton(new PresentationSchedule("Sunday", "09:00", "11:00")));
        ApSection = new Section(AP, "3", new ExamTime("2022-11-13T09:00", "2022-11-13T11:00"),
                Collections.singleton(new PresentationSchedule("Tuesday", "09:00", "11:00")));
        OsSection = new Section(OS, "4", new ExamTime("2022-11-12T09:00", "2022-11-12T11:00"),
                Collections.singleton(new PresentationSchedule("Wednesday", "09:00", "11:00")));
        DaSection = new Section(DA, "5", new ExamTime("2022-11-30T09:00", "2022-11-30T11:00"),
                Collections.singleton(new PresentationSchedule("Sunday", "09:00", "11:00")));
        CaSection = new Section(CA, "6", new ExamTime("2022-11-20T09:00", "2022-11-20T11:00"),
                Collections.singleton(new PresentationSchedule("Thursday", "09:00", "11:00")));
        CadSection = new Section(CAD, "7", new ExamTime("2022-11-01T09:00", "2022-11-01T11:00"),
                Collections.singleton(new PresentationSchedule("Saturday", "014:00", "15:00")));
        MathSection = new Section(MATH, "8", new ExamTime("2022-11-01T01:00", "2022-11-01T02:00"),
                Collections.singleton(new PresentationSchedule("Saturday", "02:00", "3:00")));
        FlatSection = new Section(FLAT, "9", new ExamTime("2022-11-11T09:00", "2022-11-11T11:00"),
                Collections.singleton(new PresentationSchedule("Saturday", "02:00", "3:00")));
    }

    static {
        try{
            setUp();
        }catch (Exception e){}
    }

    @Parameters
    public static Collection<Object[]> parameters() throws Exception {
        EnrollmentList enrollmentListTrue = new EnrollmentList("EnrollmentListTest", student);
        enrollmentListTrue.addSections(CadSection);
        enrollmentListTrue.addSections(CnSection);
        enrollmentListTrue.addSections(ApSection);
        enrollmentListTrue.addSections(OsSection);

        EnrollmentList PrerequisiteNotTaken = new EnrollmentList("EnrollmentListTest", student);
        PrerequisiteNotTaken.addSections(CadSection);
        PrerequisiteNotTaken.addSections(CnSection);
        PrerequisiteNotTaken.addSections(CaSection);
        PrerequisiteNotTaken.addSections(OsSection);

        EnrollmentList RequestedCourseAlreadyPassed = new EnrollmentList("EnrollmentListTest", student);
        RequestedCourseAlreadyPassed.addSections(DsSection);
        RequestedCourseAlreadyPassed.addSections(CadSection);
        RequestedCourseAlreadyPassed.addSections(CnSection);
        RequestedCourseAlreadyPassed.addSections(ApSection);

        EnrollmentList CourseRequestedTwice = new EnrollmentList("EnrollmentListTest", student);
        CourseRequestedTwice.addSections(CadSection);
        CourseRequestedTwice.addSections(CnSection);
        CourseRequestedTwice.addSections(OsSection);
        CourseRequestedTwice.addSections(OsSection);

        EnrollmentList MaxCreditsLimitExceeded = new EnrollmentList("EnrollmentListTest", student);
        MaxCreditsLimitExceeded.addSections(CadSection);
        MaxCreditsLimitExceeded.addSections(CnSection);
        MaxCreditsLimitExceeded.addSections(ApSection);
        MaxCreditsLimitExceeded.addSections(OsSection);
        MaxCreditsLimitExceeded.addSections(MathSection);

        EnrollmentList ConflictOfClassSchedule = new EnrollmentList("EnrollmentListTest", student);
        ConflictOfClassSchedule.addSections(CadSection);
        ConflictOfClassSchedule.addSections(CnSection);
        ConflictOfClassSchedule.addSections(ApSection);
        ConflictOfClassSchedule.addSections(DaSection);

        EnrollmentList ExamTimeCollision = new EnrollmentList("EnrollmentListTest", student);
        ExamTimeCollision.addSections(CadSection);
        ExamTimeCollision.addSections(CnSection);
        ExamTimeCollision.addSections(ApSection);
        ExamTimeCollision.addSections(FlatSection);

        EnrollmentList MinCreditsRequiredNotMet = new EnrollmentList("EnrollmentListTest", student);

        return Arrays.asList(new Object[][]{
                {enrollmentListTrue, 0, ""},
                {PrerequisiteNotTaken, 1, "[0000002] CN is not passed as a prerequisite of [0000007] CA"},
                {RequestedCourseAlreadyPassed, 1, "[0000001] DS has been already passed"},
                {CourseRequestedTwice, 1, "[0000005] OS is requested to be taken twice"},
                {MaxCreditsLimitExceeded, 1, "Maximum number of credits(14) exceeded."},
                {MinCreditsRequiredNotMet, 1, "Minimum number of credits(12) is not met."},
                {ConflictOfClassSchedule, 1,
                        "ir.proprog.enrollassist.domain.section.Section@ba071fc1 course and ir.proprog.enrollassist.domain.section.Section@ba072041 course have conflict in schedule."},
                {ExamTimeCollision, 1,
                        "ir.proprog.enrollassist.domain.section.Section@ba071fc1 is not passed as a prerequisite of ir.proprog.enrollassist.domain.section.Section@ba07234c"},
        });
    }

    @Test
    public void testEnrollmentRules() {
        List<EnrollmentRuleViolation> violations = enrollmentList.checkEnrollmentRules();
        assertEquals(this.expectedViolationsSize, violations.size());
        if (violations.size() != 0)
           assertEquals(this.expectedViolationMessage, violations.get(0).toString());
    }
}