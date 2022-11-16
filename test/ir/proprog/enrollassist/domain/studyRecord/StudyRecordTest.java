package ir.proprog.enrollassist.domain.studyRecord;

import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class StudyRecordTest {
    public int grade;
    public GraduateLevel graduateLevel;
    public String courseGraduateLevel;
    public boolean expectedIsPassed;

    public StudyRecordTest(int grade, GraduateLevel graduateLevel, String courseGraduateLevel, boolean expectedIsPassed){
        this.grade = grade;
        this.graduateLevel = graduateLevel;
        this.courseGraduateLevel = courseGraduateLevel;
        this.expectedIsPassed = expectedIsPassed;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters(){
        return Arrays.asList(new Object[][] {{9, GraduateLevel.PHD, "PHD", false}, {11, GraduateLevel.PHD, "PHD", false},
                {13, GraduateLevel.PHD, "PHD", false}, {15, GraduateLevel.PHD, "PHD", true},
                {9, GraduateLevel.Undergraduate, "PHD", false}, {11, GraduateLevel.Undergraduate, "PHD", true},
                {13, GraduateLevel.Undergraduate, "PHD", true}, {15, GraduateLevel.Undergraduate, "PHD", true},
                {9, GraduateLevel.Masters, "PHD", false}, {11, GraduateLevel.Masters, "PHD", false},
                {13, GraduateLevel.Masters, "PHD", true}, {15, GraduateLevel.Masters, "PHD", true},
                {9, GraduateLevel.PHD, "Undergraduate", false}, {11, GraduateLevel.PHD, "Undergraduate", true},
                {13, GraduateLevel.PHD, "Undergraduate", true}, {15, GraduateLevel.PHD, "Undergraduate", true},
                {9, GraduateLevel.Undergraduate, "Undergraduate", false},
                {11, GraduateLevel.Undergraduate, "Undergraduate", true},
                {13, GraduateLevel.Undergraduate, "Undergraduate", true},
                {15, GraduateLevel.Undergraduate, "Undergraduate", true},
                {9, GraduateLevel.Masters, "Undergraduate", false}, {11, GraduateLevel.Masters, "Undergraduate", true},
                {13, GraduateLevel.Masters, "Undergraduate", true}, {15, GraduateLevel.Masters, "Undergraduate", true},
                {9, GraduateLevel.PHD, "Masters", false}, {11, GraduateLevel.PHD, "Masters", false},
                {13, GraduateLevel.PHD, "Masters", true}, {15, GraduateLevel.PHD, "Masters", true},
                {9, GraduateLevel.Masters, "Masters", false}, {11, GraduateLevel.Masters, "Masters", false},
                {13, GraduateLevel.Masters, "Masters", true}, {15, GraduateLevel.Masters, "Masters", true},
                {9, GraduateLevel.Undergraduate, "Masters", false}, {11, GraduateLevel.Undergraduate, "Masters", true},
                {13, GraduateLevel.Undergraduate, "Masters", true}, {15, GraduateLevel.Undergraduate, "Masters", true}
        });
    }

    @Test
    public void test1() throws ExceptionList {
        StudyRecord studyRecord = new StudyRecord("00001",
                new Course("0000001", "test", 3, courseGraduateLevel),
                grade
        );
        assertEquals(expectedIsPassed, studyRecord.isPassed(graduateLevel));
    }
}