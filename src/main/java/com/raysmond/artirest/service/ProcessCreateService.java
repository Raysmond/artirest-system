package com.raysmond.artirest.service;

import com.raysmond.artirest.domain.*;
import com.raysmond.artirest.domain.enumeration.Status;
import com.raysmond.artirest.repository.ArtifactModelRepository;
import com.raysmond.artirest.repository.ProcessModelRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Raysmond<i@raysmond.com>
 */
@Service
public class ProcessCreateService {
    private final Logger log = LoggerFactory.getLogger(ProcessCreateService.class);


    @Autowired
    private ArtifactModelRepository artifactModelRepository;

    @Autowired
    private ProcessModelRepository processModelRepository;

//    @PostConstruct
//    private void init() {
//        if (processModelRepository.count() == 0) {
//            log.debug("Create Loan and Order process models for the first time");
//
//            createLoanProcessModel();
//            createOrderProcessModel();
//        }
//    }

    public ProcessModel createLoanProcessModel() {
        String host = "http://localhost:3000";

        // loan artifact
        ArtifactModel artifact = new ArtifactModel();
        artifact.setName("Loan");
        artifact.setComment("Loan artifact");
        artifact.attributes.add(new AttributeModel("customerName", "String", "Customer name"));
        artifact.attributes.add(new AttributeModel("amount", "Double", "Loan amount"));
        artifact.attributes.add(new AttributeModel("type", "String", "Loan type"));
        artifact.attributes.add(new AttributeModel("rate", "Double", "Loan rate"));
        artifact.attributes.add(new AttributeModel("appliedAt", "Date", "Applied time"));
        artifact.attributes.add(new AttributeModel("approveStatus", "String", "Approve Status"));
        artifact.attributes.add(new AttributeModel("confirmStatus", "String", "Confirm Status"));

        StateModel start = new StateModel("start", "Start", StateModel.StateType.START);
        StateModel applied = new StateModel("applied", "Applied", StateModel.StateType.NORMAL);
        StateModel approved = new StateModel("approved", "Approved", StateModel.StateType.NORMAL);
        StateModel canceled = new StateModel("canceled", "Canceled", StateModel.StateType.FINAL);
        StateModel confirmed = new StateModel("confirmed", "Confirmed", StateModel.StateType.FINAL);
        start.nextStates.add("applied");
        applied.nextStates.add("approved");
        applied.nextStates.add("canceled");
        approved.nextStates.add("confirmed");

        artifact.states.add(start);
        artifact.states.add(applied);
        artifact.states.add(approved);
        artifact.states.add(canceled);
        artifact.states.add(confirmed);

        artifactModelRepository.save(artifact);

        ProcessModel model = new ProcessModel();
        model.setName("Loan Approval Process");
        model.artifacts.add(artifact);
        model.setStatus(Status.ENACTED);

        // services
//        ServiceModel serviceModel1 = new ServiceModel(
//            "getLoanTypes",
//            host + "/loan/services/getLoanTypes",
//            ServiceModel.RestMethod.GET,
//            null,
//            null);
//
//        model.services.add(serviceModel1);

        ServiceModel serviceModel2 = new ServiceModel(
            "apply",
            host + "/loan/services/apply",
            ServiceModel.RestMethod.POST,
            "Loan",
            "Loan");

        serviceModel2.inputParams.add("customerName");
        serviceModel2.inputParams.add("amount");
        serviceModel2.inputParams.add("type");
        serviceModel2.inputParams.add("rate");

        model.services.add(serviceModel2);


        ServiceModel serviceModel3 = new ServiceModel(
            "approve",
            host + "/loan/services/approve",
            ServiceModel.RestMethod.PUT,
            "Loan",
            "Loan");

        serviceModel3.inputParams.add("approveStatus");
        model.services.add(serviceModel3);


        ServiceModel serviceModel4 = new ServiceModel(
            "confirm",
            host + "/loan/services/confirm",
            ServiceModel.RestMethod.PUT,
            "Loan",
            "Loan");
        serviceModel4.inputParams.add("confirmStatus");

        model.services.add(serviceModel4);

        // business rules

        // rule1
        BusinessRuleModel rule1 = new BusinessRuleModel();
        rule1.name = "rule1_apply";
        rule1.action = new BusinessRuleModel.Action();
        rule1.action.name = "loan application";
        rule1.action.service = "apply";
        rule1.action.transitions.add(new BusinessRuleModel.Transition("Loan", "start", "applied"));

        // pre conditions
        rule1.preConditions.add(new BusinessRuleModel.Atom("Loan", null, "start", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule1.preConditions.add(new BusinessRuleModel.Atom("Loan", "amount", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));
        rule1.preConditions.add(new BusinessRuleModel.Atom("Loan", "customerName", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));
        rule1.preConditions.add(new BusinessRuleModel.Atom("Loan", "type", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));
        rule1.preConditions.add(new BusinessRuleModel.Atom("Loan", "rate", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));

        // post conditions
        rule1.postConditions.add(new BusinessRuleModel.Atom("Loan", null, "applied", null, BusinessRuleModel.AtomType.INSTATE, null));

        model.businessRules.add(rule1);


        // rule2
        BusinessRuleModel rule2 = new BusinessRuleModel();
        rule2.name = "rule2_approved";
        rule2.action = new BusinessRuleModel.Action();
        rule2.action.name = "loan approval";
        rule2.action.service = "approve";
        rule2.action.transitions.add(new BusinessRuleModel.Transition("Loan", "applied", "approved"));
        // rule2.action.transitions.add(new BusinessRuleModel.Transition("Loan", "applied", "canceled"));

        rule2.preConditions.add(new BusinessRuleModel.Atom("Loan", null, "applied", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule2.preConditions.add(new BusinessRuleModel.Atom("Loan", "approveStatus", null, BusinessRuleModel.Operator.EQUAL, BusinessRuleModel.AtomType.SCALAR_COMPARISON, "approved"));
//        condition3.atoms.add(new BusinessRuleModel.Atom("Loan", "amount", null, ">=", BusinessRuleModel.AtomType.ATTRIBUTE_CONDITION, 100000));

        rule2.postConditions.add(new BusinessRuleModel.Atom("Loan", null, "approved", null, BusinessRuleModel.AtomType.INSTATE, null));
//        condition4.atoms.add(new BusinessRuleModel.Atom("Loan", null, "canceled", null, BusinessRuleModel.AtomType.INSTATE, null));


        model.businessRules.add(rule2);


        BusinessRuleModel rule21 = new BusinessRuleModel();
        rule21.name = "rule2_canceled";
        rule21.action = new BusinessRuleModel.Action();
        rule21.action.name = "loan approval";
        rule21.action.service = "approve";
        rule21.action.transitions.add(new BusinessRuleModel.Transition("Loan", "applied", "canceled"));

        rule21.preConditions.add(new BusinessRuleModel.Atom("Loan", null, "applied", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule21.preConditions.add(new BusinessRuleModel.Atom("Loan", "approveStatus", null, BusinessRuleModel.Operator.EQUAL, BusinessRuleModel.AtomType.SCALAR_COMPARISON, "canceled"));

        rule21.postConditions.add(new BusinessRuleModel.Atom("Loan", null, "canceled", null, BusinessRuleModel.AtomType.INSTATE, null));


        model.businessRules.add(rule21);


        // rule3
        BusinessRuleModel rule3 = new BusinessRuleModel();
        rule3.name = "rule3_confirmed";
        rule3.action = new BusinessRuleModel.Action();
        rule3.action.name = "loan confirmation";
        rule3.action.service = "confirm";
        rule3.action.transitions.add(new BusinessRuleModel.Transition("Loan", "approved", "confirmed"));

        rule3.preConditions.add(new BusinessRuleModel.Atom("Loan", null, "approved", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule3.postConditions.add(new BusinessRuleModel.Atom("Loan", null, "confirmed", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule3.preConditions.add(new BusinessRuleModel.Atom("Loan", "confirmStatus", null, BusinessRuleModel.Operator.EQUAL, BusinessRuleModel.AtomType.SCALAR_COMPARISON, "confirmed"));

        model.businessRules.add(rule3);

        processModelRepository.save(model);

        return model;
    }

    public ProcessModel createOrderProcessModel() {
        String host = "http://localhost:3000";

        // artifacts
        ArtifactModel artifact1 = new ArtifactModel();
        artifact1.setName("Order");
        artifact1.attributes.add(new AttributeModel("grandTotal", "Double", "Grand total"));
        artifact1.attributes.add(new AttributeModel("customerName", "String", "Customer name"));
        artifact1.attributes.add(new AttributeModel("customerAddress", "String", "Customer address"));
        artifact1.attributes.add(new AttributeModel("orderItems", "List<String>", "Order items"));

        StateModel start = new StateModel("start", "Start", StateModel.StateType.START);
        StateModel add_order_item = new StateModel("add_order_item", "Adding order items", StateModel.StateType.NORMAL);
        StateModel creating_shipping = new StateModel("creating_shipping", "Creating shipping", StateModel.StateType.NORMAL);
        StateModel billed = new StateModel("billed", "Billed", StateModel.StateType.NORMAL);
        StateModel processing_order_item = new StateModel("processing_order_item", "Processing order items", StateModel.StateType.NORMAL);
        StateModel admin_creating_shipping = new StateModel("admin_creating_shipping", "Creating shipping", StateModel.StateType.NORMAL);
        StateModel ready_for_shipping = new StateModel("ready_for_shipping", "Ready for shipping", StateModel.StateType.NORMAL);
        StateModel in_shipping = new StateModel("in_shipping", "In shipping", StateModel.StateType.NORMAL);
        StateModel shipped = new StateModel("shipped", "Shipped", StateModel.StateType.NORMAL);
        StateModel closed = new StateModel("closed", "Closed", StateModel.StateType.FINAL);

        start.nextStates.add("add_order_item");
        add_order_item.nextStates.add("creating_shipping");
        creating_shipping.nextStates.add("billed");
        billed.nextStates.add("processing_order_item");
        processing_order_item.nextStates.add("admin_creating_shipping");
        admin_creating_shipping.nextStates.add("ready_for_shipping");
        ready_for_shipping.nextStates.add("in_shipping");
        in_shipping.nextStates.add("shipped");
        shipped.nextStates.add("closed");

        artifact1.states.add(start);
        artifact1.states.add(add_order_item);
        artifact1.states.add(creating_shipping);
        artifact1.states.add(billed);
        artifact1.states.add(processing_order_item);
        artifact1.states.add(admin_creating_shipping);
        artifact1.states.add(ready_for_shipping);
        artifact1.states.add(in_shipping);
        artifact1.states.add(shipped);
        artifact1.states.add(closed);

        // Shipment artifact
        ArtifactModel artifact2 = new ArtifactModel();
        artifact2.setName("Shipment");
        artifact2.setComment("Shipment");
        artifact2.attributes.add(new AttributeModel("orderId", "String", "Order id"));
        artifact2.attributes.add(new AttributeModel("customerName", "String", "Customer name"));
        artifact2.attributes.add(new AttributeModel("shippingAddress", "String", "Shipping address"));
        artifact2.attributes.add(new AttributeModel("shipStartDate", "Date", "Shipping start date"));
        artifact2.attributes.add(new AttributeModel("shipEndDate", "Date", "Shipping end date"));

        StateModel s_start = new StateModel("start", "Start", StateModel.StateType.START);
        StateModel s_waiting_for_ship_item = new StateModel("waiting_for_ship_item", "Waiting for ship item", StateModel.StateType.NORMAL);
        StateModel s_ready_for_shipping = new StateModel("ready_for_shipping", "Ready for shipping", StateModel.StateType.NORMAL);
        StateModel s_in_shipping = new StateModel("in_shipping", "In shipping", StateModel.StateType.NORMAL);
        StateModel s_shipping_completed = new StateModel("shipping_completed", "Shipping completed", StateModel.StateType.FINAL);

        s_start.nextStates.add("waiting_for_ship_item");
        s_waiting_for_ship_item.nextStates.add("ready_for_shipping");
        s_ready_for_shipping.nextStates.add("in_shipping");
        s_in_shipping.nextStates.add("shipping_completed");


        artifact2.states.add(s_start);
        artifact2.states.add(s_waiting_for_ship_item);
        artifact2.states.add(s_ready_for_shipping);
        artifact2.states.add(s_in_shipping);
        artifact2.states.add(s_shipping_completed);

        // Invoice artifact
        ArtifactModel artifact3 = new ArtifactModel();
        artifact3.setName("Invoice");
        artifact3.setComment("Invoice");
        artifact3.attributes.add(new AttributeModel("orderId", "String", "Order id"));
        artifact3.attributes.add(new AttributeModel("invoiceDate", "Date", "Invoice date"));
        artifact3.attributes.add(new AttributeModel("billingAddress", "String", "Billing address"));
        artifact3.attributes.add(new AttributeModel("total", "Double", "Total"));
        artifact3.attributes.add(new AttributeModel("amountPaid", "Double", "Amount paid"));

        StateModel i_start = new StateModel("start", "Start", StateModel.StateType.START);
        StateModel i_unpaid = new StateModel("unpaid", "Unpaid", StateModel.StateType.NORMAL);
        StateModel i_paid = new StateModel("paid", "Paid", StateModel.StateType.FINAL);

        i_start.nextStates.add("unpaid");
        i_unpaid.nextStates.add("paid");

        artifact3.states.add(i_start);
        artifact3.states.add(i_unpaid);
        artifact3.states.add(i_paid);


//        artifact1.referenceArtifacts.add(artifact2.name);
//        artifact1.referenceArtifacts.add(artifact3.name);

        artifact1 = artifactModelRepository.save(artifact1);
        artifact2 = artifactModelRepository.save(artifact2);
        artifact3 = artifactModelRepository.save(artifact3);

        ProcessModel model1 = new ProcessModel();
        model1.setName("Order");


        model1.artifacts.add(artifact1);
        model1.artifacts.add(artifact2);
        model1.artifacts.add(artifact3);


        // services
        model1.services.add(new ServiceModel(
            "createOrder",
            host + "/shopping/services/createOrder",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"));

        model1.services.add(new ServiceModel(
            "addOrderItem",
            host + "/shopping/services/addOrderItem",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"));

        model1.services.add(new ServiceModel(
            "createShipping",
            host + "/shopping/services/createShipping",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"));

        model1.services.add(new ServiceModel(
            "payOrder",
            host + "/shopping/services/payOrder",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"
        ));

        model1.services.add(new ServiceModel(
            "processOrderItem",
            host + "/shopping/services/processOrderItem",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"
        ));

        model1.services.add(new ServiceModel(
            "adminCreateShipping",
            host + "/shopping/services/adminCreateShipping",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"
        ));

        model1.services.add(new ServiceModel(
            "readyForShipping",
            host + "/shopping/services/readyForShipping",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"
        ));

        model1.services.add(new ServiceModel(
            "inShipping",
            host + "/shopping/services/inShipping",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"
        ));

        model1.services.add(new ServiceModel(
            "shipped",
            host + "/shopping/services/shipped",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"
        ));

        model1.services.add(new ServiceModel(
            "close",
            host + "/shopping/services/close",
            ServiceModel.RestMethod.POST,
            "Order",
            "Order"
        ));


        // business rules
        BusinessRuleModel rule1 = new BusinessRuleModel();
        rule1.name = "rule1_createOrder";
        rule1.action = new BusinessRuleModel.Action();
        rule1.action.name = "create order";
        rule1.action.service = "createOrder";
        rule1.action.transitions.add(new BusinessRuleModel.Transition("Order", "start", "add_order_item"));

        // pre conditions
        rule1.preConditions.add(new BusinessRuleModel.Atom("Order", null, "start", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule1.preConditions.add(new BusinessRuleModel.Atom("Order", "customerName", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));

        // post conditions
        rule1.postConditions.add(new BusinessRuleModel.Atom("Order", null, "add_order_item", null, BusinessRuleModel.AtomType.INSTATE, null));

        model1.businessRules.add(rule1);

        // add order item service rule
        BusinessRuleModel rule2 = new BusinessRuleModel();
        rule2.name = "rule2_add_order_item";
        rule2.action = new BusinessRuleModel.Action();
        rule2.action.name = "Add order item";
        rule2.action.service = "addOrderItem";

        rule2.preConditions.add(new BusinessRuleModel.Atom("Order", null, "add_order_item", null, BusinessRuleModel.AtomType.INSTATE, null));

        model1.businessRules.add(rule2);

        // customer creating_shipping rule
        BusinessRuleModel rule3 = new BusinessRuleModel();
        rule3.name = "rule3_creating_shipping";
        rule3.action = new BusinessRuleModel.Action();
        rule3.action.name = "Create shipping";
        rule3.action.service = "createShipping";
        rule3.action.transitions.add(new BusinessRuleModel.Transition("Order", "add_order_item", "creating_shipping"));
        rule3.action.transitions.add(new BusinessRuleModel.Transition("Invoice", "start", "unpaid"));


        rule3.preConditions.add(new BusinessRuleModel.Atom("Order", "customerAddress", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));
        rule3.preConditions.add(new BusinessRuleModel.Atom("Order", null, "add_order_item", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule3.preConditions.add(new BusinessRuleModel.Atom("Order", null, "creating_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));

        rule3.postConditions.add(new BusinessRuleModel.Atom("Order", null, "creating_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));

        model1.businessRules.add(rule3);


        // billing service rule
        BusinessRuleModel rule4 = new BusinessRuleModel();
        rule4.name = "rule4_pay";
        rule4.action = new BusinessRuleModel.Action();
        rule4.action.service = "payOrder";
        rule4.action.transitions.add(new BusinessRuleModel.Transition("Order", "creating_shipping", "billed"));
        rule4.action.transitions.add(new BusinessRuleModel.Transition("Invoice", "unpaid", "paid"));

        rule4.preConditions.add(new BusinessRuleModel.Atom("Order", null, "creating_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule4.preConditions.add(new BusinessRuleModel.Atom("Order", "customerAddress", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));
        rule4.preConditions.add(new BusinessRuleModel.Atom("Order", "grandTotal", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));

        rule4.postConditions.add(new BusinessRuleModel.Atom("Order", null, "billed", null, BusinessRuleModel.AtomType.INSTATE, null));

        model1.businessRules.add(rule4);


        // process_order_item service rule
        BusinessRuleModel rule5 = new BusinessRuleModel();
        rule5.name = "rule5_process_order_item";
        rule5.action = new BusinessRuleModel.Action();
        rule5.action.service = "processOrderItem";
        rule5.action.transitions.add(new BusinessRuleModel.Transition("Order", "billed", "processing_order_item"));

        rule5.preConditions.add(new BusinessRuleModel.Atom("Order", null, "billed", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule5.preConditions.add(new BusinessRuleModel.Atom("Order", null, "processing_order_item", null, BusinessRuleModel.AtomType.INSTATE, null));

        rule5.postConditions.add(new BusinessRuleModel.Atom("Order", null, "processing_order_item", null, BusinessRuleModel.AtomType.INSTATE, null));

        model1.businessRules.add(rule5);


        // admin creating_shipping service rule
        BusinessRuleModel rule6 = new BusinessRuleModel();
        rule6.name = "rule6_admin_creating_shipping";
        rule6.action = new BusinessRuleModel.Action();
        rule6.action.service = "adminCreateShipping";
        rule6.action.transitions.add(new BusinessRuleModel.Transition("Order", "processing_order_item", "admin_creating_shipping"));
        rule6.action.transitions.add(new BusinessRuleModel.Transition("Shipment", "start", "waiting_for_ship_item"));

        rule6.preConditions.add(new BusinessRuleModel.Atom("Order", null, "processing_order_item", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule6.preConditions.add(new BusinessRuleModel.Atom("Shipment", null, "start", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule6.preConditions.add(new BusinessRuleModel.Atom("Shipment", "orderId", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));
        rule6.preConditions.add(new BusinessRuleModel.Atom("Shipment", "customerName", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));
        rule6.preConditions.add(new BusinessRuleModel.Atom("Shipment", "shippingAddress", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));

        rule6.postConditions.add(new BusinessRuleModel.Atom("Order", null, "admin_creating_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule6.postConditions.add(new BusinessRuleModel.Atom("Shipment", null, "waiting_for_ship_item", null, BusinessRuleModel.AtomType.INSTATE, null));

        model1.businessRules.add(rule6);


        // ready_for_shipping
        BusinessRuleModel rule7 = new BusinessRuleModel();
        rule7.name = "rule7_ready_for_shipping";
        rule7.action = new BusinessRuleModel.Action();
        rule7.action.service = "readyForShipping";
        rule7.action.transitions.add(new BusinessRuleModel.Transition("Order", "admin_creating_shipping", "ready_for_shipping"));
        rule7.action.transitions.add(new BusinessRuleModel.Transition("Shipment", "waiting_for_ship_item", "ready_for_shipping"));

        rule7.preConditions.add(new BusinessRuleModel.Atom("Order", null, "admin_creating_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule7.preConditions.add(new BusinessRuleModel.Atom("Shipment", null, "waiting_for_ship_item", null, BusinessRuleModel.AtomType.INSTATE, null));

        rule7.postConditions.add(new BusinessRuleModel.Atom("Order", null, "ready_for_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule7.postConditions.add(new BusinessRuleModel.Atom("Shipment", null, "ready_for_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));

        model1.businessRules.add(rule7);


        // rule8 in_shipping service rule
        BusinessRuleModel rule8 = new BusinessRuleModel();
        rule8.name = "rule8_in_shipping";
        rule8.action = new BusinessRuleModel.Action();
        rule8.action.service = "inShipping";
        rule8.action.transitions.add(new BusinessRuleModel.Transition("Order", "ready_for_shipping", "in_shipping"));
        rule8.action.transitions.add(new BusinessRuleModel.Transition("Shipment", "ready_for_shipping", "in_shipping"));

        rule8.preConditions.add(new BusinessRuleModel.Atom("Order", null, "ready_for_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule8.preConditions.add(new BusinessRuleModel.Atom("Shipment", null, "ready_for_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule8.preConditions.add(new BusinessRuleModel.Atom("Shipment", "shipStartDate", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));

        rule8.postConditions.add(new BusinessRuleModel.Atom("Order", null, "in_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule8.postConditions.add(new BusinessRuleModel.Atom("Shipment", null, "in_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));

        model1.businessRules.add(rule8);

        // rule9 shipped service rule
        BusinessRuleModel rule9 = new BusinessRuleModel();
        rule9.name = "rule9_shipped";
        rule9.action = new BusinessRuleModel.Action();
        rule9.action.service = "shipped";
        rule9.action.transitions.add(new BusinessRuleModel.Transition("Order", "in_shipping", "shipped"));
        rule9.action.transitions.add(new BusinessRuleModel.Transition("Shipment", "in_shipping", "shipping_completed"));

        rule9.preConditions.add(new BusinessRuleModel.Atom("Order", null, "in_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule9.preConditions.add(new BusinessRuleModel.Atom("Shipment", "shipEndDate", null, null, BusinessRuleModel.AtomType.ATTRIBUTE_DEFINED, null));
        rule9.preConditions.add(new BusinessRuleModel.Atom("Shipment", null, "in_shipping", null, BusinessRuleModel.AtomType.INSTATE, null));

        rule9.postConditions.add(new BusinessRuleModel.Atom("Order", null, "shipped", null, BusinessRuleModel.AtomType.INSTATE, null));
        rule9.postConditions.add(new BusinessRuleModel.Atom("Shipment", null, "shipping_completed", null, BusinessRuleModel.AtomType.INSTATE, null));


        model1.businessRules.add(rule9);

        // rule10 close service rule
        BusinessRuleModel rule10 = new BusinessRuleModel();
        rule10.name = "rule10_close";
        rule10.action = new BusinessRuleModel.Action();
        rule10.action.service = "close";
        rule10.action.transitions.add(new BusinessRuleModel.Transition("Order", "shipped", "closed"));

        rule10.preConditions.add(new BusinessRuleModel.Atom("Order", null, "shipped", null, BusinessRuleModel.AtomType.INSTATE, null));

        rule10.postConditions.add(new BusinessRuleModel.Atom("Order", null, "closed", null, BusinessRuleModel.AtomType.INSTATE, null));

        model1.businessRules.add(rule10);

        processModelRepository.save(model1);

        return model1;
    }
}
