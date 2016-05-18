package com.raysmond.artirest.service;

import com.raysmond.artirest.domain.Artifact;
import com.raysmond.artirest.domain.ArtifactModel;
import com.raysmond.artirest.domain.Attribute;
import com.raysmond.artirest.domain.AttributeModel;
import com.raysmond.artirest.domain.BusinessRuleModel;
import com.raysmond.artirest.domain.Process;
import com.raysmond.artirest.domain.ProcessModel;
import com.raysmond.artirest.domain.ServiceModel;
import com.raysmond.artirest.domain.StateModel;
import com.raysmond.artirest.domain.enumeration.ServiceType;
import com.raysmond.artirest.repository.ArtifactModelRepository;
import com.raysmond.artirest.repository.ArtifactRepository;
import com.raysmond.artirest.repository.ProcessModelRepository;
import com.raysmond.artirest.repository.ProcessRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Process.
 */
@Service
public class ProcessService {

    private final Logger log = LoggerFactory.getLogger(ProcessService.class);

    @Inject
    private ProcessRepository processRepository;

    @Autowired
    private ProcessModelRepository processModelRepository;

    @Autowired
    private ArtifactModelRepository artifactModelRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private LogService logService;

    public static final String CACHE_NAME = "artirest.process";

    /**
     * Save a process.
     *
     * @return the persisted entity
     */
    public Process save(Process process) {
        log.debug("Request to save Process : {}", process);
        Process result = processRepository.save(process);
        return result;
    }

    /**
     * get all the processs.
     *
     * @return the list of entities
     */
    public Page<Process> findAll(Pageable pageable) {
        log.debug("Request to get all Processs");
        Page<Process> result = processRepository.findAll(pageable);
        return result;
    }

    public Page<Process> findInstances(String processModelId, Pageable pageable) {
        ProcessModel processModel = processModelRepository.findOne(processModelId);
        Page<Process> processes = processRepository.findByProcessModel(processModel, pageable);
        return processes;
    }

    /**
     * get one process by id.
     *
     * @return the entity
     */
    @Cacheable(CACHE_NAME)
    public Process findOne(String id) {
        log.debug("Request to get Process : {}", id);
        Process process = processRepository.findOne(id);
        process.getProcessModel().getId();
        process.getProcessModel().businessRules.size();
        process.getProcessModel().artifacts.size();
        process.getProcessModel().services.size();
        process.getArtifacts().size();
        return process;
    }

    @CachePut(value = CACHE_NAME, key = "#process.id")
    public Process cacheSave(Process process) {
        log.debug("Save process to cache: {}", process.getId());
        return process;
    }

    /**
     * delete the  process by id.
     */
    @CacheEvict(CACHE_NAME)
    public void delete(String id) {
        log.debug("Request to delete Process : {}", id);
        processRepository.delete(id);
    }

    public List<ServiceModel> availableServices(Process process) {
        List<ServiceModel> services = new ArrayList<>();

        for (Artifact artifact : process.getArtifacts()) {
            services.addAll(artifactService.availableServices(artifact, process.getProcessModel().getId()));
        }

        if (process.getArtifacts().isEmpty()) {
            services.addAll(artifactService.availableBeginServices(process.getProcessModel()));
        }

        return services;
    }

    public Process createProcessInstance(ProcessModel model) {
        Process process = new Process();
        process.setName(model.getName());
        process.setProcessModel(model);

        processRepository.save(process);
        return process;
    }


    public Artifact newArtifactFromModel(ArtifactModel model) {
        Artifact artifact = new Artifact();
        artifact.setArtifactModel(model);
        artifact.setName(model.getName());

        StateModel startState = model.getStartState();
        artifact.setCurrentState(startState == null ? "" : startState.name);


        for (AttributeModel attr : model.attributes) {
            switch (attr.getType()) {
                case "Integer":
                    artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), 0));
                    break;
                case "Double":
                    artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), 0D));
                    break;
                case "Long":
                    artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), 0L));
                    break;
                case "String":
                    artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), null));
                    break;
                case "Date":
                    artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), null));
                    break;
                default:
                    if (attr.getType().startsWith("List<")) {
                        String itemType = attr.getType().substring(5, attr.getType().length() - 1);
                        switch (itemType) {
                            case "Integer":
                                artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), new ArrayList<Integer>()));
                                break;
                            case "Double":
                                artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), new ArrayList<Double>()));
                                break;
                            case "Long":
                                artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), new ArrayList<Long>()));
                                break;
                            case "Date":
                                artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), new ArrayList<Date>()));
                                break;
                            case "String":
                                artifact.getAttributes().add(new Attribute(attr.getName(), attr.getComment(), new ArrayList<String>()));
                                break;
                            default:

                        }
                    }
            }

        }

        return artifact;
    }


    public Artifact createArtifact(String processId, String artifactModelId) {
        ArtifactModel artifactModel = artifactModelRepository.findOne(artifactModelId);
        Artifact artifact = newArtifactFromModel(artifactModel);
        artifactRepository.save(artifact);

        Process instance = processRepository.findOne(processId);
        instance.getArtifacts().add(artifact);

        // comment here to enable cache
         processRepository.save(instance);

        return artifact;
    }

    public ServiceModel findService(String serviceName, ProcessModel processModel) {
        for (ServiceModel service : processModel.services) {
            if (service.name.equals(serviceName)) {
                return service;
            }
        }

        return null;
    }

    public ArtifactModel findArtifactModel(String artifactName, ProcessModel processModel) {
        for (ArtifactModel artifact : processModel.artifacts) {
            if (artifact.getName().equals(artifactName)) {
                return artifact;
            }
        }

        return null;
    }

    public Set<BusinessRuleModel> findServiceRelatedRules(String serviceName, ProcessModel processModel) {
        Set<BusinessRuleModel> rules = new HashSet<>();

        for (BusinessRuleModel rule : processModel.businessRules) {
            if (rule.action.service.equals(serviceName)) {
                rules.add(rule);
            }
        }

        return rules;
    }

    /**
     * 目前只允许: 一个流程实例里一个Artifact的名字只能有一个实例
     */
    public Artifact findArtifactByName(Process process, String artifactName) {
        Artifact artifact = null;

        for (Artifact artifact1 : process.getArtifacts()) {
            if (artifact1.getName().equals(artifactName)) {
                artifact = artifact1;
                break;
            }
        }

        return artifact;
    }

    public Artifact createProcessArtifact(Process process, String artifactName) throws Exception {
        ArtifactModel artifactModel = findArtifactModel(artifactName, process.getProcessModel());

        if (artifactModel == null) {
            throw new Exception("Illegal artifact model: " + artifactName);
        }

        Artifact artifact = newArtifactFromModel(artifactModel);

        artifact.setCurrentState(artifactModel.getStartState().name);
        artifact = artifactRepository.save(artifact);

        process.getArtifacts().add(artifact);

        // comment here to enable cache
        processRepository.save(process);

        return artifact;
    }

    public Process setArtifactAttributes(Process process, Artifact inputArtifact, ServiceModel serviceModel) throws Exception {
        Artifact artifact = findArtifactByName(process, inputArtifact.getName());

        if (artifact == null) {
            artifact = createProcessArtifact(process, inputArtifact.getName());
        }

        for (Attribute attribute : inputArtifact.getAttributes()) {
            if (serviceModel.inputParams.contains(attribute.getName())) {
                artifact.setAttribute(attribute.getName(), attribute);
            }
        }

        process.getArtifacts().add(artifact);

        return process;
    }

    public AttributeModel findAttributeModel(ProcessModel processModel, String artifactName, String attributeName) {
        ArtifactModel artifactModel = findArtifactModel(artifactName, processModel);
        AttributeModel attributeModel = null;

        if (artifactModel != null) {
            for (AttributeModel attribute : artifactModel.attributes) {
                if (attribute.getName().equals(attributeName)) {
                    attributeModel = attribute;
                    break;
                }
            }
        }

        return attributeModel;
    }

    private boolean verifyAtomConditions(Process process, ServiceModel service, Set<BusinessRuleModel.Atom> atoms) {
        boolean satisfied = true;

        for (BusinessRuleModel.Atom atom : atoms) {
            Artifact a = findArtifactByName(process, atom.artifact);

            if (atom.type.equals(BusinessRuleModel.AtomType.INSTATE)) {
                satisfied = satisfied
                    && a != null
                    && a.getCurrentState() != null
                    && a.getCurrentState().equals(atom.state);
            }

            if (atom.type.equals(BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED)) {
                satisfied = satisfied
                    && a != null
                    && a.getAttribute(atom.attribute) != null
                    && a.getAttribute(atom.attribute).getValue() != null;
            }

            if (atom.type.equals(BusinessRuleModel.AtomType.SCALAR_COMPARISON)) {
                satisfied = satisfied
                    && a != null
                    && a.getAttribute(atom.attribute) != null
                    && a.getAttribute(atom.attribute).getValue() != null;

                if (!satisfied) {
                    break;
                }

                Attribute attribute = a.getAttribute(atom.attribute);
                AttributeModel attributeModel = findAttributeModel(process.getProcessModel(), atom.artifact, atom.attribute);

                switch (atom.operator) {
                    case EQUAL:
                        satisfied = satisfied && attribute.getValue().equals(atom.value);
                        break;
                    case LESS:
                        switch (attributeModel.getType()) {
                            case "Integer":
                                satisfied = satisfied && ((Integer) attribute.getValue() < (Integer) atom.value);
                                break;
                            case "Long":
                                satisfied = satisfied && ((Long) attribute.getValue() < (Long) atom.value);
                                break;
                            case "Double":
                                satisfied = satisfied && ((Double) attribute.getValue() < (Double) atom.value);
                                break;
                            default:
                                ;
                        }
                        break;
                    case LARGER:
                        switch (attributeModel.getType()) {
                            case "Integer":
                                satisfied = satisfied && ((Integer) attribute.getValue() > (Integer) atom.value);
                                break;
                            case "Long":
                                satisfied = satisfied && ((Long) attribute.getValue() > (Long) atom.value);
                                break;
                            case "Double":
                                satisfied = satisfied && ((Double) attribute.getValue() > (Double) atom.value);
                                break;
                            default:
                                ;
                        }
                        break;
                    default:
                        ;
                }
            }

            if (!satisfied) {
                break;
            }
        }


        return satisfied;
    }


    public Process invokeService(String serviceName, Process process, Artifact artifact) throws Exception {
        ServiceModel serviceModel = findService(serviceName, process.getProcessModel());

        process = setArtifactAttributes(process, artifact, serviceModel);

        if (serviceModel.type == ServiceType.HUMAN_TASK) {
            return invokeHumanService(process, artifact, serviceModel);
        } else if (serviceModel.type == ServiceType.INVOKE_SERVICE) {
            // TODO
        }

        return process;
    }

    public void afterInvokingService(Process process){
        if (isProcessEnded(process)){
            process.getArtifacts().forEach(artifact -> {
                artifactRepository.save(artifact);
            });
            processRepository.save(process);
        }
    }


    private Process invokeHumanService(Process process, Artifact artifact, ServiceModel service) throws Exception {
        Set<BusinessRuleModel> rules = findServiceRelatedRules(service.name, process.getProcessModel());

        BusinessRuleModel firstRuleSatisfied = null;
        for (BusinessRuleModel rule : rules) {
            if (verifyAtomConditions(process, service, rule.preConditions)) {
                firstRuleSatisfied = rule;
                break;
            }
        }

        if (!rules.isEmpty() && firstRuleSatisfied == null) {
            throw new Exception("PreConditions of business rules for service " + service.name + " are not satisfied.");

        }

        if (firstRuleSatisfied != null) {
            log.debug("first business rule: {}", firstRuleSatisfied.name);
        }

        // comment here to enable cache
        processRepository.save(process);

        artifact = findArtifactByName(process, artifact.getName());

        logService.callService(process.getId(), service.name);
        logService.updateArtifact(process.getId(), artifact.getId(), service.name);


        if (firstRuleSatisfied != null) {
            doTransitions(process, firstRuleSatisfied, service);
        }

        // 后置条件没有计算, 问题:
        // 如果后置条件不满足怎么办?

        // return processRepository.findOne(process.getId());

        return process;
    }


    /**
     * Perform state transitions after invoking a service
     */
    private void doTransitions(Process process, BusinessRuleModel rule, ServiceModel service) {
        if (rule != null && !rule.action.transitions.isEmpty()) {
            for (BusinessRuleModel.Transition transition : rule.action.transitions) {
                for (Artifact artifact1 : process.getArtifacts()) {
                    if (artifact1.getName().equals(transition.artifact) && artifact1.getCurrentState().equals(transition.fromState)) {

                        // Do transition
                        artifact1.setCurrentState(transition.toState);

                        // comment here to enable cache
                        artifactRepository.save(artifact1);

                        logService.stateTransition(process.getId(), artifact1.getId(), transition.fromState, transition.toState, service.name);
                    }
                }
            }
        }
    }

    /**
     * If the process is ended
     */
    public boolean isProcessEnded(Process process) {
        if (process.getArtifacts().isEmpty()) {
            return false;
        }

        for (Artifact artifact : process.getArtifacts()) {
            if (artifact.getCurrentState() != null) {
                StateModel stateModel = artifactService.findState(artifact.getCurrentState(), artifact.getName(), process.getProcessModel());
                if (stateModel == null || stateModel.type != StateModel.StateType.FINAL) {
                    return false;
                }
            }
        }

        return true;
    }
}
