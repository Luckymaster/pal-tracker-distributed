package io.pivotal.pal.tracker.allocations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String registrationServerEndpoint;
    private ConcurrentMap<Long, ProjectInfo> cachedProject = new ConcurrentHashMap<>();

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations= restOperations;
        this.registrationServerEndpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        cachedProject.put(projectId,restOperations.getForObject(registrationServerEndpoint + "/projects/" + projectId, ProjectInfo.class));
        return (ProjectInfo) cachedProject.get(projectId);
    }

    public ProjectInfo getProjectFromCache(long projectId) {
        return (ProjectInfo) cachedProject.get(projectId);
    }

}
