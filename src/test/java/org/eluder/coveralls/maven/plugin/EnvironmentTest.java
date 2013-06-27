package org.eluder.coveralls.maven.plugin;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.service.ServiceSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnvironmentTest {

    private AbstractCoverallsMojo mojo;
    
    @Mock
    private ServiceSetup serviceMock;
    
    @Before
    public void init() {
        mojo = new AbstractCoverallsMojo() {
            @Override
            protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
                return mock(CoverageParser.class);
            }
        };
        mojo.serviceName = "service";
        when(serviceMock.isSelected("service")).thenReturn(true);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithoutMojo() {
        new Environment(null, Arrays.asList(serviceMock));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithoutServices() {
        new Environment(mojo, null);
    }
    
    @Test
    public void testSetupWithoutServices() {
        create(Collections.<ServiceSetup>emptyList()).setup();
        assertProperties(null, null, null);
    }
    
    @Test
    public void testSetupWithoutValues() {
        when(serviceMock.getServiceJobId()).thenReturn("");
        when(serviceMock.getRepoToken()).thenReturn(null);
        when(serviceMock.getBranch()).thenReturn(null);
        
        create(Arrays.asList(serviceMock)).setup();
        assertProperties(null, null, null);
    }
    
    @Test
    public void testSetupWithValues() {
        when(serviceMock.getServiceJobId()).thenReturn("123");
        when(serviceMock.getRepoToken()).thenReturn("abcde");
        when(serviceMock.getBranch()).thenReturn("master");
        
        create(Arrays.asList(mock(ServiceSetup.class), serviceMock)).setup();
        assertProperties("123", "abcde", "master");
    }
    
    private Environment create(final Iterable<ServiceSetup> services) {
        return new Environment(mojo, services);
    }
    
    private void assertProperties(final String serviceJobId, final String repoToken, final String branch) {
        assertEquals(serviceJobId, mojo.serviceJobId);
        assertEquals(repoToken, mojo.repoToken);
        assertEquals(branch, mojo.branch);
    }
}
