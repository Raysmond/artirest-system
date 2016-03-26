package com.raysmond.artirest.service;

import com.raysmond.artirest.domain.Log;
import com.raysmond.artirest.domain.enumeration.LogType;
import com.raysmond.artirest.repository.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Raysmond<i@raysmond.com>
 */
@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public Log updateArtifact(String processId, String artifactId, String service) {
        Log log = new Log();
        log.setArtifactId(artifactId);
        log.setProcessId(processId);
        log.setService(service);
        log.setType(LogType.UPDATE_ARTIFACT);
        log.setTitle("Artifact (" + artifactId + ") was updated.");
        logRepository.save(log);
        return log;
    }

    public Log callService(String processId, String service) {
        Log log = new Log();
        log.setTitle("Service (" + service + ") was invoked.");
        log.setProcessId(processId);
        log.setType(LogType.CALL_SERVICE);
        logRepository.save(log);
        return log;
    }

    public Log stateTransition(String processId, String artifactId, String fromState, String toState, String service) {
        Log log = new Log();
        log.setArtifactId(artifactId);
        log.setProcessId(processId);
        log.setService(service);
        log.setTitle("The state of artifact (" + artifactId + ") transited from \"" + fromState + "\" to \"" + toState + "\".");
        log.setType(LogType.STATE_TRANSITION);
        log.setFromState(fromState);
        log.setToState(toState);
        logRepository.save(log);
        return log;
    }
}
