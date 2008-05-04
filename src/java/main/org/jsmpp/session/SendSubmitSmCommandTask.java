package org.jsmpp.session;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;

/**
 * @author uudashr
 *
 */
public class SendSubmitSmCommandTask extends AbstractSendCommandTask {
    private final String serviceType;
    private final TypeOfNumber sourceAddrTon;
    private final NumberingPlanIndicator sourceAddrNpi;
    private final String sourceAddr;
    private final TypeOfNumber destAddrTon;
    private final NumberingPlanIndicator destAddrNpi;
    private final String destinationAddr;
    private final ESMClass esmClass;
    private final byte protocolId;
    private final byte priorityFlag;
    private final String scheduleDeliveryTime;
    private final String validityPeriod;
    private final RegisteredDelivery registeredDelivery;
    private final byte replaceIfPresentFlag;
    private final DataCoding dataCoding;
    private final byte smDefaultMsgId;
    private final byte[] shortMessage;
    private final OptionalParameter[] optionalParameters;
    
    public SendSubmitSmCommandTask(PDUSender pduSender, String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddr,
            ESMClass esmClass, byte protocoId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag,
            DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) {
        
        super(pduSender);
        this.serviceType = serviceType;
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrNpi;
        this.sourceAddr = sourceAddr;
        this.destAddrTon = destAddrTon;
        this.destAddrNpi = destAddrNpi;
        this.destinationAddr = destinationAddr;
        this.esmClass = esmClass;
        this.protocolId = protocoId;
        this.priorityFlag = priorityFlag;
        this.scheduleDeliveryTime = scheduleDeliveryTime;
        this.validityPeriod = validityPeriod;
        this.registeredDelivery = registeredDelivery;
        this.replaceIfPresentFlag = replaceIfPresentFlag;
        this.dataCoding = dataCoding;
        this.smDefaultMsgId = smDefaultMsgId;
        this.shortMessage = shortMessage;
        this.optionalParameters = optionalParameters;
    }

    public void executeTask(OutputStream out, int sequenceNumber)
            throws PDUStringException, IOException {
        pduSender.sendSubmitSm(out, sequenceNumber, serviceType, sourceAddrTon,
                sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi,
                destinationAddr, esmClass, protocolId, priorityFlag,
                scheduleDeliveryTime, validityPeriod, registeredDelivery,
                replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage,
                optionalParameters);
    }
    
    public String getCommandName() {
        return "submit_sm";
    }
}
