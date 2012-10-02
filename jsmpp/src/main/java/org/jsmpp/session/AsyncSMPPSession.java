package org.jsmpp.session;

import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.connection.ConnectionFactory;
import org.jsmpp.session.state.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * User: CVrabie1
 * Date: 31/10/11
 * {@link SMPPSession} that  alters the default behavior of sending receipts (deliver_sm_resp) right after the processing
 * of the message. <b>Please note that some other component HAS to take up the responsibility of
 * sending receipts by explicitly calling the method {@link #sendMessageReceipt(int, int, org.jsmpp.session.state.Receipt)}.</b>
 */
public class AsyncSMPPSession extends SMPPSession{
  protected final Logger LOG = LoggerFactory.getLogger(getClass());
  private final AsyncResponseHandler asyncResponseHandler = newResponseHandler();

  protected AsyncResponseHandler newResponseHandler() {
    return new AsyncResponseHandler();
  }

  public AsyncSMPPSession() {
    super();
    setResponseHandler(asyncResponseHandler);
  }

  public AsyncSMPPSession(final PDUSender pduSender, final PDUReader pduReader, final ConnectionFactory connFactory) {
    super(pduSender, pduReader, connFactory);
    setResponseHandler(asyncResponseHandler);
  }

  /**
   * Sends a message receipt for the {@link DeliverSm} with the specified command id and sequence number.
   * If the passed response is {@link Receipt#SUCCESS} then a positive acknowledgement is sent, otherwise
   * a negative one.
   * @param commandId
   * @param seqNumber
   * @param resp
   */
  public void sendMessageReceipt(int commandId, int seqNumber, final Receipt resp) {
    if(Receipt.SUCCESS.equals(resp)){
      sayYes(commandId, seqNumber);
    } else {
      if(Receipt.CONTINUE.equals(resp)){
        //this should never happen but can be ignored
        LOG.warn("Receipt.CONTINUE should have never got here!");
      }
      if(null != resp && resp.cause instanceof ProcessRequestException){
        //use explicit error code if provided
        sayNo(commandId, seqNumber, ((ProcessRequestException)resp.cause).getErrorCode());
      }else{
        //use default error code
        sayNo(commandId,seqNumber, Receipt.DEFAULT_ERROR_CODE);
      }
    }
  }

  protected void sayYes(final int commandId, final int sequenceNumber){
    try {
      LOG.debug("SUCCESS deliver_sm_resp for seqNum {}",sequenceNumber);
      asyncResponseHandler.sendDeliverSmRespForReal(0, sequenceNumber);
    } catch (IOException e) {
      LOG.error("Need to send positive for command "+commandId+" with seqNo "+sequenceNumber+" response but could not!",e);
    }
  }

  protected void sayNo(final int commandId, final int sequenceNumber, final int errorCode){
    try {
      LOG.debug("FAILURE({}) deliver_sm_resp for seqNum {}",errorCode,sequenceNumber);
      asyncResponseHandler.sendNegativeResponse(commandId, errorCode, sequenceNumber);
    } catch (IOException e) {
      LOG.error("Need to send negative for command "+commandId+" with seqNo "+sequenceNumber+" response but could not!",e);
    }
  }

  /**
   * Internal {@link org.jsmpp.session.ResponseHandler} that overrides the default SM processing so it  block the default sending of delivery receipt
   * after processing a {@link DeliverSm}. Instead it exposes the method {@link #sendDeliverSmRespForReal(int,int)}
   * which can be used to explicitly send an receipt.
   */
  protected class AsyncResponseHandler extends ResponseHandlerImpl{
    @Override
    public void sendDeliverSmResp(int commandStatus, int sequenceNumber) throws IOException {
      LOG.debug("Ignoring default request to send deliver_sm_resp for message with sequence number {}.",sequenceNumber);
    }

    public void sendDeliverSmRespForReal(int commandStatus, int sequenceNumber) throws IOException {
      super.sendDeliverSmResp(commandStatus, sequenceNumber);
    }
  }
}


