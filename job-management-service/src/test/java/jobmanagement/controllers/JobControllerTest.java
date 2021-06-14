package jobmanagement.controllers;

import jobmanagement.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class JobControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JobService jobService;

//    @Test
//    void createJob() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(
//                post("/jobs", new JobDto())
//                        .accept(MediaType.APPLICATION_JSON
//                        )).andReturn();
//
//        assertEquals(HttpStatus.ACCEPTED.value(), mvcResult.getResponse().getStatus());
//    }
}