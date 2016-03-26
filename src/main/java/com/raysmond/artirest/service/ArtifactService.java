package com.raysmond.artirest.service;

import com.raysmond.artirest.domain.Artifact;
import com.raysmond.artirest.domain.ArtifactModel;
import com.raysmond.artirest.domain.BusinessRuleModel;
import com.raysmond.artirest.domain.ProcessModel;
import com.raysmond.artirest.domain.ServiceModel;
import com.raysmond.artirest.domain.StateModel;
import com.raysmond.artirest.repository.ArtifactRepository;
import com.raysmond.artirest.repository.ProcessModelRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Artifact.
 */
@Service
public class ArtifactService {

    private final Logger log = LoggerFactory.getLogger(ArtifactService.class);

    @Inject
    private ArtifactRepository artifactRepository;

    @Autowired
    private ProcessModelRepository processModelRepository;

    /**
     * Save a artifact.
     *
     * @return the persisted entity
     */
    public Artifact save(Artifact artifact) {
        log.debug("Request to save Artifact : {}", artifact);
        Artifact result = artifactRepository.save(artifact);
        return result;
    }

    /**
     * get all the artifacts.
     *
     * @return the list of entities
     */
    public Page<Artifact> findAll(Pageable pageable) {
        log.debug("Request to get all Artifacts");
        Page<Artifact> result = artifactRepository.findAll(pageable);
        return result;
    }

    /**
     * get one artifact by id.
     *
     * @return the entity
     */
    public Artifact findOne(String id) {
        log.debug("Request to get Artifact : {}", id);
        Artifact artifact = artifactRepository.findOne(id);
        return artifact;
    }

    /**
     * delete the  artifact by id.
     */
    public void delete(String id) {
        log.debug("Request to delete Artifact : {}", id);
        artifactRepository.delete(id);
    }

    public StateModel findState(String name, String artifactName, ProcessModel processModel) {
        ArtifactModel artifact = null;
        for (ArtifactModel a : processModel.artifacts) {
            if (a.getName().equals(artifactName)) {
                artifact = a;
                break;
            }
        }

        if (artifact == null) {
            return null;
        }

        for (StateModel state : artifact.states) {
            if (state.name.equals(name)) {
                return state;
            }
        }

        return null;
    }


    public ServiceModel findService(String serviceName, ProcessModel processModel) {
        for (ServiceModel service : processModel.services) {
            if (service.name.equals(serviceName)) {
                return service;
            }
        }

        return null;
    }

    public List<ServiceModel> availableBeginServices(ProcessModel processModel) {
        List<ServiceModel> services = new ArrayList<>();

        for (BusinessRuleModel rule : processModel.businessRules) {
            if (rule.action == null) {
                continue;
            }

            for (BusinessRuleModel.Transition transition : rule.action.transitions) {
                StateModel stateModel = findState(transition.fromState, transition.artifact, processModel);
                if (stateModel != null && stateModel.type == StateModel.StateType.START) {
                    services.add(findService(rule.action.service, processModel));
                    break;
                }
            }
        }

        return services;
    }

    /**
     * Find available services of an Artifact instance
     */
    public List<ServiceModel> availableServices(Artifact artifact, String processModelId) {
        List<ServiceModel> services = new ArrayList<>();
        List<String> availables = new ArrayList<>();
        ProcessModel model = processModelRepository.findOne(processModelId);

        List<String> servicesWithRules = new ArrayList<>();

        for (BusinessRuleModel rule : model.businessRules) {
            if (rule.action != null) {
                servicesWithRules.add(rule.action.service);
            }

            boolean stateSatisfied = false;

            for (BusinessRuleModel.Atom atom : rule.preConditions) {
                if (atom.artifact.equals(artifact.getName())
                    && atom.type.equals(BusinessRuleModel.AtomType.INSTATE)
                    && artifact.getCurrentState().equals(atom.state)) {

                    stateSatisfied = true;
                }
            }

            if (stateSatisfied) {
                availables.add(rule.action.service);
            }
        }

        for (ServiceModel service : model.services) {
            if (!servicesWithRules.contains(service.name) || availables.contains(service.name)) {
                services.add(service);
            }
        }

        return services;
    }
}
