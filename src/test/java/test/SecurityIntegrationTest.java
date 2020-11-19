package test;

import conf.MvcConfig;
import conf.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@SuppressWarnings("PMD")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { MvcConfig.class, SecurityConfig.class })
public class SecurityIntegrationTest {

    private WebApplicationContext wac;

    @Autowired
    public void setWac(WebApplicationContext wac) {
        this.wac = wac;
    }

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .apply(sharedHttpSession())
                .build();
    }

    @Test
    public void apiUrlsNeedAuthentication() throws Exception {
        mvc.perform(get("/api/info"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void apiHomeDoesNotNeedAuthentication() throws Exception {
        mvc.perform(get("/api/home"))
                .andExpect(status().isOk());
    }

    @Test
    public void redirectsToLoginForm() throws Exception {
        mvc.perform(get("/api/info"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void canLoginWithCorrectPassword() throws Exception {
        mvc.perform(formLogin("/login").user("user").password("user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        mvc.perform(formLogin("/login").user("user").password("wrong_pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void adminCanSeeMoreInfo() throws Exception {
        mvc.perform(get("/api/admin/info").with(user("user").roles("USER")))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/admin/info").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void canLogOut() throws Exception {
        mvc.perform(get("/api/info").with(user("user").roles("USER")))
                .andExpect(status().isOk());

        mvc.perform(get("/api/info"))
                .andExpect(status().isOk());

        mvc.perform(logout("/api/logout"));

        mvc.perform(get("/api/info"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void doesNotShowLoginForm() throws Exception {
        mvc.perform(get("/api/info"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void logOutDoesNotRedirect() throws Exception {
        mvc.perform(logout("/api/logout"))
                .andExpect(status().isOk());
    }

    @Test
    public void canLoginWithJsonRequest() throws Exception {
        mvc.perform(get("/api/info"))
                .andExpect(status().isUnauthorized());

        mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON).content("bad_data"))
                .andExpect(status().isUnauthorized());

        String json = "{ \"userName\": \"user\", \"password\": \"123\" }";

        mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mvc.perform(get("/api/info"))
                .andExpect(status().isOk());
    }

    @Test
    public void userCanSeeOnlyOwnInfo() throws Exception {
        mvc.perform(get("/api/users/user").with(user("user").roles("USER")))
                .andExpect(status().isOk());

        mvc.perform(get("/api/users/alice").with(user("user").roles("USER")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void canAccessWithJwtToken() throws Exception {
        String json = "{ \"userName\": \"user\", \"password\": \"123\" }";

        MvcResult mvcResult = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus(), is(200));

        var jwtToken = mvcResult.getResponse().getHeader("Authorization");

        mvc.perform(get("/api/info"))
                .andExpect(status().isUnauthorized());

        mvc.perform(get("/api/info")
                .header("Authorization", jwtToken))
                .andExpect(status().isOk());

    }
}