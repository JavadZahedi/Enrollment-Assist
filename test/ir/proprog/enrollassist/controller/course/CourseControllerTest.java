package ir.proprog.enrollassist.controller.course;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void afterGetFromCourseControllerShouldReturnAddedCourseInfo() {
        // Arrange
        JSONObject obj = new JSONObject();

        obj.put("graduateLevel", "Undergraduate");
        obj.put("courseTitle", "AP");
        obj.put("courseCredits", 3);
        JSONObject courseNumber = new JSONObject();
        courseNumber.put("courseNumber", "0000001");
        obj.put("courseNumber", courseNumber);

        // Act
        this.restTemplate.postForObject("http://localhost:" + port + "/courses", obj, String.class);
        String expected_json_string = "[{\"courseId\":1,\"courseNumber\":{\"courseNumber\":\"0000001\"},\"" +
                "graduateLevel\":\"Undergraduate\",\"courseTitle\":\"AP\",\"courseCredits\":3,\"prerequisites\":[]}]";

        // Assert
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/courses",
                String.class)).contains(expected_json_string);
    }

    @Test
    public void ifRequestHaveWrongFormatCourseControllerShouldReturnError() {
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/courses", "dummy message",
                String.class)).contains("Unsupported Media Type");
    }

    @Test
    public void ifRequestHaveUnrecognizedJsonFieldCourseControllerShouldReturnError() {
        JSONObject obj = new JSONObject();

        obj.put("Unrecognized Fields", "dummy");
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/courses", obj,
                String.class)).contains("JSON parse error: Unrecognized field");
    }

    @Test
    public void afterGetFromCourseControllerWhichHaveNotAnyCoursesShouldReturnEmptyJson() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/courses",
                String.class)).contains("[]");
    }

    @Test
    public void ifGetCourseWithExistIdCourseControllerShouldReturnCourseInfo() {
        // Arrange
        JSONObject obj = new JSONObject();

        String CourseId = "1";
        obj.put("courseId", CourseId);
        obj.put("graduateLevel", "Undergraduate");
        obj.put("courseTitle", "AP");
        obj.put("courseCredits", 3);
        JSONObject courseNumber = new JSONObject();
        courseNumber.put("courseNumber", "0000001");
        obj.put("courseNumber", courseNumber);

        // Act
        this.restTemplate.postForObject("http://localhost:" + port + "/courses", obj, String.class);
        String expected_json_string = "{\"courseId\":1,\"courseNumber\":{\"courseNumber\":\"0000001\"},\"" +
                "graduateLevel\":\"Undergraduate\",\"courseTitle\":\"AP\",\"courseCredits\":3,\"prerequisites\":[]}";

        // Assert
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/courses/" + CourseId,
                String.class)).contains(expected_json_string);
    }

    @Test
    public void ifCourseWasAddedCourseControllerShouldReturnAddedCourseInfo() {
        // Arrange
        JSONObject obj = new JSONObject();

        String CourseId = "2";
        obj.put("courseId", CourseId);
        obj.put("graduateLevel", "Undergraduate");
        obj.put("courseTitle", "DS");
        obj.put("courseCredits", 3);
        JSONObject courseNumber = new JSONObject();
        courseNumber.put("courseNumber", "0000002");
        obj.put("courseNumber", courseNumber);

        String expected_json_string = "{\"courseId\":2,\"courseNumber\":{\"courseNumber\":\"0000002\"},\"" +
                "graduateLevel\":\"Undergraduate\",\"courseTitle\":\"DS\",\"courseCredits\":3,\"prerequisites\":[]}";

        // Assert
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/courses", obj,
                String.class)).contains(expected_json_string);
    }
}
